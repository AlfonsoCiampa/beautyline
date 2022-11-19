package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.ConsumerDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.service.admin.AdminConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/consumer")
public class AdminConsumerController {

	private final AdminConsumerService adminConsumerService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ConsumerDTO getConsumer(@PathVariable("id") Long id) {
		return adminConsumerService.getConsumer(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ConsumerDTO> getConsumers(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminConsumerService.getConsumers(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegistrationDTO create(@RequestBody RegistrationDTO createRegistrationDTO) {
		return adminConsumerService.create(createRegistrationDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegistrationDTO update(@RequestBody RegistrationDTO uodateRegistrationDTO) {
		return adminConsumerService.update(uodateRegistrationDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminConsumerService.delete(id);
	}
}
