package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.SecurityTestHelper;
import com.beautyline.polimi.dto.*;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.ConsumerEntity;
import com.beautyline.polimi.entity.GiftEntity;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import com.beautyline.polimi.repository.GiftRepository;
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
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GiftServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private GiveProductService giveProductService;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private GiftService giftService;
	@Autowired
	private SecurityTestHelper securityTestHelper;
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

		String account2Mail = "email@test.it";
		String default2Password = "Password2";

		ConsumerEntity consumer2Entity = consumerRepository.save(ConsumerEntity.builder()
			.name("Even")
			.surname("Pepa")
			.phone("+383123567283")
			.build());
		accountRepository.save(AccountEntity.builder()
			.email(account2Mail)
			.password(default2Password)
			.referenceType(role)
			.referenceId(consumer2Entity.getId())
			.build()
		);
	}

	private void giveProduct(String fromEmail, String toEmail) {
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

		OrderDTO order = securityTestHelper.executeAsConsumer(fromEmail, () -> orderService.place(placeOrderDTO));
		OrderItemDTO orderItem = order.getOrderItems().get(0);

		GiveProductDTO giveProductDTO = new GiveProductDTO(null, orderItem.getId(), toEmail);
		securityTestHelper.executeAsConsumer(fromEmail, () -> giveProductService.create(giveProductDTO));
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getGiftDone() {
		// arrange
		giveProduct("email@test.it", "account@mail.it");
		GiftEntity giftEntity = giftRepository.findAll().get(0);

		// act
		GiftDTO result = giftService.getGift(giftEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(giftEntity.getId(), result.getId());
		Assertions.assertEquals(giftEntity.getConsumerId(), result.getConsumerId());
		Assertions.assertEquals(giftEntity.getOrderItemId(), result.getOrderItemId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"CONSUMER"})
	public void getGiftsDone() {
		// arrange
		giveProduct("email@test.it", "account@mail.it");
		GiftEntity giftEntity = giftRepository.findAll().get(0);

		// act
		Page<GiftDTO> result = giftService.getGifts(null, null);

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

}
