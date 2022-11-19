package com.beautyline.polimi.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Mapper {

	@Bean
	public ObjectMapper mapperJson() {
		ObjectMapper mapperJson = new ObjectMapper();
		mapperJson.registerModule(new JavaTimeModule());
		return mapperJson;
	}
}
