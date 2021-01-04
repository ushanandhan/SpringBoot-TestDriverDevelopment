package com.example.demo.repository;

import com.example.demo.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void testFindByName() {
        Optional<Car> car = carRepository.findByName("duster");
        assertTrue(car.isPresent());
    }

    @Test
    public void testFindByName_Not_Found(){
        Optional<Car> car = carRepository.findByName("pulse");
        assertFalse(car.isPresent());
    }

    @Test
    public void getCarList() {
        List<Car> carList = carRepository.findAll();
        carList.forEach(car -> {
            System.out.println(car.getId()+ " :: "+ car.getName()+" :: "+car.getType());
        });
        assertEquals(4,carList.size());
    }

    @Test
    public void saveCar() {
        Car savedCar = carRepository.save(new Car("pulse", "hatchback"));
        Optional<Car> returnCar = carRepository.findByName("pulse");
        assertTrue(returnCar.isPresent());
        assertEquals(savedCar,returnCar.get());
    }

    @Test
    public void updateCar() {
        Optional<Car> toBeUpdatedCar = carRepository.findByName("duster");
        toBeUpdatedCar.get().setType("SUV");
        carRepository.save(toBeUpdatedCar.get());
        Optional<Car> updateCar = carRepository.findByName("duster");
        assertEquals("SUV",updateCar.get().getType());
    }

    @Test void testFindCarsByType(){
        List<Car> cars = carRepository.findByTypeContaining("suv");
        assertEquals(cars.size(),2);
    }
}