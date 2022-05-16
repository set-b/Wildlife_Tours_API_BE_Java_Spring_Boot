package com.example.ecommerce.tests.controllers;

import static com.example.ecommerce.constants.StringConstants.CONTEXT_GREETINGS;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.ecommerce.models.Greeting;
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
 * Contains the integration tests for the GreetingController.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
class GreetingControllerTest {

  @Autowired
  private WebApplicationContext webApplicationContext;
  @Autowired
  private MockMvc mockMvc;

  Greeting greeting;

  ResultMatcher createdStatus = MockMvcResultMatchers.status().isCreated();
  ResultMatcher okStatus = MockMvcResultMatchers.status().isOk();
  ResultMatcher deletedStatus = MockMvcResultMatchers.status().isNoContent();
  ResultMatcher notFoundStatus = MockMvcResultMatchers.status().isNotFound();
  ResultMatcher expectedType = MockMvcResultMatchers.content()
      .contentType(MediaType.APPLICATION_JSON);
  ResultMatcher badRequestStatus = MockMvcResultMatchers.status().isBadRequest();
  ResultMatcher uniqueViolationStatus = MockMvcResultMatchers.status().is5xxServerError();

  @Before
  public void setup() throws Exception {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void getGreetingsReturns200() throws Exception {
    mockMvc.perform(get(CONTEXT_GREETINGS))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(okStatus);
  }

  @Test
  public void getGreetingByIdThatExistsReturnsGreetingWithCorrectTypeAnd200() throws Exception {
    mockMvc.perform(get(CONTEXT_GREETINGS + "/1"))
        .andExpect(jsonPath("$.text").value("hello"))
        .andExpect(expectedType).andExpect(okStatus);
  }

  @Test
  public void getGreetingByIdThatDoesNotExistReturns404() throws Exception {
    mockMvc.perform(get(CONTEXT_GREETINGS + "/999"))
        .andExpect(notFoundStatus);
  }

  @Test
  public void getGreetingWithNegativeIdReturns400BadRequest() throws Exception {
    mockMvc.perform(get(CONTEXT_GREETINGS + "/-1"))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postValidGreetingReturns201Created() throws Exception {

    String json = """
        {
            "text": "how are you?"
        }""";

    mockMvc.perform(post(CONTEXT_GREETINGS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(createdStatus);
  }

  @Test
  public void postNonUniqueGreetingReturns503UniqueViolationConstraint() throws Exception {

    String json = """
        {
            "text": "hello"
        }""";

    mockMvc.perform(post(CONTEXT_GREETINGS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(uniqueViolationStatus);
  }

  @Test
  public void postGreetingWithOnlyOneCharReturns400BadRequest() throws Exception {

    String json = """
        {
            "text": "h"
        }""";

    mockMvc.perform(post(CONTEXT_GREETINGS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postNonStringGreetingReturns400BadRequest() throws Exception {

    String json = """
        {
            "text": 12468465
        }""";

    mockMvc.perform(post(CONTEXT_GREETINGS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @Test
  public void postEmptyGreetingJSONObjectReturns400BadRequest() throws Exception {

    String json = """
        {
           
        }""";

    mockMvc.perform(post(CONTEXT_GREETINGS)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(badRequestStatus);
  }

  @DirtiesContext
  @Test
  public void putGreetingWithValidBodyAndExistingIdReturns200OK() throws Exception {

    String json = """
        {
           "text": "this greeting has been updated"
        }""";

    mockMvc.perform(put(CONTEXT_GREETINGS + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(okStatus);
  }

  @DirtiesContext
  @Test
  public void putGreetingWithValidBodyAndNonExistingIdReturns404NotFound() throws Exception {

    String json = """
        {
           "text": "this greeting has been updated"
        }""";

    mockMvc.perform(put(CONTEXT_GREETINGS + "/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(notFoundStatus);
  }

  @Test
  public void deleteReturns204NoContentAndStaysDeleted() throws Exception {

    mockMvc.perform(delete(CONTEXT_GREETINGS + "/1"))
        .andExpect(deletedStatus);

    mockMvc.perform(get(CONTEXT_GREETINGS + "/1"))
        .andExpect(notFoundStatus);
  }


}