package com.example.demo.repository;

import com.example.demo.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = CarRepositoryContainerTest.PropertiesInitializer.class)
@Testcontainers
class CarRepositoryContainerTest {

    @Autowired
    private CarRepository carRepository;

    @Container
    private static final PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:11.1")
            .withDatabaseName("users")
            .withUsername("postgres")
            .withPassword("password");

    static class PropertiesInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url="+postgres.getJdbcUrl(),
                    "spring.datasource.username="+postgres.getUsername(),
                    "spring.datasource.password="+postgres.getPassword()
            );
        }
    }

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