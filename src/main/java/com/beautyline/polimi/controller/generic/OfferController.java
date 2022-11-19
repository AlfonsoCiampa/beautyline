package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.service.generic.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/generic/offer")
public class OfferController {

	private final OfferService offerService;

	@GetMapping(value = "/event/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public EventDTO getEvent(@PathVariable("id") Long id) {
		return offerService.getEvent(id);
	}

	@GetMapping(value = "/event/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<EventDTO> getEvents(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return offerService.getEvents(page, size);
	}

	@GetMapping(value = "/package/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PackageDTO getPackage(@PathVariable("id") Long id) {
		return offerService.getPackage(id);
	}

	@GetMapping(value = "/package/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<PackageDTO> getPackages(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return offerService.getPackages(page, size);
	}
}
