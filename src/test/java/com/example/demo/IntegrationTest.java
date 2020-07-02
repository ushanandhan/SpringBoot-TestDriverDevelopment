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
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:"+port+"/cars/duster", HttpMethod.GET, entity, Car.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("hybrid",response.getBody().getType());
    }
}
