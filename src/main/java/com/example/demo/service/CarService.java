package com.example.demo.service;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    @Cacheable("cars")
    public Car getCarDetails(String name) {
        Car car = null;
        Optional<Car> optionalCar =  carRepository.findByName(name);
        if(optionalCar.isPresent()) {
            car = optionalCar.get();
        }else {
            throw new CarNotFoundException();
        }
        return car;
    }
}
