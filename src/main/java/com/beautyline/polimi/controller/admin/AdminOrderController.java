package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.OrderDTO;
import com.beautyline.polimi.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/order")
public class AdminOrderController {

	private final AdminOrderService adminOrderService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public OrderDTO getOrder(@PathVariable("id") Long id) {
		return adminOrderService.getOrder(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<OrderDTO> getOrders(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminOrderService.getOrders(page, size);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminOrderService.delete(id);
	}

}
