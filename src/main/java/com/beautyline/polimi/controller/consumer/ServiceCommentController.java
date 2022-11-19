package com.beautyline.polimi.controller.consumer;

import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.service.consumer.ServiceCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/consumer/serviceComment")
public class ServiceCommentController {

	private final ServiceCommentService serviceCommentService;

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ServiceCommentDTO> getComments(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return serviceCommentService.getComments(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO create(@RequestBody ServiceCommentDTO createServiceCommentDTO) {
		return serviceCommentService.create(createServiceCommentDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO update(@RequestBody ServiceCommentDTO updateServiceCommentDTO) {
		return serviceCommentService.update(updateServiceCommentDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		serviceCommentService.delete(id);
	}

}
