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
	1./cars/{name}	-	Get Car details by name [GET] <br>
	2./cars/{name} 	- 	Throw CarNotFoundException <br>
  
## 2.1 Create Spring Boot Application with any IDE you prefer. 
I'm using Intelij here where Spring Assistant plugin is used.
![image](https://user-images.githubusercontent.com/8769673/77244049-0a4c6000-6c37-11ea-9ec0-19769085ba91.png)

Select Spring Boot version and required Libraries. 
![image](https://user-images.githubusercontent.com/8769673/77244156-24d30900-6c38-11ea-9e61-368ee79c59c0.png)

Since we are going to use Junit 5 along with Spring boot Please include below dependencies too,
```maven
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-api</artifactId>
	<scope>test</scope>
</dependency>

<!-- https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine -->
<dependency>
	<groupId>org.junit.jupiter</groupId>
	<artifactId>junit-jupiter-engine</artifactId>
	<scope>test</scope>
</dependency>
```

## 2.2 Let's start with Controller Unit Test
Let's start from Controller. I'm going to take you to the tour where how step by step programming is happening with the help of Test Driven Development Approach. First we'll create CarControllerTest. Here we are going to create end point for - /cars/{name}.

```java
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CarController.class)
public class CarControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getCar_Details() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isOk());
    }

}

```
@WebMvcTest(controllers = CarController.class) code will give you error saying that CarController class is not available. Hence we'll go to src/main folder and will create just CarController without Body. 

```java
public class CarController {

}
```
Now the compilation error is resolved. 
When we run CarControllerTest class now, we end-up with the failed message as below,

![image](https://user-images.githubusercontent.com/8769673/86355745-caab0080-bc88-11ea-84ef-ccddad57afd4.png)

The reason for below error is that, because there is no rest endpoint with url /cars/Scala in CarController class. Let's create the endpoint in CarController class.

```java
@RestController
@RequestMapping("/cars")
public class CarController {

   @GetMapping("/{name}")
    public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
        return new ResponseEntity<>( HttpStatus.OK);
    }
}
```

Once again we'll run the CarControllerTest class. 
![image](https://user-images.githubusercontent.com/8769673/86356060-42792b00-bc89-11ea-8c54-53727d05dbb5.png)
Yes it is passed now. Hurray!! guys we created endpoint successfully.

Now are focus it so return the Car details, for which we need Car model. Let's create Car model class under model package.
```java
@Entity
@Table(name="CARS")
public class Car {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private String type;

    public Car() {}

    public Car(String name, String type) {
        this.name = name;
        this.type = type;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
```
Now in CarController class lets add below codes,
```java
@RestController
@RequestMapping("/cars")
public class CarController {

   @GetMapping("/{name}")
    public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
        Car car = new Car();
        return new ResponseEntity<>( car,HttpStatus.OK);
    }
}
```

Now lets navigate CarControllerTest class and add few more point to existing table as below,
```java
@Test
    public void getCar_Details() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("name").value("Scala"))
                .andExpect(jsonPath("type").value("Sadan"));
    }
```
From above code, what are we expecting from test is that, when we pass /cars/Scala, then the response should contain Car object with Scala as name and Sadan as type. 
Now we'll execute the test.
![image](https://user-images.githubusercontent.com/8769673/86356863-9b958e80-bc8a-11ea-983d-70e0cb12e2b5.png)
The reason for this that we are passing car object with null values in it. 

Now its time to introduce the power of Mockito to mock the response. As I said earlier Since we are going to focus only on controller we will mock any class which is external to CarController class. In CarController lets change below line,

```java
@Autowired
CarService carService;
    
@GetMapping("/{name}")
public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
	Car car = carService.getCarDetails(name);
	return new ResponseEntity<>( car,HttpStatus.OK);
}
```
