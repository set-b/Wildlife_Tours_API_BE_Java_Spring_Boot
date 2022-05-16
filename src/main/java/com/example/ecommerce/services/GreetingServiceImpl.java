package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Greeting;
import com.example.ecommerce.repositories.GreetingRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * This class contains the methods that are called by the GreetingController, and implemented from
 * the GreetingService interface.
 */
@Service
public class GreetingServiceImpl implements GreetingService {

  private final Logger logger = LoggerFactory.getLogger(GreetingService.class);

  @Autowired
  private GreetingRepository greetingRepository;

  /**
   * Retrieves greetings by the queries of the user.
   *
   * @param greeting the Greeting data to be queried through the database.
   * @return a list of Greetings if the query parameter is not included, or a list with the
   * Greeting(s) that match the query.
   */
  @Override
  public List<Greeting> queryGreetings(Greeting greeting) {
    try {
      if (greeting.isEmpty()) {
        List<Greeting> greetings = greetingRepository.findAll();
        greetings.sort(Comparator.comparing(Greeting::getId));
        return greetings;
      } else {
        Example<Greeting> greetingExample = Example.of(greeting);
        return greetingRepository.findAll(greetingExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Retrieves a Greeting that matches the id supplied by the user.
   *
   * @param id the id of the customer to be returned.
   * @return a Greeting object with the given id.
   */
  @Override
  public Greeting getGreetingById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Greeting greetingLookUpResult;
    try {
      greetingLookUpResult = greetingRepository.findById(id).orElse(null);
      if (greetingLookUpResult != null) {
        return greetingLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " greeting with id " + id);
  }

  /**
   * Adds a Greeting object to the database.
   *
   * @param greeting the Greeting to be added to the database.
   * @return the Greeting object saved to the database through the repository.
   */
  @Override
  public Greeting postGreeting(Greeting greeting) {
    try {
      return greetingRepository.save(greeting);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  /**
   * Finds a Greeting Object via the id supplied by the user, and replaces its data with the data
   * given.
   *
   * @param id       the id of the Greeting Object to be replaced/updated.
   * @param greeting the data which will replace the exiting dta of the Greeting Object.
   * @return the Greeting Object with updated data.
   */
  @Override
  public Greeting updateGreetingById(Long id, Greeting greeting) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Greeting updatedGreeting = null;
    if (!greetingRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "greeting with id " + id);
    }
    try {
      greeting.setId(id);
      updatedGreeting = greetingRepository.save(greeting);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedGreeting;
  }

  /**
   * Deletes a Greeting from the database given the id supplied by the user.
   *
   * @param id the id of the Greeting Object to be deleted.
   */
  @Override
  public void deleteGreetingById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getGreetingById(id);
    try {
      greetingRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
