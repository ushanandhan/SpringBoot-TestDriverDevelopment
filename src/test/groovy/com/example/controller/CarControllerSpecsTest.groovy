package com.example.controller

import com.example.model.Car
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
class CarControllerSpecsTest extends Specification {

    @Autowired
    private TestRestTemplate restTemplate;

    def "Get Car Details by name"(){
        when:
        def response = restTemplate.getForEntity('/cars/Duster', Car)

        then:
        response.statusCode == HttpStatus.OK
        response.body!=null
    }

    def "Get Car interest Rates"(String carName,Integer expectedRate){
        when:
        def response = restTemplate.getForEntity('/cars/'+carName+'/rate',Car)

        then:
        response.body.rate == expectedRate

        where:
        carName | expectedRate
        'Lodgy' | 5
        'Duster'| 15
        'Micra' | 5
    }
}
