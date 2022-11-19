package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.BookingConfigurationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.BookingConfigurationEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.BookingConfigurationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminBookingConfigurationServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private BookingConfigurationRepository bookingConfigurationRepository;
	@Autowired
	private AdminBookingConfigurationService adminBookingConfigurationService;

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
	public void getBookingConfigurationDone() {
		// arrange
		createDone();
		BookingConfigurationEntity bookingConfigurationEntity = bookingConfigurationRepository.findAll().get(0);

		// act
		BookingConfigurationDTO result = adminBookingConfigurationService.getBookingConfiguration(bookingConfigurationEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(bookingConfigurationEntity.getId(), result.getId());
		Assertions.assertEquals(bookingConfigurationEntity.getDate(), result.getDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getBookingConfigurationsDone() {
		// arrange
		createDone();
		BookingConfigurationEntity bookingConfigurationEntity = bookingConfigurationRepository.findAll().get(0);

		// act
		Page<BookingConfigurationDTO> result = adminBookingConfigurationService.getBookingConfigurations(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		BookingConfigurationDTO bookingConfiguration = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(bookingConfiguration);
		Assertions.assertEquals(bookingConfigurationEntity.getId(), bookingConfiguration.getId());
		Assertions.assertEquals(bookingConfigurationEntity.getDate(), bookingConfiguration.getDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		BookingConfigurationDTO bookingConfigurationDTO = new BookingConfigurationDTO(null, DayOfWeek.MONDAY.name(), LocalTime.of(8, 0, 0), LocalTime.of(18, 0, 0));

		// act
		adminBookingConfigurationService.create(bookingConfigurationDTO);

		// assert
		Assertions.assertEquals(1, bookingConfigurationRepository.count());
		BookingConfigurationEntity bookingConfigurationEntity = bookingConfigurationRepository.findAll().get(0);
		Assertions.assertEquals(bookingConfigurationDTO.getDate(), bookingConfigurationEntity.getDate());
		Assertions.assertEquals(bookingConfigurationDTO.getStartTime(), bookingConfigurationEntity.getStartTime());
		Assertions.assertEquals(bookingConfigurationDTO.getEndTime(), bookingConfigurationEntity.getEndTime());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		BookingConfigurationEntity bookingConfigurationEntity = bookingConfigurationRepository.findAll().get(0);
		BookingConfigurationDTO bookingConfigurationDTO = new BookingConfigurationDTO(bookingConfigurationEntity.getId(), DayOfWeek.TUESDAY.name(), LocalTime.of(7, 30, 0), LocalTime.of(12, 30, 0));

		// act
		adminBookingConfigurationService.update(bookingConfigurationDTO);

		// assert
		Assertions.assertEquals(1, bookingConfigurationRepository.count());
		BookingConfigurationEntity bookingConfigurationEntity2 = bookingConfigurationRepository.findAll().get(0);
		Assertions.assertEquals(bookingConfigurationDTO.getId(), bookingConfigurationEntity2.getId());
		Assertions.assertEquals(bookingConfigurationDTO.getDate(), bookingConfigurationEntity2.getDate());
		Assertions.assertEquals(bookingConfigurationDTO.getStartTime(), bookingConfigurationEntity2.getStartTime());
		Assertions.assertEquals(bookingConfigurationDTO.getEndTime(), bookingConfigurationEntity2.getEndTime());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		BookingConfigurationEntity bookingConfigurationEntity = bookingConfigurationRepository.findAll().get(0);

		// act
		adminBookingConfigurationService.delete(bookingConfigurationEntity.getId());

		// assert
		Assertions.assertEquals(0, bookingConfigurationRepository.count());
	}

}