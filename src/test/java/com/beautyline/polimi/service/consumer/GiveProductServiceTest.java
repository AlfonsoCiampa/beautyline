package com.beautyline.polimi.service.consumer;

import com.beautyline.polimi.dto.GiveProductDTO;
import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.OrderItemDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
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
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GiveProductServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private GiftRepository giftRepository;
	@Autowired
	private GiveProductService giveProductService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private ProductRepository productRepository;
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
	public void createDone() {
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

		OrderDTO order = orderService.place(placeOrderDTO);
		OrderItemDTO orderItem = order.getOrderItems().get(0);
		System.out.println(orderItem.getId());

		AccountEntity.Type role = AccountEntity.Type.CONSUMER;
		String account2Mail = "email@test.it";
		String default2Password = "Password2";

		ConsumerEntity consumerEntity = consumerRepository.save(ConsumerEntity.builder()
			.name("Even")
			.surname("Pepa")
			.phone("+383123567283")
			.build());
		accountRepository.save(AccountEntity.builder()
			.email(account2Mail)
			.password(default2Password)
			.referenceType(role)
			.referenceId(consumerEntity.getId())
			.build()
		);

		GiveProductDTO giveProductDTO = new GiveProductDTO(null, orderItem.getId(), account2Mail);

		// act
		giveProductService.create(giveProductDTO);

		// assert
		Assertions.assertEquals(1, giftRepository.count());
		GiftEntity giftEntity = giftRepository.findAll().get(0);
		Assertions.assertEquals(giveProductDTO.getConsumerToGiveEmail(),
			accountRepository.findByReferenceId(giftEntity.getConsumerId()).get().getEmail());
		Assertions.assertEquals(giveProductDTO.getOrderItemId(), giftEntity.getOrderItemId());
	}

}
