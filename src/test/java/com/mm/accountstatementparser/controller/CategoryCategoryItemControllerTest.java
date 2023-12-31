package com.mm.accountstatementparser.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mm.accountstatementparser.dto.entityDto.CategoryItemDto;
import com.mm.accountstatementparser.repository.CategoryItemRepository;

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
public class CategoryCategoryItemControllerTest {
  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
          .withUsername("postgres")
          .withPassword("mysecretpassword")
          .withDatabaseName("postgres");

  @Autowired private MockMvc mockMvc;
  @Autowired private CategoryItemRepository categoryItemRepository;
  private static final String BASE_URL = "/api/v1/report-categoryItems/";

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
    String itemRequest =
        """
    {
        "name": "report-categoryItem-name",
        "plannedAmount": 10000.00,
        "itemCategory": "NEEDS"
    }
        """;

    MvcResult result =
        mockMvc
            .perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(itemRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").isString())
            .andExpect(jsonPath("$.name", is("report-categoryItem-name")))
            .andExpect(jsonPath("$.plannedAmount", is(10000.00)))
            .andExpect(jsonPath("$.realAmount", is(0)))
            .andExpect(jsonPath("$.difference", is(10000.00)))
            .andExpect(jsonPath("$.itemCategory", is("NEEDS")))
            .andDo(print())
            .andReturn();

    int startIndex = 7;
    UUID itemId =
        UUID.fromString(
            result.getResponse().getContentAsString().substring(startIndex, startIndex + 36));

    mockMvc
        .perform(get(BASE_URL + itemId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isString())
        .andExpect(jsonPath("$.name", is("report-categoryItem-name")))
        .andExpect(jsonPath("$.plannedAmount", is(10000.00)))
        .andExpect(jsonPath("$.realAmount", is(0.00)))
        .andExpect(jsonPath("$.difference", is(10000.00)))
        .andExpect(jsonPath("$.itemCategory", is("NEEDS")))
        .andDo(print());

    String itemUpdateRequest =
        """
    {
        "name": "UPDATED-report-categoryItem-name",
        "plannedAmount": 10000.00,
        "itemCategory": "NEEDS"
    }
       """;

    mockMvc
        .perform(
            put(BASE_URL + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemUpdateRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("UPDATED-report-categoryItem-name")))
        .andDo(print());

    mockMvc
        .perform(delete(BASE_URL + itemId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void getAllReportItems_shouldReturnListOfReportItemDtos() throws Exception {

    for (int i = 0; i < 5; i++) {
      categoryItemRepository.save(getReportItemDto(i).toEntity());
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

  private CategoryItemDto getReportItemDto(int number) {
//    return CategoryItemDto.builder()
//        .name("name - " + number)
//        .plannedAmount(BigDecimal.valueOf(number))
//        .category(new Category().toDto())
//        .build();
    return null;
  }
}
