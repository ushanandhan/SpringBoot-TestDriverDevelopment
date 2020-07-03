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
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


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

    @Test
    public void getAllCars() throws Exception{
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("pulse", "hatchback"));
        carList.add(new  Car("duster", "hybrid"));
        carList.add(new Car("lodgy", "suv"));

        given(carRepository.findAll()).willReturn(carList);

        List<Car> returnCarList = carService.getAllCars();
        assertEquals(carList,returnCarList);
    }

    @Test
    public void saveOrUpdateCar() {
        given(carRepository.save(Mockito.any())).willReturn(new Car("lodgy", "SUV"));

        Car savedCar = carService.saveOrUpdate(new Car("lodgy", "SUV"));
        assertEquals("SUV",savedCar.getType());
    }

}