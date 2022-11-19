package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.service.admin.AdminServiceCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/serviceComment")
public class AdminServiceCommentController {

	private final AdminServiceCommentService adminServiceCommentService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO getComment(@PathVariable("id") Long id) {
		return adminServiceCommentService.getComment(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<ServiceCommentDTO> getComments(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminServiceCommentService.getComments(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO create(@RequestBody ServiceCommentDTO createServiceCommentDTO) {
		return adminServiceCommentService.create(createServiceCommentDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ServiceCommentDTO update(@RequestBody ServiceCommentDTO updateServiceCommentDTO) {
		return adminServiceCommentService.update(updateServiceCommentDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminServiceCommentService.delete(id);
	}

}
