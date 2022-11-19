package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.service.consumer.GiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/consumer/gift")
public class GiftController {

	private final GiftService giftService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GiftDTO getGift(@PathVariable("id") Long id) {
		return giftService.getGift(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<GiftDTO> getGifts(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return giftService.getGifts(page, size);
	}

}
