package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.GiftDTO;
import com.beautyline.polimi.service.admin.AdminGiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/serviceOwned")
public class AdminGiftController {

	private final AdminGiftService adminGiftService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public GiftDTO getGift(@PathVariable("id") Long id) {
		return adminGiftService.getGift(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<GiftDTO> getGifts(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminGiftService.getGifts(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GiftDTO create(@RequestBody GiftDTO createGiftDTO) {
		return adminGiftService.create(createGiftDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GiftDTO update(@RequestBody GiftDTO updateGiftDTO) {
		return adminGiftService.update(updateGiftDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminGiftService.delete(id);
	}

}
