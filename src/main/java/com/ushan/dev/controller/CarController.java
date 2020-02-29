package com.ushan.dev.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ushan.dev.model.Car;
import com.ushan.dev.service.CarService;

@RestController
@RequestMapping("/cars")
public class CarController {

	@Autowired
	CarService carService;
	
	@GetMapping("/{name}")
	public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
		Car car = carService.getCarDetails(name);
		return new ResponseEntity<>(car,HttpStatus.OK);
	}
	
	@GetMapping("/")
	public List<Car> getAllCars(){
		return carService.getAllCars();
	}
	
	@PostMapping("/")
	public ResponseEntity<Car> saveCar(@RequestBody Car car) {
		return new ResponseEntity<>(carService.saveOrUpdate(car),HttpStatus.CREATED);
	}
	
	@PutMapping("/")
	public ResponseEntity<Car> updateCar(@RequestBody Car car) {
		return new ResponseEntity<>(carService.saveOrUpdate(car),HttpStatus.CREATED);
	}
	
}
