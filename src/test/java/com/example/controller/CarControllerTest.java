package com.example.controller;

import com.example.exception.CarNotFoundException;
import com.example.model.Car;
import com.example.service.CarService;
import com.example.service.CarServiceTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("test")
//@TestPropertySource(locations = "classpath:application-test.properties")
public class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CarService carService;

    @Test
    public void getCarDetails() throws Exception{

        given(carService.getCarDetails(Mockito.anyString())).willReturn(new Car("Scala","Sadan"));

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Scala"))
                .andExpect(jsonPath("type").value("Sadan"));;
    }

    @Test
    public void carNotFound() throws Exception{

        given(carService.getCarDetails(Mockito.anyString())).willThrow(new CarNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isNotFound());
    }
}
