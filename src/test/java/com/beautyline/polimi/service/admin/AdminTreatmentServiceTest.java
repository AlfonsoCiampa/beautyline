package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.TreatmentEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.TreatmentRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminTreatmentServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private TreatmentRepository treatmentRepository;
	@Autowired
	private AdminTreatmentService adminTreatmentService;

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
	public void getTreatmentDone() {
		// arrange
		createDone();
		TreatmentEntity treatmentEntity = treatmentRepository.findAll().get(0);

		// act
		TreatmentDTO result = adminTreatmentService.getTreatment(treatmentEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(treatmentEntity.getId(), result.getId());
		Assertions.assertEquals(treatmentEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getTreatmentsDone() {
		// arrange
		createDone();
		TreatmentEntity treatmentEntity = treatmentRepository.findAll().get(0);

		// act
		Page<TreatmentDTO> result = adminTreatmentService.getTreatments(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		TreatmentDTO treatment = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(treatment);
		Assertions.assertEquals(treatmentEntity.getId(), treatment.getId());
		Assertions.assertEquals(treatmentEntity.getName(), treatment.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		TreatmentDTO treatmentDTO = new TreatmentDTO(null, "Treatment1", "Is perfect for you", BigDecimal.TEN, false, 1800L);

		// act
		adminTreatmentService.create(treatmentDTO);

		// assert
		Assertions.assertEquals(1, treatmentRepository.count());
		TreatmentEntity treatmentEntity = treatmentRepository.findAll().get(0);
		Assertions.assertEquals(treatmentDTO.getName(), treatmentEntity.getName());
		Assertions.assertEquals(treatmentDTO.getDescription(), treatmentEntity.getDescription());
		Assertions.assertEquals(treatmentDTO.getPrice(), treatmentEntity.getPrice());
		Assertions.assertEquals(treatmentDTO.getObscure(), treatmentEntity.getObscure());
		Assertions.assertEquals(treatmentDTO.getDuration(), treatmentEntity.getDuration());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		TreatmentEntity treatmentEntity = treatmentRepository.findAll().get(0);
		TreatmentDTO treatmentDTO = new TreatmentDTO(treatmentEntity.getId(), "Treatment2", "Is perfect for me", BigDecimal.TEN, false, 1200L);

		// act
		adminTreatmentService.update(treatmentDTO);

		// assert
		Assertions.assertEquals(1, treatmentRepository.count());
		TreatmentEntity treatmentEntity2 = treatmentRepository.findAll().get(0);
		Assertions.assertEquals(treatmentDTO.getId(), treatmentEntity2.getId());
		Assertions.assertEquals(treatmentDTO.getName(), treatmentEntity2.getName());
		Assertions.assertEquals(treatmentDTO.getDescription(), treatmentEntity2.getDescription());
		Assertions.assertEquals(treatmentDTO.getPrice(), treatmentEntity2.getPrice());
		Assertions.assertEquals(treatmentDTO.getObscure(), treatmentEntity2.getObscure());
		Assertions.assertEquals(treatmentDTO.getDuration(), treatmentEntity2.getDuration());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		TreatmentEntity treatmentEntity = treatmentRepository.findAll().get(0);
		TreatmentDTO treatmentDTO = new TreatmentDTO(treatmentEntity.getId(), "Treatment1", "Is perfect for you", BigDecimal.TEN, false, 1800L);

		// act
		adminTreatmentService.delete(treatmentDTO.getId());

		// assert
		Assertions.assertEquals(0, treatmentRepository.count());
	}

}