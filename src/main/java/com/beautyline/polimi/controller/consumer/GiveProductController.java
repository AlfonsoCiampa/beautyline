package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.dto.GiveProductDTO;
import com.beautyline.polimi.service.consumer.GiveProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/consumer/giveProduct")
public class GiveProductController {

	private final GiveProductService giveProductService;

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public GiveProductDTO create(@RequestBody GiveProductDTO giveProductDTO) {
		return giveProductService.create(giveProductDTO);
	}

}
