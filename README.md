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
```xml
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
Let's start from Controller. I'm going to take you to the tour where how step by step programming is happening with the help of Test Driven Development Approach. First we'll create CarControllerTest. Here we are going to create an end point for - /cars/{name}.

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
At line @WebMvcTest(controllers = CarController.class) code will give you an error saying that CarController class is not available. Hence we'll go to src/main folder and will create just CarController without Body. 

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

Now are focus is to return the Car details, for which we need Car model. Let's create Car model class under model package.
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

Now lets navigate to CarControllerTest class and add few more point to existing table as below,
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

Lets assume that from external class CarService, we will get the car details. Base on that assumption, in CarController lets change below line,

```java
@Autowired
CarService carService;
    
@GetMapping("/{name}")
public ResponseEntity<Car> getCarDetails(@PathVariable String name) throws Exception {
	Car car = carService.getCarDetails(name);
	return new ResponseEntity<>( car,HttpStatus.OK);
}
```
Now we need to create CarService class with method getCarDetails without body. 

```java
@Service
public class CarService {

    public Car getCarDetails(String name) {
       return null;
    }
}
```

## 2.3 Introduction of Mockito
As I said earlier Since we are going to focus only on controller we will mock any class which is external to CarController class. 

Now in CarControllerTest, we are going to mock CarService class and give the definition for getCarDetails method present in it. 
```java
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
```
Here we are mocking CarService object with @MockBean annotation.
```java
given(carService.getCarDetails(Mockito.anyString())).willReturn(new Car("Scala","Sadan"));
```
With above line we are defining the behaviour in such a way that, if we pass any String as name it should return new Car details. Now lets run the test once again.
Yes it is passed.

Lets assume if no car details avaliable for the given name, what would happen. For this scenario we need to create CarNotFoundException class which will be throwed when no car details present for given name. 

```java
@ResponseStatus(code= HttpStatus.NOT_FOUND)
public class CarNotFoundException extends RuntimeException {

    public CarNotFoundException() {}
}
```
Again from CarControllerTest we are going to have another test to validate this scenario. 
```java
    @Test
    public void Car_NotFoud_HttpStatus() throws Exception{
        given(carService.getCarDetails(Mockito.anyString())).willThrow(new CarNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/cars/Scala"))
                .andExpect(status().isNotFound());
    }
```
If you run this test method it will work like charm. So far we completed two scenarios one with valid response and another with Exception throwed in Controller class level.

Now lets move to CarService Class. As we know so far we did not touch CarService class as part of CarControllerTest. Now Lets create CarServiceTest which is dedicated to CarService class. 

## 2.4 Service Unit Test
Here we are going to use only Mockito related setup to ensure that how getCarDetails method is working. Here also we have two scenarios one with valid result from CarRepository and another with CarNotFoundException. Create CarRepository with findByName(name) interface first.

```java
public interface CarRepository {
    public Optional<Car> findByName(String name);
}
```
Now create CarServiceTest class,
```java
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getCarDetails() throws Exception{
        given(carRepository.findByName("pulse")).willReturn(Optional.of(new Car("pulse", "hatchback")));

        Car car = carService.getCarDetails("pulse");
        assertNotNull(car);
        assertEquals("pulse",car.getName());
        assertEquals("hatchback",car.getType());

    }

    @Test
    public void getCar_NotFound_Test(){
        given(carRepository.findByName("pulse")).willThrow(new CarNotFoundException());

        assertThrows(CarNotFoundException.class, ()-> carService.getCarDetails("pulse"));
    }

}
```
As per above code we can see we did not bother about the logic behind CarRepository class. We are just mocking them by our expectations. we can run now.
Yes both test methods are passed.

## 2.5 Repository Unit Test
Now lets focus on CarRepository interface. We need to ensure that the CarRepository's method findByName should give us proper data fetched from database. Here we are going to use Embedded H2Database. Under src/main/resources folder add data.sql file just like below,
```sql
DROP TABLE IF EXISTS CARS;

CREATE TABLE CARS (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  name VARCHAR(250) NOT NULL,
  type VARCHAR(250) NOT NULL
);

INSERT INTO CARS (id, name, type) VALUES ('1001','duster','hybrid');
INSERT INTO CARS (id, name, type) VALUES ('1002','micra','hatchback');
INSERT INTO CARS (id, name, type) VALUES ('1003','lodgy','suv');
```
What will happen here is that when we run @DataJpaTest annotated CarRepositoryTest class, these data will be stored in H2 Database untill the execution of test method is over. 

Create CarRepositoryTest Class,
```java
@ExtendWith(SpringExtension.class)
@DataJpaTest
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;

    @Test
    public void testFindByName() {
        Optional<Car> car = carRepository.findByName("duster");
        assertTrue(car.isPresent());
    }

    @Test
    public void testFindByName_Not_Found(){
        Optional<Car> car = carRepository.findByName("pulse");
        assertFalse(car.isPresent());
    }
}
```
Lets run this,
![image](https://user-images.githubusercontent.com/8769673/86366509-7dcf2600-bc98-11ea-98a6-d67f97544d7c.png)
Yes these cases passed. If you see the highlighted part, the query is executed to fetch the data from database. 

## 2.6 Cache Test
Now lets focus on Cache test in our application. Lets go and add @EnableCaching to Application class,
```java
@SpringBootApplication
@EnableCaching
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
```
Now go to CarService class and annotate getCarDetails method with @Cacheable("cars").

Lets create CacheTest class. Just be clear that since we are going to verify cache, we need to use @SpringBootTest in this class,
```java
@SpringBootTest
public class CacheTest {

    @MockBean
    CarRepository carRepository;

    @Autowired
    CarService carService;

    @Test
    void cacheTest() {
        given(carRepository.findByName("pulse")).willReturn(Optional.of(new Car("pulse", "hatchback")));

        Car car = carService.getCarDetails("pulse");
        assertNotNull(car);
        carService.getCarDetails("pulse");

        Mockito.verify(carRepository,Mockito.times(1)).findByName("pulse");

    }
}
```
Here with the help of Mockito's verify method we are ensuring that carRepository's findByName method is called only once,though we called carService.getCarDetails() twice.

## 2.7 Integration Test
Now let create IntegrationTest class to ensure entire flow is working fine.

```java
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CarService carService;

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void getCarDetails() throws Exception {
        HttpEntity<String> entity = new HttpEntity<String>(null,headers);
        ResponseEntity<Car> response = restTemplate.exchange(
                "http://localhost:"+port+"/cars/duster", HttpMethod.GET, entity, Car.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals("hybrid",response.getBody().getType());
    }
}
```
If we run all Test at once we may get following out put from Intelij,
![image](https://user-images.githubusercontent.com/8769673/86367899-28941400-bc9a-11ea-8142-b5dfe3e031bd.png)

So finally we came to end of the session. That all about Test Driven Development approach towards creation of Spring Boot application. 
