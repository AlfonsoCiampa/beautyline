package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.SecurityTestHelper;
import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.repository.*;
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
public class AdminGiftServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private AdminGiftService adminGiftService;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
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
	public void getGiftDone() {
		// arrange
		createDone();
		GiftEntity giftEntity = giftRepository.findAll().get(0);

		// act
		GiftDTO result = adminGiftService.getGift(giftEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(giftEntity.getId(), result.getId());
		Assertions.assertEquals(giftEntity.getConsumerId(), result.getConsumerId());
		Assertions.assertEquals(giftEntity.getOrderItemId(), result.getOrderItemId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getGiftsDone() {
		// arrange
		createDone();
		GiftEntity giftEntity = giftRepository.findAll().get(0);

		// act
		Page<GiftDTO> result = adminGiftService.getGifts(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		GiftDTO gift = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(gift);
		Assertions.assertEquals(giftEntity.getId(), gift.getId());
		Assertions.assertEquals(giftEntity.getConsumerId(), gift.getConsumerId());
		Assertions.assertEquals(giftEntity.getOrderItemId(), gift.getOrderItemId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
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
		OrderEntity order = orderRepository.findAll().get(0);
		OrderItemEntity orderItem = order.getOrderItems().get(0);

		String account2Mail = "emailTest@test.it";
		String default2Password = "Password2";

		ConsumerEntity consumer2Entity = consumerRepository.save(ConsumerEntity.builder()
			.name("Even")
			.surname("Pepa")
			.phone("+383123567283")
			.build()
		);
		accountRepository.save(AccountEntity.builder()
			.email(account2Mail)
			.password(default2Password)
			.referenceType(AccountEntity.Type.CONSUMER)
			.referenceId(consumer2Entity.getId())
			.build()
		);

		GiftDTO giftDTO = new GiftDTO(null, orderItem.getId(), consumer2Entity.getId());

		// act
		adminGiftService.create(giftDTO);

		// assert
		Assertions.assertEquals(1, giftRepository.count());
		GiftEntity giftEntity = giftRepository.findAll().get(0);
		Assertions.assertEquals(giftDTO.getConsumerId(), giftEntity.getConsumerId());
		Assertions.assertEquals(giftDTO.getOrderItemId(), giftEntity.getOrderItemId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();

		String account3Mail = "emailTest2@test.it";
		String default3Password = "Password2";

		ConsumerEntity consumer3Entity = consumerRepository.save(ConsumerEntity.builder()
			.name("Even2")
			.surname("Pepa")
			.phone("+383123567283")
			.build()
		);
		accountRepository.save(AccountEntity.builder()
			.email(account3Mail)
			.password(default3Password)
			.referenceType(AccountEntity.Type.CONSUMER)
			.referenceId(consumer3Entity.getId())
			.build()
		);

		GiftEntity giftEntity = giftRepository.findAll().get(0);
		GiftDTO giftDTO = new GiftDTO(giftEntity.getId(), giftEntity.getOrderItemId(), consumer3Entity.getId());

		// act
		adminGiftService.update(giftDTO);

		// assert
		Assertions.assertEquals(1, giftRepository.count());
		GiftEntity giftEntity2 = giftRepository.findAll().get(0);
		Assertions.assertEquals(giftDTO.getId(), giftEntity2.getId());
		Assertions.assertEquals(giftDTO.getConsumerId(), giftEntity2.getConsumerId());
		Assertions.assertEquals(giftDTO.getOrderItemId(), giftEntity2.getOrderItemId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		GiftEntity giftEntity = giftRepository.findAll().get(0);

		// act
		adminGiftService.delete(giftEntity.getId());

		// assert
		Assertions.assertEquals(0, giftRepository.count());
	}

}
