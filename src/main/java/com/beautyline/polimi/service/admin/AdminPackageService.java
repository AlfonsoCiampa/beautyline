package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.entity.PackageEntity;
import com.beautyline.polimi.mapper.PackageMapper;
import com.beautyline.polimi.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminPackageService {

	private final PackageRepository packageRepository;

	public PackageDTO getPackage(Long id) {
		return PackageMapper.entityToDto(packageRepository.findById(id).orElse(null));
	}

	public Page<PackageDTO> getPackages(Integer page, Integer size) {
		return PackageMapper.entitiesToDtoPage(packageRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public PackageDTO create(PackageDTO packageDTO) {

		packageDTOControls(packageDTO, true);

		// startDate validation
		if (packageDTO.getStartDate() == null || packageDTO.getStartDate().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid start date");
		}

		PackageEntity packageEntity = PackageMapper.dtoToEntity(packageDTO);
		packageRepository.save(packageEntity);
		return packageDTO;

	}

	public PackageDTO update(PackageDTO packageDTO) {

		packageDTOControls(packageDTO, false);

		PackageEntity packageEntity = PackageMapper.dtoToEntity(packageDTO);
		packageRepository.save(packageEntity);
		return packageDTO;

	}

	public void delete(Long id) {
		if (id == null || packageRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		packageRepository.deleteById(id);
	}

	public void packageDTOControls(PackageDTO packageDTO, boolean create) {

		// id validation
		if (!create && (packageDTO.getId() == null || packageRepository.findById(packageDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// name validation
		if (packageDTO.getName() == null || packageDTO.getName().equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		// price validation
		if (packageDTO.getPrice() == null || packageDTO.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException("Invalid price");
		}

		// description validation
		if (packageDTO.getDescription() == null || packageDTO.getDescription().equals("")) {
			throw new IllegalArgumentException("Invalid description");
		}

		// endDate validation
		if (packageDTO.getEndDate() == null || packageDTO.getEndDate().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid end date");
		}

		// code validation
		if (packageDTO.getCode() == null || packageDTO.getCode().equals("")
			|| (
			create
				? packageRepository.findByCode(packageDTO.getCode()).isPresent()
				: packageRepository.findByCode(packageDTO.getCode())
				.map(p -> !packageDTO.getId().equals(p.getId())).orElse(false)
		)
		) {
			throw new IllegalArgumentException("Invalid code");
		}

	}

}
