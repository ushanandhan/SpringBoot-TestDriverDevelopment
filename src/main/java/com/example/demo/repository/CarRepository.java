package com.example.demo.repository;

import com.example.demo.dto.CarDto;
import com.example.demo.model.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    Optional<Car> findByName(String name);
    List<Car> findAll();
    List<Car> findByTypeContaining(String type);

    /**
     * This how we can use JPA Query Language to fetch join data.
     * @return
     */
    @Query("SELECT new com.example.demo.dto.CarDto(p.name,c.name) FROM Car c INNER JOIN Person p ON c.person = p.id")
    List<CarDto> findCarsAndPersons();
}
