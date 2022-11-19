package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.TreatmentDTO;
import com.beautyline.polimi.service.admin.AdminTreatmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/treatment")
public class AdminTreatmentController {

	private final AdminTreatmentService adminTreatmentService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public TreatmentDTO getTreatment(@PathVariable("id") Long id) {
		return adminTreatmentService.getTreatment(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<TreatmentDTO> getTreatments(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminTreatmentService.getTreatments(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TreatmentDTO create(@RequestBody TreatmentDTO createTreatmentDTO) {
		return adminTreatmentService.create(createTreatmentDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public TreatmentDTO update(@RequestBody TreatmentDTO updateTreatmentDTO) {
		return adminTreatmentService.update(updateTreatmentDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminTreatmentService.delete(id);
	}

}
