package com.ushan.dev.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.ushan.dev.exception.CarNotFoundException;
import com.ushan.dev.model.Car;
import com.ushan.dev.repository.CarRepository;
import com.ushan.dev.service.CarService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarServiceTest {

	@MockBean
	CarRepository carRepository;
	
	@Autowired
	CarService carService;
	
	@Test
	public void getCarDetails() throws Exception{
		given(carRepository.findByName("pulse")).willReturn(Optional.of(new Car("pulse", "hatchback")));
		
		Car car = carService.getCarDetails("pulse");
		assertThat(car.getType()).isEqualTo("hatchback");
	}
	
	@Test(expected=CarNotFoundException.class)
	public void getCar_NotFound() throws Exception{
		given(carRepository.findByName("pulse")).willReturn(Optional.empty());
		
		carService.getCarDetails("pulse");
	}
	
	@Test
	public void getAllCars() throws Exception{
		List<Car> carList = new ArrayList<Car>();
		carList.add(new Car("pulse", "hatchback"));
		carList.add(new  Car("duster", "hybrid"));
		carList.add(new Car("lodgy", "suv"));
		
		given(carRepository.findAll()).willReturn(carList);
		
		List<Car> returnCarList = carService.getAllCars();
		assertThat(carList).isEqualTo(returnCarList);
	}
	
	@Test
	public void saveOrUpdateCar() {
		given(carRepository.save(Mockito.any())).willReturn(new Car("lodgy", "suv"));
		
		Car savedCar = carService.saveOrUpdate(new Car("lodgy", "suv"));
		assertThat(savedCar.getType()).isEqualTo("suv");
	}
	
	
}
