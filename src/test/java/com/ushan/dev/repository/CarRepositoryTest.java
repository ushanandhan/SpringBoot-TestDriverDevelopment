package com.ushan.dev.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ushan.dev.model.Car;
import com.ushan.dev.repository.CarRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryTest {

	@Autowired
	private CarRepository carRepository;
	
	@Test
	public void testFindByName() {
		Optional<Car> car = carRepository.findByName("duster");
		assertThat(car.isPresent()).isEqualTo(true);
	}
	
	@Test
	public void CarNotFound() {
		Optional<Car> car = carRepository.findByName("duster1");
		assertThat(car.isPresent()).isEqualTo(false);
	}
	
	@Test
	public void getCarList() {
		List<Car> carList = carRepository.findAll();
		carList.forEach(car -> {
			System.out.println(car.getId()+ " :: "+ car.getName()+" :: "+car.getType());
		});
		assertThat(carList.size()).isEqualTo(3);
		
	}
	
	@Test
	public void saveCar() {
		Car savedCar = carRepository.save(new Car("pulse", "hatchback"));
		Optional<Car> returnCar = carRepository.findByName("pulse");
		assertThat(returnCar.isPresent()).isEqualTo(true);
		assertThat(returnCar.get()).isEqualTo(savedCar);
	}
	
	@Test
	public void updateCar() {
		Optional<Car> toBeUpdatedCar = carRepository.findByName("duster");
		toBeUpdatedCar.get().setType("SUV");
		carRepository.save(toBeUpdatedCar.get());
		Optional<Car> updateCar = carRepository.findByName("duster");
		assertThat(updateCar.get().getType()).isEqualTo("SUV");
	}

}
