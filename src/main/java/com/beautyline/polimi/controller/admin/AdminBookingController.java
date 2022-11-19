package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.service.admin.AdminBookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/booking")
public class AdminBookingController {

	private final AdminBookingService adminBookingService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingDTO getBooking(@PathVariable("id") Long id) {
		return adminBookingService.getBooking(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<BookingDTO> getBookings(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminBookingService.getBookings(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingDTO create(@RequestBody BookingDTO createBookingDTO) {
		return adminBookingService.create(createBookingDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingDTO update(@RequestBody BookingDTO updateBookingDTO) {
		return adminBookingService.update(updateBookingDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminBookingService.delete(id);
	}
}
