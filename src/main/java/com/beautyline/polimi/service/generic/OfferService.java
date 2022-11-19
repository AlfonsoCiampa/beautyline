package com.beautyline.polimi.service.generic;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.mapper.EventMapper;
import com.beautyline.polimi.mapper.PackageMapper;
import com.beautyline.polimi.repository.EventRepository;
import com.beautyline.polimi.repository.PackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OfferService {

	private final EventRepository eventRepository;
	private final PackageRepository packageRepository;

	public EventDTO getEvent(Long id) {
		return EventMapper.entityToDto(eventRepository.findById(id).orElse(null));
	}

	public Page<EventDTO> getEvents(Integer page, Integer size) {
		return EventMapper.entitiesToDtoPage(eventRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public PackageDTO getPackage(Long id) {
		return PackageMapper.entityToDto(packageRepository.findById(id).orElse(null));
	}

	public Page<PackageDTO> getPackages(Integer page, Integer size) {
		return PackageMapper.entitiesToDtoPage(packageRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

}