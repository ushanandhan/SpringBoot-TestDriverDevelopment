package com.example.demo.service;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;


public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCarDetails() throws Exception{
        given(carRepository.findByName("pulse")).willReturn(Optional.of(new Car("pulse", "hatchback")));

        Car car = carService.getCarDetails("pulse");
        assertNotNull(car);
        assertEquals("pulse",car.getName());
        assertEquals("hatchback",car.getType());

    }

    @Test
    public void getCar_NotFound_Test(){
        given(carRepository.findByName("pulse")).willThrow(new CarNotFoundException());

        assertThrows(CarNotFoundException.class, ()-> carService.getCarDetails("pulse"));
    }

}