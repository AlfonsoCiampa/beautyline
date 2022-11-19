package com.beautyline.polimi.service.admin;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.entity.EventEntity;
import com.beautyline.polimi.mapper.EventMapper;
import com.beautyline.polimi.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminEventService {

	private final EventRepository eventRepository;

	public EventDTO getEvent(Long id) {
		return EventMapper.entityToDto(eventRepository.findById(id).orElse(null));
	}

	public Page<EventDTO> getEvents(Integer page, Integer size) {
		return EventMapper.entitiesToDtoPage(eventRepository.findAll(
			PageRequest.of(Math.max(0, Optional.ofNullable(page).orElse(0)), Math.max(20, Optional.ofNullable(size).orElse(20)))
		));
	}

	public EventDTO create(EventDTO eventDTO) {

		eventDTOControls(eventDTO, true);

		// startDate validation
		if (eventDTO.getStartDate() == null || eventDTO.getStartDate().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid end date");
		}

		EventEntity eventEntity = EventMapper.dtoToEntity(eventDTO);
		eventRepository.save(eventEntity);
		return eventDTO;
	}

	public EventDTO update(EventDTO eventDTO) {

		eventDTOControls(eventDTO, false);

		EventEntity eventEntity = EventMapper.dtoToEntity(eventDTO);
		eventRepository.save(eventEntity);
		return eventDTO;

	}

	public void delete(Long id) {
		if (id == null || eventRepository.findById(id).isEmpty()) {
			throw new IllegalArgumentException("Invalid ID");
		}

		eventRepository.deleteById(id);
	}

	public void eventDTOControls(EventDTO eventDTO, Boolean create) {

		// id validation
		if (!create && (eventDTO.getId() == null || eventRepository.findById(eventDTO.getId()).isEmpty())) {
			throw new IllegalArgumentException("Invalid ID");
		}

		// name validation
		if (eventDTO.getName() == null || eventDTO.getName().equals("")) {
			throw new IllegalArgumentException("Invalid name");
		}

		// description validation
		if (eventDTO.getDescription() == null || eventDTO.getDescription().equals("")) {
			throw new IllegalArgumentException("Invalid description");
		}

		// endDate validation
		if (eventDTO.getEndDate() == null || eventDTO.getEndDate().isBefore(LocalDateTime.now())) {
			throw new IllegalArgumentException("Invalid start date");
		}

	}
}
