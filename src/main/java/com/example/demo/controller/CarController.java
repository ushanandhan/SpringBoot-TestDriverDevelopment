package com.example.demo.controller;

import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    CarService carService;

   @GetMapping("/{name}")
    public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
        Car car = carService.getCarDetails(name);
        return new ResponseEntity<>( car,HttpStatus.OK);
    }
}
