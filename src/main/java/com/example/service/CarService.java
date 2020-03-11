package com.example.service;

import com.example.model.Car;
import com.example.respository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    CarRepository carRepository;

    public Car getCarDetails(String name) {
        return carRepository.findByName(name);
    }

    public int getIntrestRateForCar(String name){
        int intrestRate = 0;
        if(name.equals("Scala") || name.equals("Duster")){
            intrestRate = 15;
        }else{
            intrestRate =5;
        }
        return intrestRate;
    }
}
