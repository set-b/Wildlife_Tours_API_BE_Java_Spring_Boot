package com.example.ecommerce.tests.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Address;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import com.example.ecommerce.services.CustomerService;
import com.example.ecommerce.services.CustomerServiceImpl;
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
 * Contains the unit tests for the CustomerServiceImpl.
 */
class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;
  @Mock
  private CustomerService customerService;
  private AutoCloseable closeable;

  @InjectMocks
  CustomerServiceImpl customerServiceImpl;

  Customer customer;
  Address address;
  List<Customer> customerList = new ArrayList<>();

  @BeforeEach
  public void setUp() throws Exception {
    closeable = MockitoAnnotations.openMocks(this);
    address = new Address("1 Test Street", "Testown", "TX", "11111");
    customer = new Customer("Test Testerton", "test@test.com", address);
  }

  @AfterEach
  public void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  public void queryCustomersReturnsCorrectCustomers() {
    when(customerRepository.findAll()).thenReturn(customerList);
    List<Customer> result = customerServiceImpl.queryCustomers(new Customer());
    assertEquals(customerList, result);
  }

  @Test
  public void queryCustomersByExample() {
    when(customerRepository.findAll(any(Example.class))).thenReturn(customerList);
    List<Customer> result = customerServiceImpl.queryCustomers(customer);
    assertEquals(customerList, result);
  }

  @Test
  public void queryCustomersThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(customerRepository).findAll();
    assertThrows(ServiceUnavailable.class,
        () -> customerServiceImpl.queryCustomers(new Customer()));
  }

  @Test
  public void getCustomerByExistingIdReturnsCustomer() {
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    Customer result = customerServiceImpl.getCustomerById(1L);
    assertEquals(customer, result);
  }

  @Test
  public void getCustomerByNonExistentIdThrowsNotFound() {
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> customerServiceImpl.getCustomerById(999L));
  }

  @Test
  public void getCustomerByNegativeIdThrowsBadDataResponse() {
    when(customerRepository.findById(any(Long.class))).thenThrow(BadDataResponse.class);
    assertThrows(BadDataResponse.class, () -> customerServiceImpl.getCustomerById(-18L));
  }

  @Test
  public void getCustomerByIdThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(customerRepository).findById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> customerServiceImpl.getCustomerById(1L));
  }

  @Test
  public void postCustomerWithValidBodyCreatesSuccessfully() {
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);
    Customer result = customerServiceImpl.addCustomer(new Customer());
    assertEquals(customer, result);
  }

  @Test
  void postCustomerWithExistingEmailThrowsConflict409() {
    when(customerRepository.existsByEmail(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> customerServiceImpl.addCustomer(customer));
  }

  @Test
  public void postCustomerThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(customerRepository).save(any(Customer.class));
    assertThrows(ServiceUnavailable.class,
        () -> customerServiceImpl.addCustomer(new Customer()));
  }

  @Test
  public void updateCustomerWithValidIdAndBodyReturnsCustomerSuccessfully() {
    when(customerRepository.existsById(any(Long.class))).thenReturn(true);
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    when(customerRepository.save(any(Customer.class))).thenReturn(customer);
    Customer result = customerServiceImpl.updateCustomerById(1L, new Customer());
    assertEquals(customer, result);
  }

  @Test
  public void updateCustomerWithNonExistentIdThrowsNotFound() {
    when(customerRepository.existsById(any(Long.class))).thenReturn(false);
    assertThrows(ResourceNotFound.class,
        () -> customerServiceImpl.updateCustomerById(999L, new Customer()));
  }

  @Test
  public void updateCustomerWithNegativeIdThrowsBadDataResponse() {
    assertThrows(BadDataResponse.class,
        () -> customerServiceImpl.updateCustomerById(-8L, new Customer()));
  }

  @Test
  public void updateCustomerThrowsServiceUnavailable() {
    when(customerRepository.existsById(any(Long.class))).thenReturn(true);
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    doThrow(ServiceUnavailable.class).when(customerRepository).save(any(Customer.class));
    assertThrows(ServiceUnavailable.class,
        () -> customerServiceImpl.updateCustomerById(1L, new Customer()));
  }

  @Test
  public void updateCustomerWithEmailThatAlreadyExistsThrowsConflict409() {
    when(customerRepository.existsById(any(Long.class))).thenReturn(true);
    when(customerRepository.existsByEmail(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> customerServiceImpl.updateCustomerById(1L, customer));
  }

  @Test
  public void deleteCustomerByExistentIdReturns204NoContent() {
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    customerServiceImpl.deleteCustomerById(1L);
    verify(customerRepository).deleteById(any());
  }

  @Test
  public void deleteCustomerByNonExistentIdThrows404NotFound() {
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> customerServiceImpl.deleteCustomerById(999L));
  }

  @Test
  public void deleteCustomerByNegativeIdThrows400BadDataResponse() {
    assertThrows(BadDataResponse.class, () -> customerServiceImpl.deleteCustomerById(-9L));
  }

  @Test
  public void deleteCustomerThrowsServiceUnavailable() {
    when(customerRepository.existsById(any(Long.class))).thenReturn(true);
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.of(customer));
    doThrow(ServiceUnavailable.class).when(customerRepository).deleteById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> customerServiceImpl.deleteCustomerById(1L));
  }

  @Test
  public void deleteEmptyCustomerThrowsResourceNotFound() {
    when(customerRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> customerServiceImpl.deleteCustomerById(40L));
  }
}