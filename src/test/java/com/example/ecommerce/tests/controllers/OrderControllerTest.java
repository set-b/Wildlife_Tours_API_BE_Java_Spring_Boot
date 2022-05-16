package com.example.ecommerce.tests.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_ORDERS;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.ecommerce.models.Order;
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
 * Contains the integration tests for the OrderController.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class OrderControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private MockMvc mockMvc;

  Order order;

  ResultMatcher createdStatus = MockMvcResultMatchers.status().isCreated();
  ResultMatcher okStatus = MockMvcResultMatchers.status().isOk();
  ResultMatcher deletedStatus = MockMvcResultMatchers.status().isNoContent();
  ResultMatcher notFoundStatus = MockMvcResultMatchers.status().isNotFound();
  ResultMatcher expectedType = MockMvcResultMatchers.content()
      .contentType(MediaType.APPLICATION_JSON);
  ResultMatcher badRequestStatus = MockMvcResultMatchers.status().isBadRequest();

  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void getOrdersReturns200() throws Exception {
    mockMvc.perform(get(CONTEXT_ORDERS))
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(okStatus);
  }

  @Test
  public void getOrderByIdThatExistsReturnsOrderWithCorrectTypeAnd200() throws Exception {
    mockMvc.perform(get(CONTEXT_ORDERS + "/1"))
        .andExpect(jsonPath("$.date").value("2022-04-22"))
        .andExpect(expectedType).andExpect(okStatus);
  }

  @Test
  public void getOrderByIdThatDoesNotExistReturns404() throws Exception {
    mockMvc.perform(get(CONTEXT_ORDERS + "/999"))
        .andExpect(notFoundStatus);
  }

  @Test
  public void getOrderWithNegativeIdReturns400BadRequest() throws Exception {
    mockMvc.perform(get(CONTEXT_ORDERS + "/-1"))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postValidOrderReturns201Created() throws Exception {

    String json = """
        {
                "id": 1,
                "customerId": 1,
                "date": "2022-08-22",
                "items": [
                    {
                        "id": 1,
                        "productId": 1,
                        "quantity": 12
                    }
                ],
                "orderTotal": 23.99
        }""";

    mockMvc.perform(post(CONTEXT_ORDERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(createdStatus);
  }

  @Test
  public void postInvalidOrderJSONObjectReturns400BadRequest() throws Exception {

    String json = """
        {
           
        }""";

    mockMvc.perform(post(CONTEXT_ORDERS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @DirtiesContext
  @Test
  public void putOrderWithValidBodyAndExistingIdReturns200OK() throws Exception {

    String json = """
        {
                "id": 2,
                "customerId": 1,
                "date": "2022-04-22",
                "items": [
                    {
                        "id": 1,
                        "productId": 1,
                        "quantity": 10
                    }
                ],
                "orderTotal": 3.99
        }""";

    mockMvc.perform(put(CONTEXT_ORDERS + "/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(okStatus);
  }

  @DirtiesContext
  @Test
  public void putOrderWithValidBodyAndNonExistingIdReturns404NotFound() throws Exception {

    String json = """
        {
                "id": 999,
                "customerId": 1,
                "date": "2022-12-22",
                "items": [
                    {
                        "id": 1,
                        "productId": 1,
                        "quantity": 10
                    }
                ],
                "orderTotal": 3.99
        }""";

    mockMvc.perform(put(CONTEXT_ORDERS + "/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(notFoundStatus);
  }

  @Test
  public void putOrderWithNegativeIdReturns400BadRequest() throws Exception {

    String json = """
        {
                "id": -1,
                "customerId": 1,
                "date": "2022-04-22",
                "items": [
                    {
                        "id": 1,
                        "productId": 1,
                        "quantity": 10
                    }
                ],
                "orderTotal": 3.99
        }""";

    mockMvc.perform(put(CONTEXT_ORDERS + "/-1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @Test
  public void deleteReturns204NoContentAndStaysDeleted() throws Exception {

    mockMvc.perform(delete(CONTEXT_ORDERS + "/1"))
        .andExpect(deletedStatus);

    mockMvc.perform(get(CONTEXT_ORDERS + "/1"))
        .andExpect(notFoundStatus);
  }


}