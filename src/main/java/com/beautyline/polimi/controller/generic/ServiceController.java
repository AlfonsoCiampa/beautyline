package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.service.generic.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/generic/service")
public class ServiceController {

	private final ServiceService serviceService;

	@GetMapping(value = "/treatment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TreatmentDTO getTreatment(@PathVariable("id") Long id) {
		return serviceService.getTreatment(id);
	}

	@GetMapping(value = "/treatment/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<TreatmentDTO> getTreatments(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return serviceService.getTreatments(page, size);
	}

	@GetMapping(value = "/treatment/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ServiceCommentDTO> getTreatmentComments(@PathVariable("id") Long id,
		@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return serviceService.getTreatmentComments(id, page, size);
	}

	@GetMapping(value = "/product/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductDTO getProduct(@PathVariable("id") Long id) {
		return serviceService.getProduct(id);
	}

	@GetMapping(value = "/product/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ProductDTO> getProducts(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return serviceService.getProducts(page, size);
	}

	@GetMapping(value = "/product/{id}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ServiceCommentDTO> getProductComments(@PathVariable("id") Long id,
		@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return serviceService.getProductComments(id, page, size);
	}

	@GetMapping(value = "/comment/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO getComment(@PathVariable("id") Long id) {
		return serviceService.getComment(id);
	}

}
