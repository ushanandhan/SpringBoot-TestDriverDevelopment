package com.example.repository;

import com.example.model.Car;
import com.example.respository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;

    @Test
    public void getCarDetails(){
        Car car = carRepository.findByName("Duster");
        assertNotNull("Car is null here",car);
    }
}
