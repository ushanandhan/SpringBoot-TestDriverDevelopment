#	Test Driven Development With Spring-Boot
# 1. About TDD:

## 1.1 So, what is it?
![image](https://user-images.githubusercontent.com/8769673/77243804-331f2600-6c34-11ea-8f32-35f61aeecaf7.png)
* What TDD actually is, is a cycle.
* You write a very simple test that fails. Then you write as little code as possible to make the test pass. You then write a slightly more complex test case that fails. Then you make it pass with as little code as possible. And around and around you go, in this cycle that should be complete in mere minutes (if not seconds).
* This cycle is known as the Red-> Green cycle.

## 1.2 You must refactor!
![image](https://user-images.githubusercontent.com/8769673/77243829-93ae6300-6c34-11ea-9c3c-067eed081151.png)
* However, there is an extremely important step between the passes and the next failure. You must refactor your code where appropriate. You could make any test pass with enough if statements and hard-coding, but it would be useless code. Useful code is better. And when you refactor, you refactor without fear of breaking your existing functionality. You have no fear, because your full set of tests will let you know if anything breaks.
* This cycle is known as the Red-> Green-> Refactor cycle. This cycle is <b>Test Driven Development</b>.

## 1.3 So why should we do it?
* You will be fearless
* Code will be streamlined
* You will reduce debugging time.
* Your tests will become the most comprehensive set of documentation imaginable
* Your code will have better design
* Need not worry about code coverage

## 1.4 Drawbacks
* Bugs in Tests
* Slower at the beginning
* All the members of the team need to do it
* Test need to be maintained when requirements changes

# 2. Let's start with an Example:
Mostly now a days we follow agile workflow model. So we will get requirements via UserStories. Here let's assume that we got one userstory with following endpoints. <br>
<b>Acceptance Criteria: </b><br>
  Expose below Rest URLS: <br>
	1. /cars/		-	GetAllCars [GET] <br>
	2./cars/{name}	-	Get Car details by name [GET] <br>
	3. /cars/{name} 	- 	Throw CarNotFoundException <br>
	4./cars		-	Save Car [POST] <br>
	5./cars 		-	Update Car [PUT] <br>
	6./cars/{name}	-	Delete Car [DELETE] <br>
  
## 2.1 Create Spring Boot Application with any IDE you prefer. 
I'm using Intelij here where Spring Assistant plugin is used.
![image](https://user-images.githubusercontent.com/8769673/77244049-0a4c6000-6c37-11ea-9ec0-19769085ba91.png)

Select Spring Boot version and required Libraries. 
![image](https://user-images.githubusercontent.com/8769673/77244156-24d30900-6c38-11ea-9e61-368ee79c59c0.png)

## 2.2 Let's start with Controller Unit Test
Create a class under test package with the name CarControllerTest.
```java
package com.example.controller;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@ActiveProfiles("test")
public class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testGetAllCars() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());
    }

}

```
When we run this test we end-up with the fail message as below,
