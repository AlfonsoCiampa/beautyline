package com.beautyline.polimi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;

import java.util.TimeZone;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
public class BeautyLineApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Rome"));
		SpringApplication.run(BeautyLineApplication.class, args);
	}

}
