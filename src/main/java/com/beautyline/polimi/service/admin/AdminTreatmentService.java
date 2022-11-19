package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.entity.TreatmentEntity;
import com.beautyline.polimi.mapper.TreatmentMapper;
import com.beautyline.polimi.repository.TreatmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminTreatmentService {

	private final TreatmentRepository treatmentRepository;

	public TreatmentDTO getTreatment(Long id) {
		return TreatmentMapper.entityToDto(treatmentRepository.findById(id).orElse(null));
	}

	public Page<TreatmentDTO> getTreatments(Integer page, Integer size) {
		return TreatmentMapper.entitiesToDtoPage(treatmentRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public TreatmentDTO create(TreatmentDTO treatmentDTO) {

		treatmentDTOControls(treatmentDTO, true);

		TreatmentEntity treatmentEntity = TreatmentMapper.dtoToEntity(treatmentDTO);
		treatmentRepository.save(treatmentEntity);
		return treatmentDTO;
	}

	public TreatmentDTO update(TreatmentDTO treatmentDTO) {

		treatmentDTOControls(treatmentDTO, false);

		TreatmentEntity treatmentEntity = TreatmentMapper.dtoToEntity(treatmentDTO);
		treatmentRepository.save(treatmentEntity);
		return treatmentDTO;

	}

	public void delete(Long id) {
		if (id == null || treatmentRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		treatmentRepository.deleteById(id);
	}

	public void treatmentDTOControls(TreatmentDTO treatmentDTO, boolean create) {

		// id validation
		if (!create && (treatmentDTO.getId() == null || treatmentRepository.findById(treatmentDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// name validation
		if (treatmentDTO.getName() == null || treatmentDTO.getName().equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		// description validation
		if (treatmentDTO.getDescription() == null || treatmentDTO.getDescription().equals("")) {
			throw new IllegalArgumentException("Invalid description");
		}

		// obscure validation
		if (treatmentDTO.getObscure() == null) {
			throw new IllegalArgumentException("Invalid obscure");
		}

		// price validation
		if (treatmentDTO.getPrice() == null || treatmentDTO.getPrice().compareTo(BigDecimal.valueOf(0)) < 0) {
			throw new IllegalArgumentException("Invalid price");
		}

	}
}
