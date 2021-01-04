package com.example.demo.controller;

import com.example.demo.exception.CarNotFoundException;
import com.example.demo.model.Car;
import com.example.demo.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CarController.class)
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    @Test
    public void getCar_Details() throws Exception{
        given(carService.getCarDetails(Mockito.anyString())).willReturn(new Car("Scala","Sadan"));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Scala"))
                .andExpect(jsonPath("type").value("Sadan"));
    }

    @Test
    public void Car_NotFoud_HttpStatus() throws Exception{
        given(carService.getCarDetails(Mockito.anyString())).willThrow(new CarNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testBadRequest() throws Exception{
        Car car = new Car("pulse", "hatchback");
        car.setId(Long.valueOf(1003));
        given(carService.saveOrUpdate(Mockito.any())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.put("/cars/")
                .content(asJsonString(new String("car")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void getAllCars() throws Exception{
        List<Car> carList = new ArrayList<>();
        carList.add(new Car("pulse", "hatchback"));
        carList.add(new  Car("duster", "hybrid"));
        carList.add(new Car("lodgy", "suv"));

        given(carService.getAllCars()).willReturn(carList);

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

    @Test
    public void saveCar() throws Exception{
        Car car = new Car("pulse", "hatchback");
        car.setId(new Long(1003));
        given(carService.saveOrUpdate(Mockito.any())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.post("/cars/")
                .content(asJsonString(new Car("pulse", "hatchback")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap())
                .andDo(print());
    }

    @Test
    public void updateCar() throws Exception{
        Car car = new Car("pulse", "hatchback");
        car.setId(new Long(1003));
        given(carService.saveOrUpdate(Mockito.any())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.put("/cars/")
                .content(asJsonString(new Car("pulse", "hatchback")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isMap())
                .andDo(print());
    }

    @Test
    public void testGetCarById()throws Exception{
        Car car = new Car("pulse", "hatchback");
        car.setId(new Long(1003));
        given(carService.getCarDetailsById(Mockito.anyLong())).willReturn(car);

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/carId")
                .param("id", "1003")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
