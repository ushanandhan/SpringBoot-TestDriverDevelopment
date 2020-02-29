package com.ushan.dev.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ushan.dev.model.Car;

public interface CarRepository extends CrudRepository<Car, Long> {

	public Optional<Car> findByName(String name);
	
	public List<Car> findAll();
}
