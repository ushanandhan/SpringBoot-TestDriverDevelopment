package com.ushan.dev.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ushan.dev.exception.CarNotFoundException;
import com.ushan.dev.model.Car;
import com.ushan.dev.service.CarService;

@RunWith(SpringRunner.class)
@WebMvcTest
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
	public void getAllCars() throws Exception{
		List<Car> carList = new ArrayList<Car>();
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

	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
