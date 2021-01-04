package com.example.demo.repository;

import com.example.demo.model.Car;
import com.example.demo.model.CarView;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarViewRepository extends CrudRepository<CarView, Long> {

    public List<CarView>  findAll();
}
