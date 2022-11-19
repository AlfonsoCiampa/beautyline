package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.AdminDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.ConsumerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private ConsumerRepository consumerRepository;
	@Autowired
	private AdminService adminService;

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
	public void getAdminDone() {
		// arrange
		AccountEntity accountEntity = accountRepository.findAll().get(0);

		// act
		AdminDTO result = adminService.getAdminData(accountEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(accountEntity.getId(), result.getId());
		Assertions.assertEquals(accountEntity.getEmail(), result.getEmail());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getAdminsDone() {
		// arrange
		AccountEntity accountEntity = accountRepository.findAll().get(0);

		// act
		Page<AdminDTO> result = adminService.getAdminsData(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		AdminDTO admin = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(admin);
		Assertions.assertEquals(accountEntity.getId(), admin.getId());
		Assertions.assertEquals(accountEntity.getEmail(), admin.getEmail());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		RegistrationDTO adminDTO = new RegistrationDTO(null, "admin@test.it", "password", "+393736291243", "Giacomo", "Dongiovanni");

		// act
		adminService.create(adminDTO);

		// assert
		Assertions.assertEquals(0, consumerRepository.count());
		Assertions.assertEquals(2, accountRepository.count());
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.ADMIN, "admin@test.it").get();
		Assertions.assertEquals(adminDTO.getEmail(), accountEntity.getEmail());
		Assertions.assertEquals(adminDTO.getPassword(), accountEntity.getPassword());
		Assertions.assertNull(accountEntity.getReferenceId());
		Assertions.assertEquals(AccountEntity.Type.ADMIN, accountEntity.getReferenceType());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.ADMIN, "admin@test.it").get();
		RegistrationDTO adminDTO = new RegistrationDTO(accountEntity.getId(), "admin2@test.it", "Password2", "+383736291243", "Giancarlo", "Bongiovanni");

		// act
		adminService.update(adminDTO);

		// assert
		Assertions.assertEquals(0, consumerRepository.count());
		Assertions.assertEquals(2, accountRepository.count());
		AccountEntity accountEntity2 = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.ADMIN, "admin2@test.it").get();
		Assertions.assertEquals(adminDTO.getEmail(), accountEntity2.getEmail());
		Assertions.assertEquals(adminDTO.getPassword(), accountEntity2.getPassword());
		Assertions.assertNull(accountEntity2.getReferenceId());
		Assertions.assertEquals(AccountEntity.Type.ADMIN, accountEntity2.getReferenceType());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		AccountEntity accountEntity = accountRepository.findByReferenceTypeAndEmail(AccountEntity.Type.ADMIN, "admin@test.it").get();

		// act
		adminService.delete(accountEntity.getId());

		// assert
		Assertions.assertEquals(1, accountRepository.count());
	}

}
