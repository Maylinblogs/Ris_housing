package by.bsuir.housing;

import by.bsuir.housing.config.TestConfig;
import by.bsuir.housing.controller.CalculationRequest;
import by.bsuir.housing.controller.handler.GlobalExceptionHandler;
import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.entity.Estate;
import by.bsuir.housing.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@AutoConfigureMockMvc
//@ContextConfiguration(classes = TestConfig.class)
@Import(TestConfig.class)
@TestPropertySource(locations = "classpath:application-integration.properties")
public class EstateControllerIntegrationTests {

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DealService dealService;

    @Autowired
    private WebApplicationContext webApplicationContext;



    private Estate testEstate;

    @BeforeEach
    void setup() {
        testEstate = new Estate();
        testEstate.setId(1);
        testEstate.setCity("Klichev");
        testEstate.setStreetName("Pobedy");
        testEstate.setHouseNumber(69);
        testEstate.setFloorNumber(3);
        testEstate.setFlatNumber(42);
        testEstate.setSquare(37);
        testEstate.setType(1);
        testEstate.setPrice(40);
        testEstate.setLatitude(53.483772);
        testEstate.setLongitude(29.340017);
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalExceptionHandler handler = this.webApplicationContext.getBean(GlobalExceptionHandler.class);
        assertNotNull(handler);
    }

    @Test
    void shouldReturnJsonIfGetRequest() throws Exception {
        mockMvc.perform(get("/estate")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCorrectJsonIfGetRequest() throws Exception {
        mockMvc.perform(get("/estate")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testEstate.getId()))
                .andExpect(jsonPath("$[0].city").value(testEstate.getCity()))
                .andExpect(jsonPath("$[0].streetName").value(testEstate.getStreetName()))
                .andExpect(jsonPath("$[0].houseNumber").value(testEstate.getHouseNumber()))
                .andExpect(jsonPath("$[0].floorNumber").value(testEstate.getFloorNumber()))
                .andExpect(jsonPath("$[0].flatNumber").value(testEstate.getFlatNumber()))
                .andExpect(jsonPath("$[0].square").value(testEstate.getSquare()))
                .andExpect(jsonPath("$[0].type").value(testEstate.getType()))
                .andExpect(jsonPath("$[0].price").value(testEstate.getPrice()))
                .andExpect(jsonPath("$[0].latitude").value(testEstate.getLatitude()))
                .andExpect(jsonPath("$[0].longitude").value(testEstate.getLongitude()));
    }

    @Test
    public void shouldReturnCorrectJsonIfPostWithValidBody() throws Exception {
        mockMvc.perform(post("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEstate))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.city").value(testEstate.getCity()))
                .andExpect(jsonPath("$.streetName").value(testEstate.getStreetName()))
                .andExpect(jsonPath("$.houseNumber").value(testEstate.getHouseNumber()))
                .andExpect(jsonPath("$.floorNumber").value(testEstate.getFloorNumber()))
                .andExpect(jsonPath("$.flatNumber").value(testEstate.getFlatNumber()))
                .andExpect(jsonPath("$.square").value(testEstate.getSquare()))
                .andExpect(jsonPath("$.type").value(testEstate.getType()))
                .andExpect(jsonPath("$.price").value(testEstate.getPrice()))
                .andExpect(jsonPath("$.latitude").value(testEstate.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(testEstate.getLongitude()));
    }



    @Test
    public void shouldReturnCorrectJsonIfPutWithValidBody() throws Exception {
        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEstate))
                )
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(testEstate.getId()))
                .andExpect(jsonPath("$.city").value(testEstate.getCity()))
                .andExpect(jsonPath("$.streetName").value(testEstate.getStreetName()))
                .andExpect(jsonPath("$.houseNumber").value(testEstate.getHouseNumber()))
                .andExpect(jsonPath("$.floorNumber").value(testEstate.getFloorNumber()))
                .andExpect(jsonPath("$.flatNumber").value(testEstate.getFlatNumber()))
                .andExpect(jsonPath("$.square").value(testEstate.getSquare()))
                .andExpect(jsonPath("$.type").value(testEstate.getType()))
                .andExpect(jsonPath("$.price").value(testEstate.getPrice()))
                .andExpect(jsonPath("$.latitude").value(testEstate.getLatitude()))
                .andExpect(jsonPath("$.longitude").value(testEstate.getLongitude()));
    }

    @Test
    public void shouldReturnReportIfExistsEstateWithId() throws Exception {
        mockMvc.perform(get("/estate/" + testEstate.getId() + "/report"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("Report-")))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, "application/json"))
                .andReturn();
    }

    @Test
    public void shouldReturnNotFoundIfNoEstateWithSuchIdReport() throws Exception {
        testEstate.setId(69);
        mockMvc.perform(get("/estate/" + testEstate.getId() + "/report"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnNotFoundIfNoEstateWithSuchIdPut() throws Exception {
        testEstate.setId(69);
        mockMvc.perform(put("/estate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEstate))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldDeleteEstateAndReturnNoEstates() throws Exception {
        mockMvc.perform(delete("/estate/{id}", testEstate.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/estate")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
