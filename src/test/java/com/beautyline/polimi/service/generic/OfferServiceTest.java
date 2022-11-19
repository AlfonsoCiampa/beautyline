package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.entity.EventEntity;
import com.beautyline.polimi.entity.PackageEntity;
import com.beautyline.polimi.entity.PackageItemEntity;
import com.beautyline.polimi.entity.ProductEntity;
import com.beautyline.polimi.repository.EventRepository;
import com.beautyline.polimi.repository.PackageItemRepository;
import com.beautyline.polimi.repository.PackageRepository;
import com.beautyline.polimi.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferServiceTest {

	@Autowired
	private OfferService offerService;
	@Autowired
	private EventRepository eventRepository;
	@Autowired
	private PackageRepository packageRepository;
	@Autowired
	private PackageItemRepository packageItemRepository;
	@Autowired
	private ProductRepository productRepository;

	@Test
	public void getEventDone() {
		// arrange
		EventEntity eventEntity = eventRepository.save(EventEntity.builder()
			.name("Event")
			.description("New event")
			.startDate(LocalDateTime.now().minusSeconds(60L))
			.endDate(LocalDateTime.now().plusSeconds(8888888L))
			.build()
		);

		// act
		EventDTO result = offerService.getEvent(eventEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(eventEntity.getId(), result.getId());
		Assertions.assertEquals(eventEntity.getName(), result.getName());
	}

	@Test
	public void getEventsDone() {
		// arrange
		EventEntity eventEntity = eventRepository.save(EventEntity.builder()
			.name("Event")
			.description("New event")
			.startDate(LocalDateTime.now().minusSeconds(60L))
			.endDate(LocalDateTime.now().plusSeconds(8888888L))
			.build()
		);

		// act
		Page<EventDTO> result = offerService.getEvents(null, null);

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
	public void getPackageDone() {
		// arrange
		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());

		PackageEntity packageEntity = packageRepository.save(PackageEntity.builder()
			.name("Package")
			.description("New package")
			.code("PK1")
			.price(BigDecimal.TEN)
			.startDate(LocalDateTime.now().minusSeconds(60L))
			.endDate(LocalDateTime.now().plusSeconds(8888888L))
			.build()
		);

		PackageItemEntity packageItemEntity = packageItemRepository.save(PackageItemEntity.builder()
			.packageId(packageEntity.getId())
			.productId(productEntity.getId())
			.build()
		);

		packageEntity.setPackageItems(new ArrayList<>(List.of(packageItemEntity)));
		packageEntity = packageRepository.save(packageEntity);

		// act
		PackageDTO result = offerService.getPackage(packageEntity.getId());

		// assert
		Assertions.assertNotNull(result);
		Assertions.assertEquals(packageEntity.getId(), result.getId());
		Assertions.assertEquals(packageEntity.getName(), result.getName());
		Assertions.assertEquals(1, result.getPackageItems().size());
	}

	@Test
	public void getPackagesDone() {
		// arrange
		ProductEntity productEntity = productRepository.save(ProductEntity.builder()
			.name("Cream")
			.description("Cream")
			.price(BigDecimal.TEN)
			.quantity(100L)
			.obscure(false)
			.build());

		PackageEntity packageEntity = packageRepository.save(PackageEntity.builder()
			.name("Package")
			.description("New package")
			.code("PK1")
			.price(BigDecimal.TEN)
			.startDate(LocalDateTime.now().minusSeconds(60L))
			.endDate(LocalDateTime.now().plusSeconds(8888888L))
			.build()
		);

		PackageItemEntity packageItemEntity = packageItemRepository.save(PackageItemEntity.builder()
			.packageId(packageEntity.getId())
			.productId(productEntity.getId())
			.build()
		);

		packageEntity.setPackageItems(new ArrayList<>(List.of(packageItemEntity)));
		packageEntity = packageRepository.save(packageEntity);

		// act
		Page<PackageDTO> result = offerService.getPackages(null, null);

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
}
