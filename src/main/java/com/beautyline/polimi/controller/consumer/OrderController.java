package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.dto.PlaceOrderDTO;
import com.beautyline.polimi.service.consumer.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/consumer/order")
public class OrderController {

	private final OrderService orderService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderDTO getOrder(@PathVariable("id") Long id) {
		return orderService.getOrder(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<OrderDTO> getOrders(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return orderService.getOrders(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderDTO place(@RequestBody PlaceOrderDTO placeOrderDTO) {
		return orderService.place(placeOrderDTO);
	}
}
