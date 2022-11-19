package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.dto.BookingDTO;
import com.beautyline.polimi.service.consumer.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/consumer/booking")
public class BookingController {

	private final BookingService bookingService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingDTO getBooking(@PathVariable("id") Long id) {
		return bookingService.getBooking(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<BookingDTO> getBookings(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return bookingService.getBookings(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public BookingDTO create(@RequestBody BookingDTO createBookingDTO) {
		return bookingService.create(createBookingDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		bookingService.delete(id);
	}

}
