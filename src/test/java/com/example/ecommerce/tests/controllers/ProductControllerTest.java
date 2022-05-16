package com.example.ecommerce.tests.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_PRODUCTS;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.ecommerce.models.Product;
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
 * Contains the integration tests for the ProductController.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class ProductControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private MockMvc mockMvc;

  Product product;

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
  public void getProductsReturns200() throws Exception {
    mockMvc.perform(get(CONTEXT_PRODUCTS))
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(okStatus);
  }

  @Test
  public void getProductByIdThatExistsReturnsProductWithCorrectTypeAnd200() throws Exception {
    mockMvc.perform(get(CONTEXT_PRODUCTS + "/4"))
        .andExpect(jsonPath("$.sku").value("FP-857"))
        .andExpect(expectedType).andExpect(okStatus);
  }

  @Test
  public void getProductByIdThatDoesNotExistReturns404() throws Exception {
    mockMvc.perform(get(CONTEXT_PRODUCTS + "/999"))
        .andExpect(notFoundStatus);
  }

  @Test
  public void getProductWithNegativeIdReturns400BadRequest() throws Exception {
    mockMvc.perform(get(CONTEXT_PRODUCTS + "/-1"))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postValidProductReturns201Created() throws Exception {

    String json = """
        {
                "sku": "SB-RP12",
                "type": "Candy",
                "name": "Frisk Fresh Breath Spray",
                "description": "Mint Flavor",
                "manufacturer": "Frisk Fresh",
                "price": 2.99
        }""";

    mockMvc.perform(post(CONTEXT_PRODUCTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(createdStatus);
  }

  @DirtiesContext
  @Test
  public void postNonUniqueSkuProductThrows409Conflict() throws Exception {

    String json = """
        {
                "sku": "CS4-956",
                "type": "Candy",
                "name": "Frisk Fresh Breath Spray",
                "description": "Mint Flavor",
                "manufacturer": "Frisk Fresh",
                "price": 2.99
        }""";

    mockMvc.perform(post(CONTEXT_PRODUCTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(conflictStatus);
  }

  @Test
  public void postInvalidProductJSONObjectReturns400BadRequest() throws Exception {

    String json = """
        {
           
        }""";

    mockMvc.perform(post(CONTEXT_PRODUCTS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @DirtiesContext
  @Test
  public void putProductWithValidBodyAndExistingIdReturns200OK() throws Exception {

    String json = """
        {
                "sku": "FA-W124",
                "type": "Alien Technology",
                "name": "Alien Blaster",
                "description": "A strange piece of alien technology. Maybe you can make it work?",
                "manufacturer": "Wastelands",
                "price": 1999.99
        }""";

    mockMvc.perform(put(CONTEXT_PRODUCTS + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(okStatus);
  }

  @DirtiesContext
  @Test
  public void putProductWithValidBodyAndNonExistingIdReturns404NotFound() throws Exception {

    String json = """
        {
                "sku": "FA-W124",
                "type": "Alien Technology",
                "name": "Alien Blaster",
                "description": "A strange piece of alien technology. Maybe you can make it work?",
                "manufacturer": "Wastelands",
                "price": 1999.99
        }""";

    mockMvc.perform(put(CONTEXT_PRODUCTS + "/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(notFoundStatus);
  }

  @Test
  public void putProductWithNegativeIdReturns400BadRequest() throws Exception {

    String json = """
        {
                "sku": "FA-W124",
                "type": "Alien Technology",
                "name": "Alien Blaster",
                "description": "A strange piece of alien technology. Maybe you can make it work?",
                "manufacturer": "Wastelands",
                "price": 1999.99
        }""";

    mockMvc.perform(put(CONTEXT_PRODUCTS + "/-1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @Test
  public void deleteReturns204NoContentAndStaysDeleted() throws Exception {

    mockMvc.perform(delete(CONTEXT_PRODUCTS + "/1"))
        .andExpect(deletedStatus);

    mockMvc.perform(get(CONTEXT_PRODUCTS + "/1"))
        .andExpect(notFoundStatus);
  }


}