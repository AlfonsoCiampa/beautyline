package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.entity.*;
import com.beautyline.polimi.repository.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminBookingServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BookingRepository bookingRepository;
	@Autowired
	private AdminBookingService adminBookingService;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private TreatmentRepository treatmentRepository;
	@Autowired
	private BookingConfigurationRepository bookingConfigurationRepository;

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
	public void getBookingDone() {
		// arrange
		createDone();
		BookingEntity bookingEntity = bookingRepository.findAll().get(0);

		// act
		BookingDTO result = adminBookingService.getBooking(bookingEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(bookingEntity.getId(), result.getId());
		Assertions.assertEquals(bookingEntity.getTreatmentId(), result.getTreatmentId());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getBookingsDone() {
		// arrange
		createDone();
		BookingEntity bookingEntity = bookingRepository.findAll().get(0);

		// act
		Page<BookingDTO> result = adminBookingService.getBookings(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		BookingDTO booking = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(booking);
		Assertions.assertEquals(bookingEntity.getId(), booking.getId());
		Assertions.assertEquals(bookingEntity.getTreatmentId(), booking.getTreatmentId());
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

		TreatmentEntity treatmentEntity = treatmentRepository.save(TreatmentEntity.builder()
			.name("Treatment1")
			.description("Is perfect for you")
			.duration(1800L)
			.price(BigDecimal.TEN)
			.obscure(false)
			.build());

		LocalDateTime now = LocalDateTime.now();
		bookingConfigurationRepository.save(BookingConfigurationEntity.builder()
			.date(now.getDayOfWeek().name())
			.startTime(LocalTime.of(0,0,0))
			.endTime(LocalTime.of(23,59,59))
			.build());
		bookingConfigurationRepository.save(BookingConfigurationEntity.builder()
			.date(now.plusDays(1).getDayOfWeek().name())
			.startTime(LocalTime.of(0,0,0))
			.endTime(LocalTime.of(23,59,59))
			.build());

		BookingDTO bookingDTO = new BookingDTO(null, consumerEntity.getId(), treatmentEntity.getId(),
			null, now.plus(Duration.ofMinutes(5)), null);

		// act
		adminBookingService.create(bookingDTO);

		// assert
		Assertions.assertEquals(1, bookingRepository.count());
		BookingEntity bookingEntity = bookingRepository.findAll().get(0);
		Assertions.assertEquals(bookingDTO.getConsumerId(), bookingEntity.getConsumerId());
		Assertions.assertEquals(bookingDTO.getNote(), bookingEntity.getNote());
		Assertions.assertEquals(bookingDTO.getTreatmentId(), bookingEntity.getTreatmentId());
		Assertions.assertEquals(bookingDTO.getStartTime(), bookingEntity.getStartTime());
		Assertions.assertEquals(bookingDTO.getStartTime().plusSeconds(1800L), bookingEntity.getEndTime());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		LocalDateTime now = LocalDateTime.now();
		BookingEntity bookingEntity = bookingRepository.findAll().get(0);
		BookingDTO bookingDTO = new BookingDTO(bookingEntity.getId(), bookingEntity.getConsumerId(), bookingEntity.getTreatmentId(),
			null, now.plus(Duration.ofMinutes(1)), null);

		// act
		adminBookingService.update(bookingDTO);

		// assert
		Assertions.assertEquals(1, bookingRepository.count());
		BookingEntity bookingEntity2 = bookingRepository.findAll().get(0);
		Assertions.assertEquals(bookingDTO.getId(), bookingEntity2.getId());
		Assertions.assertEquals(bookingDTO.getConsumerId(), bookingEntity2.getConsumerId());
		Assertions.assertEquals(bookingDTO.getNote(), bookingEntity2.getNote());
		Assertions.assertEquals(bookingDTO.getTreatmentId(), bookingEntity2.getTreatmentId());
		Assertions.assertEquals(bookingDTO.getStartTime(), bookingEntity2.getStartTime());
		Assertions.assertEquals(bookingDTO.getStartTime().plusSeconds(1800L), bookingEntity2.getEndTime());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		BookingEntity bookingEntity = bookingRepository.findAll().get(0);

		// act
		adminBookingService.delete(bookingEntity.getId());

		// assert
		Assertions.assertEquals(0, bookingRepository.count());
	}

}
