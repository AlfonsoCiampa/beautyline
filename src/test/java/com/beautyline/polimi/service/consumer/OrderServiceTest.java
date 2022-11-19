package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.repository.OrderRepository;
import com.beautyline.polimi.repository.ProductRepository;
import com.beautyline.polimi.service.stripe.StripeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderServiceTest {
	private final MathContext MATH_CONTEXT = new MathContext(2, RoundingMode.HALF_UP);
	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private OrderService orderService;
	@MockBean
	@SuppressWarnings("unused")
	private StripeService stripeService;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.CONSUMER;
		String accountMail = "account@mail.it";
		String defaultPassword = "TestPassword123";

		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Nicola")
			.surname("Ciampa")
			.phone("+393123567283")
			.build());
		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.referenceId(consumerEntity.getId())
			.build()
		);
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getOrderDone() {
		// arrange
		placeDone();
		OrderEntity orderEntity = orderRepository.findAll().get(0);

		// act
		OrderDTO result = orderService.getOrder(orderEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(orderEntity.getId(), result.getId());
		Assertions.assertEquals(orderEntity.getConsumerId(), result.getConsumerId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getOrdersDone() {
		// arrange
		placeDone();
		OrderEntity orderEntity = orderRepository.findAll().get(0);

		// act
		Page<OrderDTO> result = orderService.getOrders(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		OrderDTO order = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(order);
		Assertions.assertEquals(orderEntity.getId(), order.getId());
		Assertions.assertEquals(orderEntity.getConsumerId(), order.getConsumerId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void placeDone() {
		// arrange
		ProductEntity productEntity = new ProductEntity();
		productEntity.setName("Cream");
		productEntity.setDescription("Cream");
		productEntity.setPrice(BigDecimal.TEN);
		productEntity.setQuantity(100L);
		productEntity = productRepository.save(productEntity);

		PlaceOrderDTO placeOrderDTO = new PlaceOrderDTO();
		placeOrderDTO.setPrice(BigDecimal.TEN);
		placeOrderDTO.setPaymentId("testId");
		placeOrderDTO.setIsIntentId(false);
		placeOrderDTO.setItems(List.of(productEntity.getId()));

		// act
		OrderDTO result = orderService.place(placeOrderDTO);


		// assert
		List<OrderEntity> orders = orderRepository.findAll();
		Assertions.assertEquals(1, orders.size());
		OrderEntity order = orders.get(0);
		Assertions.assertEquals(BigDecimal.TEN.round(MATH_CONTEXT), order.getPrice().round(MATH_CONTEXT));
		Assertions.assertEquals(1, order.getOrderItems().size());
		OrderItemEntity creamItem = order.getOrderItems().get(0);
		Assertions.assertEquals(productEntity.getId(), creamItem.getProductId());
		Assertions.assertEquals(BigDecimal.TEN.round(MATH_CONTEXT), creamItem.getPrice().round(MATH_CONTEXT));

		Assertions.assertEquals(BigDecimal.TEN.round(MATH_CONTEXT), result.getPrice().round(MATH_CONTEXT));
	}

}
