package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.service.admin.AdminProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/product")
public class AdminProductController {

	private final AdminProductService adminProductService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductDTO getProduct(@PathVariable("id") Long id) {
		return adminProductService.getProduct(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ProductDTO> getProducts(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminProductService.getProducts(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductDTO create(@RequestBody ProductDTO createProductDTO) {
		return adminProductService.create(createProductDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ProductDTO update(@RequestBody ProductDTO updateProductDTO) {
		return adminProductService.update(updateProductDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminProductService.delete(id);
	}

}
