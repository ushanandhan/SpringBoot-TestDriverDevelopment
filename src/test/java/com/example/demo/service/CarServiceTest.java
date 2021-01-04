package com.example.demo.service;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.repository.CarRepository;
import com.example.demo.repository.CarViewRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @Mock
    CarViewRepository carViewRepository;

    @InjectMocks
    CarService carService;

    @Captor
    ArgumentCaptor<Car> type;

   @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(carService,"firstProperty","testProperty-1",String.class);
        ReflectionTestUtils.setField(carService,"secondProperty","testProperty-2",String.class);
    }

    @Test
    public void getCarDetails() throws Exception{
        //Given
        given(carRepository.findByName("pulse")).willReturn(Optional.of(new Car("pulse", "hatchback")));

        //When
        Car car = carService.getCarDetails("pulse");

        //Then
        assertNotNull(car);
        assertEquals("pulse",car.getName());
        assertEquals("hatchback",car.getType());

    }

    @Test
    public void getCar_NotFound_Test(){
       //Given
        given(carRepository.findByName("pulse")).willReturn(Optional.empty());

        //Then
        assertThrows(CarNotFoundException.class, ()-> carService.getCarDetails("pulse"));
    }

    @Test
    public void getAllCars() throws Exception{
       //Given
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("pulse", "hatchback"));
        carList.add(new  Car("duster", "hybrid"));
        carList.add(new Car("lodgy", "suv"));

        given(carRepository.findAll()).willReturn(carList);

        //When
        List<Car> returnCarList = carService.getAllCars();

        //Then
        assertEquals(carList,returnCarList);
    }

    @Test
    public void saveCar() {
        //Given
        Car car = new Car("lodgy", "SUV");
        car.setId(Long.valueOf(1003));
        given(carRepository.save(Mockito.any())).willReturn(new Car("lodgy", "SUV"));

        //When
        Car savedCar = carService.saveOrUpdate(new Car("lodgy", "SUV"));

        //Then
        assertEquals("SUV",savedCar.getType());
    }

    @Test
    public void updateCar() {
       //Given
        Car car = new Car("lodgy", "Compact SUV");
        car.setId(Long.valueOf(1003));
        given(carRepository.save(Mockito.any())).willReturn(new Car("lodgy", "Compact SUV"));

        //When
        Car savedCar = carService.saveOrUpdate(car);

        //Then
//        verify(carRepository).save(type.capture());
        then(carRepository).should().save(type.capture());
        assertEquals("Compact SUV",savedCar.getType());
        assertTrue(type.getValue().getId()!=null);
    }

}