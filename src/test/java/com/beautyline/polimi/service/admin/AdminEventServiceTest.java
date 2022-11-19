package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.EventEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.EventRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminEventServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private AdminEventService adminEventService;

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
	public void getEventDone() {
		// arrange
		createDone();
		EventEntity eventEntity = eventRepository.findAll().get(0);

		// act
		EventDTO result = adminEventService.getEvent(eventEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(eventEntity.getId(), result.getId());
		Assertions.assertEquals(eventEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getEventsDone() {
		// arrange
		createDone();
		EventEntity eventEntity = eventRepository.findAll().get(0);

		// act
		Page<EventDTO> result = adminEventService.getEvents(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		EventDTO event = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(event);
		Assertions.assertEquals(eventEntity.getId(), event.getId());
		Assertions.assertEquals(eventEntity.getName(), event.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		EventDTO eventDTO = new EventDTO(null, "Event", "New event", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(8888888L));

		// act
		adminEventService.create(eventDTO);

		// assert
		Assertions.assertEquals(1, eventRepository.count());
		EventEntity eventEntity = eventRepository.findAll().get(0);
		Assertions.assertEquals(eventDTO.getName(), eventEntity.getName());
		Assertions.assertEquals(eventDTO.getDescription(), eventEntity.getDescription());
		Assertions.assertEquals(eventDTO.getStartDate(), eventEntity.getStartDate());
		Assertions.assertEquals(eventDTO.getEndDate(), eventEntity.getEndDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		EventEntity eventEntity = eventRepository.findAll().get(0);
		EventDTO eventDTO = new EventDTO(eventEntity.getId(), "Event2", "New event!!!!", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(9999999L));

		// act
		adminEventService.update(eventDTO);

		// assert
		Assertions.assertEquals(1, eventRepository.count());
		EventEntity eventEntity2 = eventRepository.findAll().get(0);
		Assertions.assertEquals(eventDTO.getId(), eventEntity2.getId());
		Assertions.assertEquals(eventDTO.getName(), eventEntity2.getName());
		Assertions.assertEquals(eventDTO.getDescription(), eventEntity2.getDescription());
		Assertions.assertEquals(eventDTO.getStartDate(), eventEntity2.getStartDate());
		Assertions.assertEquals(eventDTO.getEndDate(), eventEntity2.getEndDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		EventEntity eventEntity = eventRepository.findAll().get(0);
		EventDTO eventDTO = new EventDTO(eventEntity.getId(), "Event", "New event", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(8888888L));

		// act
		adminEventService.delete(eventDTO.getId());

		// assert
		Assertions.assertEquals(0, eventRepository.count());
	}

}