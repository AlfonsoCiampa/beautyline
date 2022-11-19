package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.BookingConfigurationDTO;
import com.beautyline.polimi.service.admin.AdminBookingConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/booking/configuration")
public class AdminBookingConfigurationController {

	private final AdminBookingConfigurationService adminBookingConfigurationService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingConfigurationDTO getBookingConfiguration(@PathVariable("id") Long id) {
		return adminBookingConfigurationService.getBookingConfiguration(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<BookingConfigurationDTO> getBookingConfigurations(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminBookingConfigurationService.getBookingConfigurations(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingConfigurationDTO create(@RequestBody BookingConfigurationDTO createBookingConfigurationDTO) {
		return adminBookingConfigurationService.create(createBookingConfigurationDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingConfigurationDTO update(@RequestBody BookingConfigurationDTO updateBookingConfigurationDTO) {
		return adminBookingConfigurationService.update(updateBookingConfigurationDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminBookingConfigurationService.delete(id);
	}

}
