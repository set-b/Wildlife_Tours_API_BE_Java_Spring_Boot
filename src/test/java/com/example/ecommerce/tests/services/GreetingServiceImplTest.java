package com.example.ecommerce.tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Greeting;
import com.example.ecommerce.repositories.GreetingRepository;
import com.example.ecommerce.services.GreetingService;
import com.example.ecommerce.services.GreetingServiceImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Example;

/**
 * Contains the unit tests for the GreetingServiceImpl.
 */
class GreetingServiceImplTest {

  @Mock
  private GreetingRepository greetingRepository;
  @Mock
  private GreetingService greetingService;
  private AutoCloseable closeable;

  @InjectMocks
  GreetingServiceImpl greetingServiceImpl;

  Greeting greeting;
  List<Greeting> greetingList = new ArrayList<>();

  @BeforeEach
  public void setUp() throws Exception {
    closeable = MockitoAnnotations.openMocks(this);
    greeting = new Greeting("test");
  }

  @AfterEach
  public void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  public void queryGreetingsReturnsCorrectGreetings() {
    when(greetingRepository.findAll()).thenReturn(greetingList);
    List<Greeting> result = greetingServiceImpl.queryGreetings(new Greeting());
    assertEquals(greetingList, result);
  }

  @Test
  public void queryGreetingsByExample() {
    when(greetingRepository.findAll(any(Example.class))).thenReturn(greetingList);
    List<Greeting> result = greetingServiceImpl.queryGreetings(greeting);
    assertEquals(greetingList, result);
  }

  @Test
  public void queryGreetingsThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(greetingRepository).findAll();
    assertThrows(ServiceUnavailable.class,
        () -> greetingServiceImpl.queryGreetings(new Greeting()));
  }

  @Test
  public void getGreetingByExistingIdReturnsGreeting() {
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.of(greeting));
    Greeting result = greetingServiceImpl.getGreetingById(1L);
    assertEquals(greeting, result);
  }

  @Test
  public void getGreetingByNonExistentIdThrowsNotFound() {
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> greetingServiceImpl.getGreetingById(999L));
  }

  @Test
  public void getGreetingByNegativeIdThrowsBadDataResponse() {
    when(greetingRepository.findById(any(Long.class))).thenThrow(BadDataResponse.class);
    assertThrows(BadDataResponse.class, () -> greetingServiceImpl.getGreetingById(-18L));
  }

  @Test
  public void getGreetingByIdThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(greetingRepository).findById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> greetingServiceImpl.getGreetingById(1L));
  }

  @Test
  public void postGreetingWithValidBodyCreatesSuccessfully() {
    when(greetingRepository.save(any(Greeting.class))).thenReturn(greeting);
    Greeting result = greetingServiceImpl.postGreeting(new Greeting());
    assertEquals(greeting, result);
  }

  @Test
  public void postGreetingThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(greetingRepository).save(any(Greeting.class));
    assertThrows(ServiceUnavailable.class,
        () -> greetingServiceImpl.postGreeting(new Greeting()));
  }

  @Test
  public void updateGreetingWithValidIdAndBodyReturnsGreetingSuccessfully() {
    when(greetingRepository.existsById(any(Long.class))).thenReturn(true);
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.of(greeting));
    when(greetingRepository.save(any(Greeting.class))).thenReturn(greeting);
    Greeting result = greetingServiceImpl.updateGreetingById(1L, new Greeting());
    assertEquals(greeting, result);
  }

  @Test
  public void updateGreetingWithNonExistentIdThrowsNotFound() {
    when(greetingRepository.existsById(any(Long.class))).thenReturn(false);
    assertThrows(ResourceNotFound.class,
        () -> greetingServiceImpl.updateGreetingById(999L, new Greeting()));
  }

  @Test
  public void updateGreetingWithNegativeIdThrowsBadDataResponse() {
    assertThrows(BadDataResponse.class,
        () -> greetingServiceImpl.updateGreetingById(-8L, new Greeting()));
  }

  @Test
  public void updateGreetingThrowsServiceUnavailable() {
    when(greetingRepository.existsById(any(Long.class))).thenReturn(true);
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.of(greeting));
    doThrow(ServiceUnavailable.class).when(greetingRepository).save(any(Greeting.class));
    assertThrows(ServiceUnavailable.class,
        () -> greetingServiceImpl.updateGreetingById(1L, new Greeting()));
  }

  @Test
  public void deleteGreetingByExistentIdReturns204NoContent() {
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.of(greeting));
    greetingServiceImpl.deleteGreetingById(1L);
    verify(greetingRepository).deleteById(any());
  }

  @Test
  public void deleteGreetingByNonExistentIdThrows404NotFound() {
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> greetingServiceImpl.deleteGreetingById(999L));
  }

  @Test
  public void deleteGreetingByNegativeIdThrows400BadDataResponse() {
    assertThrows(BadDataResponse.class, () -> greetingServiceImpl.deleteGreetingById(-9L));
  }

  @Test
  public void deleteGreetingThrowsServiceUnavailable() {
    when(greetingRepository.existsById(any(Long.class))).thenReturn(true);
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.of(greeting));
    doThrow(ServiceUnavailable.class).when(greetingRepository).deleteById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> greetingServiceImpl.deleteGreetingById(1L));
  }

  @Test
  public void deleteEmptyGreetingThrowsResourceNotFound() {
    when(greetingRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> greetingServiceImpl.deleteGreetingById(40L));
  }
}