package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.EventDTO;
import com.beautyline.polimi.dto.PackageDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@MockBean
	private OfferController offerController;

	@Test
	public void getEvent() {
		// arrange

		// act
		ResponseEntity<EventDTO> response = restTemplate.exchange("/generic/offer/event/111", HttpMethod.GET, HttpEntity.EMPTY, EventDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(offerController, Mockito.times(1)).getEvent(111L);
	}

	@Test
	public void getEvents() {
		// arrange
		ParameterizedTypeReference<Page<EventDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<EventDTO>> response = restTemplate.exchange("/generic/offer/event/list", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(offerController, Mockito.times(1)).getEvents(null, null);
	}

	@Test
	public void getPackage() {
		// arrange

		// act
		ResponseEntity<PackageDTO> response = restTemplate.exchange("/generic/offer/package/111", HttpMethod.GET, HttpEntity.EMPTY, PackageDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(offerController, Mockito.times(1)).getPackage(111L);
	}

	@Test
	public void getPackages() {
		// arrange
		ParameterizedTypeReference<Page<PackageDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<PackageDTO>> response = restTemplate.exchange("/generic/offer/package/list", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(offerController, Mockito.times(1)).getPackages(null, null);
	}
}
