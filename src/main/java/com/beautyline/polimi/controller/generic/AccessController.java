package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.LoginDTO;
import com.beautyline.polimi.dto.RegistrationDTO;
import com.beautyline.polimi.service.generic.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping(path = "/generic/access")
public class AccessController {

	private final AccessService accessService;

	@PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String register(@RequestBody RegistrationDTO registrationDTO) {
		return accessService.register(registrationDTO);
	}

	@PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
	public String login(@RequestBody LoginDTO loginDTO) {
		return accessService.login(loginDTO);
	}

	@PostMapping(value = "/forgotPassword")
	public void forgotPassword(@RequestParam(value = "email") String email) {
		accessService.forgotPassword(email);
	}

}
