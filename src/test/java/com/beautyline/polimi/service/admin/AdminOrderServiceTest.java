package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.SecurityTestHelper;
import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.entity.OrderEntity;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.repository.OrderRepository;
import com.beautyline.polimi.repository.ProductRepository;
import com.beautyline.polimi.service.consumer.OrderService;
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
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminOrderServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AdminOrderService adminOrderService;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private ProductRepository productRepository;
	@MockBean
	@SuppressWarnings("unused")
	private StripeService stripeService;
	@Autowired
	private SecurityTestHelper securityTestHelper;

	@BeforeEach
	public void beforeEach() {
		AccountEntity.Type role = AccountEntity.Type.ADMIN;
		String accountMail = "account@mail.it";
		String defaultPassword = "TestPassword123";

		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(role)
			.build()
		);
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getOrderDone() {
		// arrange
		String accountMail = "accountTest@mail.it";
		String defaultPassword = "TestPassword123";
		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Nicola")
			.surname("Ciampa")
			.phone("+393123567283")
			.build()
		);
		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(AccountEntity.Type.CONSUMER)
			.referenceId(consumerEntity.getId())
			.build()
		);

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

		securityTestHelper.executeAsConsumer(accountMail, () -> orderService.place(placeOrderDTO));

		OrderEntity orderEntity = orderRepository.findAll().get(0);

		// act
		OrderDTO result = adminOrderService.getOrder(orderEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(orderEntity.getId(), result.getId());
		Assertions.assertEquals(orderEntity.getConsumerId(), result.getConsumerId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getOrdersDone() {
		// arrange
		String accountMail = "accountTest@mail.it";
		String defaultPassword = "TestPassword123";
		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Nicola")
			.surname("Ciampa")
			.phone("+393123567283")
			.build()
		);
		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(AccountEntity.Type.CONSUMER)
			.referenceId(consumerEntity.getId())
			.build()
		);

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

		securityTestHelper.executeAsConsumer(accountMail, () -> orderService.place(placeOrderDTO));

		OrderEntity orderEntity = orderRepository.findAll().get(0);

		// act
		Page<OrderDTO> result = adminOrderService.getOrders(null, null);

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
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		String accountMail = "accountTest@mail.it";
		String defaultPassword = "TestPassword123";
		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Nicola")
			.surname("Ciampa")
			.phone("+393123567283")
			.build()
		);
		accountRepository.save(AccountEntity.builder()
			.email(accountMail)
			.password(defaultPassword)
			.referenceType(AccountEntity.Type.CONSUMER)
			.referenceId(consumerEntity.getId())
			.build()
		);

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

		securityTestHelper.executeAsConsumer(accountMail, () -> orderService.place(placeOrderDTO));

		OrderEntity orderEntity = orderRepository.findAll().get(0);

		// act
		Assertions.assertEquals(1, orderRepository.count());
		adminOrderService.delete(orderEntity.getId());

		// assert
		Assertions.assertEquals(0, orderRepository.count());
	}
}
