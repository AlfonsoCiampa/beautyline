package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.OrderItemDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.mapper.OrderItemMapper;
import com.beautyline.polimi.mapper.OrderMapper;
import com.beautyline.polimi.repository.*;
import com.beautyline.polimi.service.stripe.StripeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderService {

	private final OrderRepository orderRepository;
	private final AccountRepository accountRepository;
	private final ConsumerRepository consumerRepository;
	private final StripeService stripeService;
	private final ProductRepository productRepository;
	private final PackageRepository packageRepository;
	private final OrderItemRepository orderItemRepository;

	private ConsumerEntity getConsumer() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			User user = ((User) authentication.getPrincipal());

			if (!user.getAuthorities().contains(new SimpleGrantedAuthority(AccountEntity.Type.CONSUMER.name()))) {
				throw new JWTService.TokenVerificationException();
			}

			AccountEntity account = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, user.getUsername())
				.orElseThrow(JWTService.TokenVerificationException::new);
			return consumerRepository.findById(account.getReferenceId())
				.orElseThrow(JWTService.TokenVerificationException::new);
		}
		throw new JWTService.TokenVerificationException();
	}

	public OrderDTO getOrder(Long id) {
		ConsumerEntity consumer = getConsumer();
		OrderEntity orderEntity = orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid ID"));
		if (!Objects.equals(orderEntity.getConsumerId(), consumer.getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}
		return OrderMapper.entityToDto(orderEntity);
	}

	public Page<OrderDTO> getOrders(Integer page, Integer size) {
		return OrderMapper.entitiesToDtoPage(orderRepository.findAllByConsumerId(
			getConsumer().getId(),
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public OrderDTO place(PlaceOrderDTO placeOrderDTO) {

		OrderDTO orderDTO = new OrderDTO();

		// consumerId validation
		ConsumerEntity consumerEntity = getConsumer();

		// price validation
		if (placeOrderDTO.getPrice() == null || placeOrderDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("Invalid price");
		}

		// create a list of orderItemDTO
		List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
		BigDecimal total = BigDecimal.ZERO;

		// items validation (there must be one of them at least)
		if (placeOrderDTO.getItems() != null && !placeOrderDTO.getItems().isEmpty()) {
			for (Long itemId : placeOrderDTO.getItems()) {
				ProductEntity productEntity = productRepository.findById(itemId)
					.orElseThrow(() -> new IllegalArgumentException("Invalid items"));

				// if exist save it as orderItemDTO
				OrderItemDTO orderItemDTO = new OrderItemDTO();
				orderItemDTO.setProductId(itemId);
				orderItemDTO.setPrice(productEntity.getPrice());
				orderItemDTO.setRetrieved(false);
				// add it to the list
				orderItemDTOList.add(orderItemDTO);
				// and add the price to the total variable
				total = total.add(orderItemDTO.getPrice());
			}
		}

		// packageCode validation
		if (placeOrderDTO.getPackageCode() != null) {
			Optional<PackageEntity> packageEntityOpt = packageRepository.findByCode(placeOrderDTO.getPackageCode());
			if (packageEntityOpt.isEmpty()) {
				throw new IllegalArgumentException("Invalid package code");
			}

			// if exists
			PackageEntity packageEntity = packageEntityOpt.get();
			total = total.add(packageEntity.getPrice());

			for (PackageItemEntity packageItemEntity : packageEntity.getPackageItems()) {
				// if exist save it as orderItemDTO
				OrderItemDTO orderItemDTO = new OrderItemDTO();
				orderItemDTO.setProductId(packageItemEntity.getProductId());
				orderItemDTO.setPrice(BigDecimal.ZERO);
				orderItemDTO.setRetrieved(false);
				// add it to the list
				orderItemDTOList.add(orderItemDTO);
			}

			//// set packageCode
			orderDTO.setPackageCode(placeOrderDTO.getPackageCode());
		}


		if (orderItemDTOList.isEmpty()) {
			throw new IllegalArgumentException("Invalid order");
		}

		//// set orderItems
		orderDTO.setOrderItems(orderItemDTOList);

		if (total.compareTo(placeOrderDTO.getPrice()) != 0) {
			throw new IllegalArgumentException("Invalid price");
		}

		//// set price
		orderDTO.setPrice(total);

		// getPaymetId validation
		if (placeOrderDTO.getPaymentId() == null || placeOrderDTO.getPaymentId().equals("")) {
			throw new IllegalArgumentException("Invalid Payment ID");
		}

		// isIntentId validation
		if (placeOrderDTO.getIsIntentId() == null) {
			throw new IllegalArgumentException("Invalid boolean");
		}

		if (placeOrderDTO.getNote() == null) {
			//// set note
			orderDTO.setNote("");
		} else {
			//// set note
			orderDTO.setNote(placeOrderDTO.getNote());
		}

		//// set date
		orderDTO.setDate(LocalDateTime.now());

		Map<Long, Long> productToQuantity = new HashMap<>();

		// look if the products are available
		for (Long productId : placeOrderDTO.getItems()) {
			productToQuantity.put(productId, productToQuantity.getOrDefault(productId, 0L) + 1);
		}

		productToQuantity.forEach((productId, productQuantity) -> {
			ProductEntity product = productRepository.findById(productId)
				.orElseThrow(() -> new IllegalArgumentException("Invalid items"));
			if (product.getQuantity() >= productQuantity) {
				product.setQuantity(product.getQuantity() - productQuantity);
				productRepository.save(product);
			} else {
				throw new IllegalArgumentException("Invalid quantity");
			}
		});

		stripeService.pay(orderDTO.getPrice(), placeOrderDTO.getPaymentId(), placeOrderDTO.getIsIntentId());

		orderDTO.setOrderItems(new ArrayList<>());
		orderDTO.setConsumerId(consumerEntity.getId());
		OrderEntity orderEntity = OrderMapper.dtoToEntity(orderDTO);
		orderEntity = orderRepository.save(orderEntity);
		orderDTO.setId(orderEntity.getId());

		for (OrderItemDTO o : orderItemDTOList) {
			o.setOrderId(orderEntity.getId());
			OrderItemEntity orderItem = OrderItemMapper.dtoToEntity(o);
			orderItem = orderItemRepository.save(orderItem);
			orderEntity.getOrderItems().add(orderItem);
			orderDTO.getOrderItems().add(OrderItemMapper.entityToDto(orderItem));
		}

		return orderDTO;
	}

}
