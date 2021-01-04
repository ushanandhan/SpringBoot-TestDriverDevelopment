package com.example.demo.service;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.helper.Notification;
import com.example.demo.model.Car;
import com.example.demo.model.CarView;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CarViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    @Value("${example.firstProperty}") private String firstProperty;
    @Value("${example.secondProperty}") private String secondProperty;


    @Autowired
    CarRepository carRepository;

    @Autowired
    CarViewRepository carViewRepository;

    public Car getCarDetails(String name) {
        Car car = null;
        Optional<Car> optionalCar =  carRepository.findByName(name);
        if(optionalCar.isPresent()) {
            System.out.println("1st prop here : "+firstProperty);
            car = optionalCar.get();
            String status = Notification.sendEmail("xyz@email.com");
            System.out.println("2nd prop here : "+secondProperty);
        }else {
            throw new CarNotFoundException();
        }
        return car;
    }

    public Car getCarDetailsById(long id){
        Car car = null;
        Optional<Car> optionalCar = carRepository.findById(id);
        if(optionalCar.isPresent()){
            car = optionalCar.get();
        }else {
            throw new CarNotFoundException();
        }
        return null;
    }

    @Cacheable("cars")
    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    @CacheEvict(value = "cars",allEntries = true)
    public Car saveOrUpdate(Car car) {
        return carRepository.save(car);
    }
}
