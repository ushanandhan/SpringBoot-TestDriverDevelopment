package com.ushan.dev;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ushan.dev.model.Car;
import com.ushan.dev.service.CarService;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	CarService carService;

	HttpHeaders headers = new HttpHeaders();
	
	@Test
	public void getCarDetails() throws Exception {
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Car> response = restTemplate.exchange(
		          "http://localhost:"+port+"/cars/duster", HttpMethod.GET, entity, Car.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(response.getBody().getType()).isEqualTo("hybrid");
		
	}
	
	@Test
	public void car_Not_found_Http_Status() throws Exception{
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Car> response = restTemplate.exchange(
		          "http://localhost:"+port+"/cars/scala", HttpMethod.GET, entity, Car.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	public void getAllCars() throws Exception{
		ResponseEntity<List<Car>> response = restTemplate.exchange(
				"http://localhost:"+port+"/cars/",
				HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Car>>() {});
		List<Car> carList = response.getBody();
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(carList.size()).isEqualTo(4);
	}
	
	@Test
	public void saveCar() throws Exception{
		Car car = new Car("Scala","Sadan");
		ResponseEntity<Car> response = restTemplate.postForEntity("http://localhost:"+port+"/cars/", 
				car, Car.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertNotNull(response.getBody().getId());
	}
	
	@Test
	public void updateCar() throws Exception{
		Car saveCar = new Car("Captur","SUV");
		ResponseEntity<Car> saveResponse = restTemplate.postForEntity("http://localhost:"+port+"/cars/", 
				saveCar, Car.class);
		Car updateCar = saveResponse.getBody();
		updateCar.setType("crossOver");
		HttpEntity<Car> entity = new HttpEntity<>(updateCar, headers);
		ResponseEntity<Car> response = restTemplate.exchange("http://localhost:"+port+"/cars/", HttpMethod.PUT, entity, Car.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody().getType()).isEqualTo("crossOver");
	}

}
