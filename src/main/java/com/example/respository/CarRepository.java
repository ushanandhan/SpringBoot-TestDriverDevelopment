package com.example.respository;

import com.example.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findByName(String scala);
}
