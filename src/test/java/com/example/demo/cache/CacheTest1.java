package com.example.demo.cache;

import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.service.CarService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@SpringBootTest
@ActiveProfiles("test")
public class CacheTest1 {

    @MockBean
    CarRepository carRepository;

    @Autowired
    CarService carService;

    @Test
    void testCarsCache() {
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("pulse", "hatchback"));
        carList.add(new  Car("duster", "hybrid"));
        carList.add(new Car("lodgy", "suv"));
        given(carRepository.findAll()).willReturn(carList);

        List<Car> cars = carService.getAllCars();
        verify(carRepository, Mockito.times(1)).findAll();
        List<Car> retryCars = carService.getAllCars();
        verifyNoMoreInteractions(carRepository);
    }
}
