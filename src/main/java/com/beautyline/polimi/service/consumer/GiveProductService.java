package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.configuration.security.jwt.JWTService;
import com.beautyline.polimi.dto.GiveProductDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.entity.GiftEntity;
import com.beautyline.polimi.entity.OrderEntity;
import com.beautyline.polimi.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class GiveProductService {
	private final OrderRepository orderRepository;
	private final GiftRepository giftRepository;

	private final OrderItemRepository orderItemRepository;
	private final ConsumerRepository consumerRepository;
	private final AccountRepository accountRepository;

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

	public GiveProductDTO create(GiveProductDTO giveProductDTO) {

		giveProductDTOControls(giveProductDTO);

		Long consumerId = accountRepository
			.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, giveProductDTO.getConsumerToGiveEmail())
			.orElseThrow(() -> new IllegalArgumentException("Invalid consumerToGiveEmail")).getReferenceId();

		giftRepository.save(GiftEntity.builder()
			.consumerId(consumerId)
			.orderItemId(giveProductDTO.getOrderItemId())
			.build());

		return giveProductDTO;
	}

	public void giveProductDTOControls(GiveProductDTO giveProductDTO) {

		// orderItemId validation
		if (giveProductDTO.getOrderItemId() == null || orderItemRepository.findById(giveProductDTO.getOrderItemId()).isEmpty()) {
			throw new IllegalArgumentException("Invalid orderItem ID");
		}

		// owner validation
		if (!Objects.equals(orderItemRepository.findById(giveProductDTO.getOrderItemId())
			.flatMap(orderItem -> orderRepository.findById(orderItem.getOrderId()).map(OrderEntity::getConsumerId))
			.orElse(null), getConsumer().getId())) {
			throw new IllegalArgumentException("Invalid owner");
		}

		// consumerToGiveEmail validation
		if (giveProductDTO.getConsumerToGiveEmail() == null || accountRepository
			.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, giveProductDTO.getConsumerToGiveEmail()).isEmpty()) {
			throw new IllegalArgumentException("Invalid email of consumer to give");
		}

		// different consumers validation
		if (Objects.equals(getConsumer().getId(), accountRepository
			.findByReferenceTypeAndEmail(AccountEntity.Type.CONSUMER, giveProductDTO.getConsumerToGiveEmail()).get().getId())) {
			throw new IllegalArgumentException("Invalid, you are giving the product to yourself");
		}
	}

}
