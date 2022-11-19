package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.service.admin.AdminEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/event")
public class AdminEventController {

	private final AdminEventService adminEventService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EventDTO getEvent(@PathVariable("id") Long id) {
		return adminEventService.getEvent(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<EventDTO> getEvents(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminEventService.getEvents(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EventDTO create(@RequestBody EventDTO createEventDTO) {
		return adminEventService.create(createEventDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public EventDTO update(@RequestBody EventDTO updateEventDTO) {
		return adminEventService.update(updateEventDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminEventService.delete(id);
	}

}
