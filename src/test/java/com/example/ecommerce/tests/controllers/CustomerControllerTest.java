package com.example.ecommerce.tests.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_CUSTOMERS;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.ecommerce.models.Customer;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Contains the integration tests for the CustomerController.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private MockMvc mockMvc;

  Customer customer;

  ResultMatcher createdStatus = MockMvcResultMatchers.status().isCreated();
  ResultMatcher okStatus = MockMvcResultMatchers.status().isOk();
  ResultMatcher deletedStatus = MockMvcResultMatchers.status().isNoContent();
  ResultMatcher notFoundStatus = MockMvcResultMatchers.status().isNotFound();
  ResultMatcher expectedType = MockMvcResultMatchers.content()
      .contentType(MediaType.APPLICATION_JSON);
  ResultMatcher badRequestStatus = MockMvcResultMatchers.status().isBadRequest();
  ResultMatcher conflictStatus = MockMvcResultMatchers.status().isConflict();

  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void getCustomersReturns200() throws Exception {
    mockMvc.perform(get(CONTEXT_CUSTOMERS))
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(okStatus);
  }

  @Test
  public void getCustomerByIdThatExistsReturnsCustomerWithCorrectTypeAnd200() throws Exception {
    mockMvc.perform(get(CONTEXT_CUSTOMERS + "/1"))
        .andExpect(jsonPath("$.name").value("Lucy Vrombaut"))
        .andExpect(expectedType).andExpect(okStatus);
  }

  @Test
  public void getCustomerByIdThatDoesNotExistReturns404() throws Exception {
    mockMvc.perform(get(CONTEXT_CUSTOMERS + "/999"))
        .andExpect(notFoundStatus);
  }

  @Test
  public void getCustomerWithNegativeIdReturns400BadRequest() throws Exception {
    mockMvc.perform(get(CONTEXT_CUSTOMERS + "/-1"))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postValidCustomerReturns201Created() throws Exception {

    String json = """
        {
                "name": "Boppo the clown",
                "email": "beebopdoowop@charter.net",
                "address": {
                    "street": "1258 N. Highland",
                    "city": "Los Angeles",
                    "state": "CA",
                    "zipcode": "90055"
                }
        }""";

    mockMvc.perform(post(CONTEXT_CUSTOMERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(createdStatus);
  }

  @DirtiesContext
  @Test
  public void postNonUniqueEmailCustomerThrows409Conflict() throws Exception {

    String json = """
        {
                "name": "Ima Fischer",
                "email": "a_reed@student.uml.edu",
                "address": {
                    "street": "12 Powers Road",
                    "city": "Eastford",
                    "state": "MA",
                    "zipcode": "01545"
                }
        }""";

    mockMvc.perform(post(CONTEXT_CUSTOMERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(conflictStatus);
  }

  @Test
  public void postInvalidCustomerJSONObjectReturns400BadRequest() throws Exception {

    String json = """
        {
           
        }""";

    mockMvc.perform(post(CONTEXT_CUSTOMERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @DirtiesContext
  @Test
  public void putCustomerWithValidBodyAndExistingIdReturns200OK() throws Exception {

    String json = """
        {
                "name": "Scatman John",
                "email": "dweebopbop@charter.net",
                "address": {
                    "street": "13 Beverly Park Circle",
                    "city": "Los Angeles",
                    "state": "CA",
                    "zipcode": "90014"
                }
        }""";

    mockMvc.perform(put(CONTEXT_CUSTOMERS + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(okStatus);
  }

  @DirtiesContext
  @Test
  public void putCustomerWithValidBodyAndNonExistingIdReturns404NotFound() throws Exception {

    String json = """
        {
                "name": "Uncle Pennybags",
                "email": "upennybags@aol.com",
                "address": {
                    "street": "1 Beverly Hills",
                    "city": "Los Angeles",
                    "state": "CA",
                    "zipcode": "90210"
                }
        }""";

    mockMvc.perform(put(CONTEXT_CUSTOMERS + "/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(notFoundStatus);
  }

  @Test
  public void putCustomerWithNegativeIdReturns400BadRequest() throws Exception {

    String json = """
        {
                "name": "Uncle Pennybags",
                "email": "upennybags@aol.com",
                "address": {
                    "street": "1 Beverly Hills",
                    "city": "Los Angeles",
                    "state": "CA",
                    "zipcode": "90210"
                }
        }""";

    mockMvc.perform(put(CONTEXT_CUSTOMERS + "/-1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @Test
  public void deleteReturns204NoContentAndStaysDeleted() throws Exception {

    mockMvc.perform(delete(CONTEXT_CUSTOMERS + "/1"))
        .andExpect(deletedStatus);

    mockMvc.perform(get(CONTEXT_CUSTOMERS + "/1"))
        .andExpect(notFoundStatus);
  }


}