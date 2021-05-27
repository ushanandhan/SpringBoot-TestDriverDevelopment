package com.example.demo;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

//    @Autowired
//    CarService carService;

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getCarDetails() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);

        ResponseEntity<Car> response = restTemplate.withBasicAuth("admin","pass").exchange(
                "http://localhost:"+port+"/cars/duster", HttpMethod.GET, entity, Car.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("hybrid",response.getBody().getType());
    }

    @Test
    public void car_Not_found_Http_Status() throws Exception{
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<Car> response = restTemplate.withBasicAuth("admin","pass").exchange(
                "http://localhost:"+port+"/cars/scala", HttpMethod.GET, entity, Car.class);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void getAllCars() throws Exception{
        ResponseEntity<List<Car>> response = restTemplate.withBasicAuth("admin","pass").exchange(
                "http://localhost:"+port+"/cars/",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Car>>() {});
        List<Car> carList = response.getBody();
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(5,carList.size());
    }

    @Test
    public void saveCar() throws Exception{
        Car car = new Car("Scala","Sadan");
        ResponseEntity<Car> response = restTemplate.withBasicAuth("admin","pass").postForEntity("http://localhost:"+port+"/cars/",
                car, Car.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    public void updateCar() throws Exception{
        Car saveCar = new Car("Captur","SUV");
        ResponseEntity<Car> saveResponse = restTemplate.withBasicAuth("admin","pass").postForEntity("http://localhost:"+port+"/cars/",
                saveCar, Car.class);
        Car updateCar = saveResponse.getBody();
        updateCar.setType("crossOver");
        HttpEntity<Car> entity = new HttpEntity<>(updateCar, headers);
        ResponseEntity<Car> response = restTemplate.withBasicAuth("admin","pass").exchange("http://localhost:"+port+"/cars/", HttpMethod.PUT, entity, Car.class);
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals("crossOver",response.getBody().getType());
    }
}
