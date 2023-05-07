package by.bsuir.housing;

import by.bsuir.housing.config.TestConfig;
import by.bsuir.housing.controller.CalculationRequest;
import by.bsuir.housing.controller.handler.GlobalExceptionHandler;
import by.bsuir.housing.dto.DealDto;
import by.bsuir.housing.service.DealService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.time.LocalDate;

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
class DealControllerIntegrationTests {


    private MockMvc mockMvc;
    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DealService dealService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private DealDto testDeal;

    @BeforeEach
    void setup() {
        testDeal = new DealDto();
        testDeal.setId(1);
        testDeal.setEstateId(1);
        testDeal.setUserId(1);
        testDeal.setPrice(100);
        testDeal.setDays(3);
        testDeal.setPeopleCount(4);
        testDeal.setArriving(LocalDate.parse("2023-04-08"));
    }

    @Test
    public void shouldExistsGlobalControllerAdvice() {
        GlobalExceptionHandler handler = this.webApplicationContext.getBean(GlobalExceptionHandler.class);
        assertNotNull(handler);
    }

    @Test
    void shouldReturnJsonIfGetRequest() throws Exception {
        mockMvc.perform(get("/deal")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnCorrectJsonIfGetRequest() throws Exception {
        mockMvc.perform(get("/deal")
                        .param("page", "0")
                        .param("size", "10")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testDeal.getId()))
                .andExpect(jsonPath("$[0].estateId").value(testDeal.getEstateId()))
                .andExpect(jsonPath("$[0].userId").value(testDeal.getUserId()))
                .andExpect(jsonPath("$[0].price").value(testDeal.getPrice()))
                .andExpect(jsonPath("$[0].days").value(testDeal.getDays()))
                .andExpect(jsonPath("$[0].peopleCount").value(testDeal.getPeopleCount()))
                .andExpect(jsonPath("$[0].arriving").value(testDeal.getArriving().toString()));
    }

    @Test
    public void shouldReturnCorrectJsonIfPostWithValidBody() throws Exception {
        mockMvc.perform(post("/deal")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").value(testDeal.getId()))
                .andExpect(jsonPath("$.estateId").value(testDeal.getEstateId()))
                .andExpect(jsonPath("$.userId").value(testDeal.getUserId()))
//                .andExpect(jsonPath("$.price").value(testDeal.getPrice()))
                .andExpect(jsonPath("$.days").value(testDeal.getDays()))
                .andExpect(jsonPath("$.peopleCount").value(testDeal.getPeopleCount()))
                .andExpect(jsonPath("$.arriving").value(testDeal.getArriving().toString()));
    }



    @Test
    public void shouldReturnCorrectJsonIfPutWithValidBody() throws Exception {
        mockMvc.perform(put("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testDeal.getId()))
                .andExpect(jsonPath("$.estateId").value(testDeal.getEstateId()))
                .andExpect(jsonPath("$.userId").value(testDeal.getUserId()))
//                .andExpect(jsonPath("$.price").value(testDeal.getPrice()))
                .andExpect(jsonPath("$.days").value(testDeal.getDays()))
                .andExpect(jsonPath("$.peopleCount").value(testDeal.getPeopleCount()))
                .andExpect(jsonPath("$.arriving").value(testDeal.getArriving().toString()));
    }

    @Test
    public void shouldReturnNotFoundIfNoUserWithSuchIdPost() throws Exception {
        testDeal.setUserId(69);
        mockMvc.perform(post("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnNotFoundIfNoEstateWithSuchIdPost() throws Exception {
        testDeal.setEstateId(69);
        mockMvc.perform(post("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnNotFoundIfNoUserWithSuchIdPut() throws Exception {
        testDeal.setUserId(69);
        mockMvc.perform(put("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldReturnNotFoundIfNoEstateWithSuchIdPut() throws Exception {
        testDeal.setEstateId(69);
        mockMvc.perform(put("/deal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testDeal))
                )
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void shouldReturnCorrectPrice() throws Exception {
        CalculationRequest calculationRequest = new CalculationRequest();
        calculationRequest.setPeopleCount(Byte.valueOf("4"));
        calculationRequest.setDays(Byte.valueOf("7"));
        calculationRequest.setEstateId(1);
        calculationRequest.setArriving(LocalDate.parse("2023-04-10"));

        mockMvc.perform(post("/deal/calculate-price")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(calculationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("1267.2"));
    }

    @Test
    void shouldDeleteDealAndReturnNoDeals() throws Exception {
        mockMvc.perform(delete("/deal/{id}", testDeal.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/deal")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}