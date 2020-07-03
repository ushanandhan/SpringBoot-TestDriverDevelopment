package com.example.demo.cache;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class CacheTest {

    @MockBean
    CarRepository carRepository;

    @Autowired
    CarService carService;

    @Test
    void cacheTest() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("pulse", "hatchback"));
        carList.add(new  Car("duster", "hybrid"));
        carList.add(new Car("lodgy", "suv"));
        given(carRepository.findAll()).willReturn(carList);

        List<Car> cars = carService.getAllCars();
        assertNotNull(cars);

        carService.getAllCars();

        Mockito.verify(carRepository,Mockito.times(1)).findAll();

    }
}
