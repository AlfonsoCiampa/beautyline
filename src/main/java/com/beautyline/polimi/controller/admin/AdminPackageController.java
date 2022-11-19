package com.beautyline.polimi.controller.admin;

import com.beautyline.polimi.dto.PackageDTO;
import com.beautyline.polimi.service.admin.AdminPackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/admin/package")
public class AdminPackageController {

	private final AdminPackageService adminPackageService;

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PackageDTO getPackage(@PathVariable("id") Long id) {
		return adminPackageService.getPackage(id);
	}

	@GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
	public Page<PackageDTO> getPackages(@RequestParam(value = "page", required = false) Integer page,
		@RequestParam(value = "size", required = false) Integer size) {
		return adminPackageService.getPackages(page, size);
	}

	@PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PackageDTO create(@RequestBody PackageDTO createPackageDTO) {
		return adminPackageService.create(createPackageDTO);
	}

	@PostMapping(value = "/update", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public PackageDTO update(@RequestBody PackageDTO updatePackageDTO) {
		return adminPackageService.update(updatePackageDTO);
	}

	@DeleteMapping(value = "/{id}")
	public void delete(@PathVariable("id") Long id) {
		adminPackageService.delete(id);
	}

}
