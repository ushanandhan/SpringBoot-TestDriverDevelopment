package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CarServiceTest {

    CarService carService;

    @BeforeEach
    public void setUp(){
        carService = new CarService();
    }

    @Test
    public void getCarRate(){
        int rate = carService.getIntrestRateForCar("Scala");
        assertTrue("Rate is incorrect",rate==15);
    }

    @ParameterizedTest(name = "[{index}] - {0} should return {1}")
    @CsvSource(value = {"Scala,15","Duster,15","Micra,5"})
    void palindromes(String name,int expectedValue) {
        assertEquals(expectedValue,carService.getIntrestRateForCar(name));
    }

}
