package com.mm.accountstatementparser.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mm.accountstatementparser.dto.TransactionDto;
import com.mm.accountstatementparser.entity.TransactionMainCategory;
import com.mm.accountstatementparser.repository.TransactionRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Currency;
import java.util.UUID;
import org.junit.jupiter.api.*;
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
public class TransactionControllerTest {
  @Container
  private static final PostgreSQLContainer<?> postgresContainer =
      new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
          .withUsername("postgres")
          .withPassword("mysecretpassword")
          .withDatabaseName("postgres");

  @Autowired private MockMvc mockMvc;
  @Autowired private TransactionRepository transactionRepository;
  private static final String BASE_URL = "/api/v1/transactions/";

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
  public void createGetByIdUpdateDeleteTransaction() throws Exception {
    String transactionRequest =
        """
{
    "date": "2023-11-11",
    "amount": 12340.12,
    "currency": "CZK",
    "transactionMainCategory": "NEEDS"
}
""";

    MvcResult result =
        mockMvc
            .perform(
                post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(transactionRequest))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.transactionId").isString())
            .andExpect(jsonPath("$.date", is("2023-11-11")))
            .andExpect(jsonPath("$.amount", is(12340.12)))
            .andExpect(jsonPath("$.currency", is("CZK")))
            .andExpect(jsonPath("$.transactionMainCategory", is("NEEDS")))
            .andDo(print())
            .andReturn();

    int startIndex = 18;
    UUID transactionId =
        UUID.fromString(result.getResponse().getContentAsString().substring(startIndex, startIndex + 36));

    mockMvc
        .perform(get(BASE_URL + transactionId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.transactionId").isString())
        .andExpect(jsonPath("$.date", is("2023-11-11")))
        .andExpect(jsonPath("$.amount", is(12340.12)))
        .andExpect(jsonPath("$.currency", is("CZK")))
        .andExpect(jsonPath("$.transactionMainCategory", is("NEEDS")))
        .andDo(print());

    String transactionUpdateRequest =
        """
    {
        "date": "2023-11-11",
        "amount": 12340.12,
        "currency": "CZK",
        "transactionNote": "updated-note",
        "transactionMainCategory": "NEEDS"
    }
    """;

    mockMvc
        .perform(
            put(BASE_URL + transactionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionUpdateRequest))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.transactionNote", is("updated-note")))
        .andDo(print());

    mockMvc
        .perform(delete(BASE_URL + transactionId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void getAllTransactions_shouldReturnListOfTransactionDtos() throws Exception {

    for (int i = 0; i < 5; i++) {
      transactionRepository.save(getTransactionDto(i).toEntity());
    }

    mockMvc
        .perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(5)))
        .andExpect(jsonPath("$[0].amount", is(0.0)))
        .andExpect(jsonPath("$[1].amount", is(1.0)))
        .andExpect(jsonPath("$[2].amount", is(2.0)))
        .andExpect(jsonPath("$[3].amount", is(3.0)))
        .andExpect(jsonPath("$[4].amount", is(4.0)));
  }

  private TransactionDto getTransactionDto(double amount) {
    return TransactionDto.builder()
        .date(LocalDate.parse("2023-11-11"))
        .amount(BigDecimal.valueOf(amount))
        .currency(Currency.getInstance("CZK"))
        .transactionMainCategory(TransactionMainCategory.NEEDS)
        .build();
  }
}
