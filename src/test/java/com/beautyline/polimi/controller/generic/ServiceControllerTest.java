package com.beautyline.polimi.controller.generic;

import com.beautyline.polimi.dto.ProductDTO;
import com.beautyline.polimi.dto.ServiceCommentDTO;
import com.beautyline.polimi.dto.TreatmentDTO;
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
public class ServiceControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;
	@MockBean
	private ServiceController serviceController;

	@Test
	public void getTreatmentDone() {
		// arrange

		// act
		ResponseEntity<TreatmentDTO> response = restTemplate.exchange("/generic/service/treatment/111", HttpMethod.GET, HttpEntity.EMPTY, TreatmentDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getTreatment(111L);
	}

	@Test
	public void getTreatmentsDone() {
		// arrange
		ParameterizedTypeReference<Page<TreatmentDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<TreatmentDTO>> response = restTemplate.exchange("/generic/service/treatment/list", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getTreatments(null, null);
	}

	@Test
	public void getTreatmentCommentsDone() {
		// arrange
		ParameterizedTypeReference<Page<ServiceCommentDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<ServiceCommentDTO>> response = restTemplate.exchange("/generic/service/treatment/111/comment", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getTreatmentComments(111L, null, null);
	}

	@Test
	public void getProductDone() {
		// arrange

		// act
		ResponseEntity<ProductDTO> response = restTemplate.exchange("/generic/service/product/111", HttpMethod.GET, HttpEntity.EMPTY, ProductDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getProduct(111L);
	}

	@Test
	public void getProductsDone() {
		// arrange
		ParameterizedTypeReference<Page<ProductDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<ProductDTO>> response = restTemplate.exchange("/generic/service/product/list", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getProducts(null, null);
	}

	@Test
	public void getProductCommentsDone() {
		// arrange
		ParameterizedTypeReference<Page<ServiceCommentDTO>> type = new ParameterizedTypeReference<>() {
		};

		// act
		ResponseEntity<Page<ServiceCommentDTO>> response = restTemplate.exchange("/generic/service/product/111/comment", HttpMethod.GET, HttpEntity.EMPTY, type);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getProductComments(111L, null, null);
	}

	@Test
	public void getCommentDone() {
		// arrange

		// act
		ResponseEntity<ServiceCommentDTO> response = restTemplate.exchange("/generic/service/comment/111", HttpMethod.GET, HttpEntity.EMPTY, ServiceCommentDTO.class);

		// assert
		Assertions.assertEquals(200, response.getStatusCodeValue());
		Mockito.verify(serviceController, Mockito.times(1)).getComment(111L);
	}
}
