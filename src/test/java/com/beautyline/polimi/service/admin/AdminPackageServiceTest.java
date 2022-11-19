package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.dto.PackageItemDTO;
import com.beautyline.polimi.entity.AccountEntity;
import com.beautyline.polimi.entity.PackageEntity;
import com.beautyline.polimi.entity.PackageItemEntity;
import com.beautyline.polimi.repository.AccountRepository;
import com.beautyline.polimi.repository.PackageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminPackageServiceTest {

	@Autowired
	private AccountRepository accountRepository;
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private AdminPackageService adminPackageService;

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
	public void getPackageDone() {
		// arrange
		createDone();
		PackageEntity packageEntity = packageRepository.findAll().get(0);

		// act
		PackageDTO result = adminPackageService.getPackage(packageEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(packageEntity.getId(), result.getId());
		Assertions.assertEquals(packageEntity.getName(), result.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void getPackagesDone() {
		// arrange
		createDone();
		PackageEntity packageEntity = packageRepository.findAll().get(0);

		// act
		Page<PackageDTO> result = adminPackageService.getPackages(null, null);

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(1, result.getTotalElements());
		Assertions.assertEquals(1, result.getTotalPages());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(20, result.getSize());
		PackageDTO packageDTO = result.get().findFirst().orElse(null);
		Assertions.assertNotNull(packageDTO);
		Assertions.assertEquals(packageEntity.getId(), packageDTO.getId());
		Assertions.assertEquals(packageEntity.getName(), packageDTO.getName());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void createDone() {
		// arrange
		List<PackageItemDTO> dtoList = new ArrayList<>();
		PackageDTO packageDTO = new PackageDTO(null, dtoList, BigDecimal.TEN, "Package1", "doc", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(3000000L), "PK1");

		// act
		adminPackageService.create(packageDTO);

		// assert
		Assertions.assertEquals(1, packageRepository.count());
		PackageEntity packageEntity = packageRepository.findAll().get(0);
		Assertions.assertEquals(packageDTO.getPackageItems(), packageEntity.getPackageItems());
		Assertions.assertEquals(packageDTO.getPrice(), packageEntity.getPrice());
		Assertions.assertEquals(packageDTO.getName(), packageEntity.getName());
		Assertions.assertEquals(packageDTO.getDescription(), packageEntity.getDescription());
		Assertions.assertEquals(packageDTO.getStartDate(), packageEntity.getStartDate());
		Assertions.assertEquals(packageDTO.getEndDate(), packageEntity.getEndDate());
	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void updateDone() {
		// arrange
		createDone();
		PackageEntity packageEntity = packageRepository.findAll().get(0);
		List<PackageItemDTO> dtoList = new ArrayList<>();
		PackageDTO packageDTO = new PackageDTO(packageEntity.getId(), dtoList, BigDecimal.TEN, "Package2", "doc", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(99999999L), "PK1");

		// act
		adminPackageService.update(packageDTO);

		// assert
		Assertions.assertEquals(1, packageRepository.count());
		PackageEntity packageEntity2 = packageRepository.findAll().get(0);
		Assertions.assertEquals(packageDTO.getId(), packageEntity2.getId());
		Assertions.assertEquals(packageDTO.getPackageItems().size(), packageEntity2.getPackageItems().size());
		Assertions.assertEquals(packageDTO.getPrice(), packageEntity2.getPrice());
		Assertions.assertEquals(packageDTO.getName(), packageEntity2.getName());
		Assertions.assertEquals(packageDTO.getDescription(), packageEntity2.getDescription());
		Assertions.assertEquals(packageDTO.getStartDate(), packageEntity2.getStartDate());
		Assertions.assertEquals(packageDTO.getEndDate(), packageEntity2.getEndDate());
		for (PackageItemDTO packageItemDTO : packageDTO.getPackageItems()) {
			PackageItemEntity packageItemEntity = packageEntity2.getPackageItems().stream()
				.filter(pi -> pi.getId().equals(packageItemDTO.getId())).findFirst().get();

			Assertions.assertEquals(packageItemDTO.getId(), packageItemEntity.getId());
			Assertions.assertEquals(packageItemDTO.getPackageId(), packageItemEntity.getPackageId());
			Assertions.assertEquals(packageItemDTO.getProductId(), packageItemEntity.getProductId());
		}

	}

	@Test
	@WithMockUser(username = "account@mail.it", password = "TestPassword123", authorities = {"ADMIN"})
	public void deleteDone() {
		// arrange
		createDone();
		PackageEntity packageEntity = packageRepository.findAll().get(0);
		List<PackageItemDTO> dtoList = new ArrayList<>();
		PackageDTO packageDTO = new PackageDTO(packageEntity.getId(), dtoList, BigDecimal.TEN, "Package1", "doc", LocalDateTime.now().plusSeconds(60L), LocalDateTime.now().plusSeconds(3000000L), "PK1");

		// act
		adminPackageService.delete(packageDTO.getId());

		// assert
		Assertions.assertEquals(0, packageRepository.count());
	}


}