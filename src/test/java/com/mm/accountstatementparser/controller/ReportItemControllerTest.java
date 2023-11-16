package com.mm.accountstatementparser.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mm.accountstatementparser.dto.ReportItemDto;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import com.mm.accountstatementparser.repository.ReportItemRepository;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ReportItemControllerTest {
  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
          .withUsername("postgres")
          .withPassword("mysecretpassword")
          .withDatabaseName("postgres");

  @Autowired private MockMvc mockMvc;
  @Autowired private ReportItemRepository reportItemRepository;
  private static final String BASE_URL = "/api/v1/report-items/";

  @DynamicPropertySource
  private static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
    dynamicPropertyRegistry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
    dynamicPropertyRegistry.add("spring.datasource.username", postgresContainer::getUsername);
    dynamicPropertyRegistry.add("spring.datasource.password", postgresContainer::getPassword);
    dynamicPropertyRegistry.add("spring.jpa.properties.hibernate.default_schema", () -> "public");
  }

  @BeforeAll
  public static void beforeAll() {
    postgresContainer.start();
  }

  @AfterAll
  public static void afterAll() {
    postgresContainer.stop();
  }

  @Test
  public void createGetByIdUpdateDeleteReportItem() throws Exception {
    String reportItemRequest =
        """
    {
        "name": "report-item-name",
        "plannedAmount": 10000.00,
        "reportItemCategory": "NEEDS"
    }
        """;

    MvcResult result =
        mockMvc
            .perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(reportItemRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isString())
            .andExpect(jsonPath("$.name", is("report-item-name")))
            .andExpect(jsonPath("$.plannedAmount", is(10000.00)))
            .andExpect(jsonPath("$.realAmount", is(0)))
            .andExpect(jsonPath("$.difference", is(10000.00)))
            .andExpect(jsonPath("$.reportItemCategory", is("NEEDS")))
            .andDo(print())
            .andReturn();

    int startIndex = 7;
    UUID reportItemId =
        UUID.fromString(
            result.getResponse().getContentAsString().substring(startIndex, startIndex + 36));

    mockMvc
        .perform(get(BASE_URL + reportItemId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.name", is("report-item-name")))
        .andExpect(jsonPath("$.plannedAmount", is(10000.00)))
        .andExpect(jsonPath("$.realAmount", is(0.00)))
        .andExpect(jsonPath("$.difference", is(10000.00)))
        .andExpect(jsonPath("$.reportItemCategory", is("NEEDS")))
        .andDo(print());

    String reportItemUpdateRequest =
        """
    {
        "name": "UPDATED-report-item-name",
        "plannedAmount": 10000.00,
        "reportItemCategory": "NEEDS"
    }
       """;

    mockMvc
        .perform(
            put(BASE_URL + reportItemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reportItemUpdateRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("UPDATED-report-item-name")))
        .andDo(print());

    mockMvc
        .perform(delete(BASE_URL + reportItemId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void getAllReportItems_shouldReturnListOfReportItemDtos() throws Exception {

    for (int i = 0; i < 5; i++) {
      reportItemRepository.save(getReportItemDto(i).toEntity());
    }

    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(5)))
        .andExpect(jsonPath("$[0].plannedAmount", is(0.0)))
        .andExpect(jsonPath("$[1].plannedAmount", is(1.0)))
        .andExpect(jsonPath("$[2].plannedAmount", is(2.0)))
        .andExpect(jsonPath("$[3].plannedAmount", is(3.0)))
        .andExpect(jsonPath("$[4].plannedAmount", is(4.0)));
  }

  private ReportItemDto getReportItemDto(int number) {
    return new ReportItemDto(
        "name - " + number, BigDecimal.valueOf(number), TransactionMainCategory.NEEDS);
  }
}