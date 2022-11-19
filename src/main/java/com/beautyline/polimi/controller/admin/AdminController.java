package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.AdminDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.service.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/admin")
public class AdminController {

	private final AdminService adminControllerService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public AdminDTO getAdmin(@PathVariable("id") Long id) {
		return adminControllerService.getAdminData(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<AdminDTO> getAdmins(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminControllerService.getAdminsData(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegistrationDTO create(@RequestBody RegistrationDTO createRegistrationDTO) {
		return adminControllerService.create(createRegistrationDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public RegistrationDTO update(@RequestBody RegistrationDTO updateRegistrationDTO) {
		return adminControllerService.update(updateRegistrationDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminControllerService.delete(id);
	}

}
