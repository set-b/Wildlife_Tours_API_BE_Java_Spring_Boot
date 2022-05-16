package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.repositories.CustomerRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * This class contains the methods that are called by the CustomerController, and implemented from
 * the CustomerService interface.
 */
@Service
public class CustomerServiceImpl implements CustomerService {

  private final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  @Autowired
  private CustomerRepository customerRepository;

  @Override
  public List<Customer> queryCustomers(Customer customer) {
    try {
      if (customer.isEmpty()) {
        List<Customer> customers = customerRepository.findAll();
        customers.sort(Comparator.comparing(Customer::getId));
        return customers;
      } else {
        Example<Customer> customerExample = Example.of(customer);
        return customerRepository.findAll(customerExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Customer getCustomerById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Customer customerLookUpResult;
    try {
      customerLookUpResult = customerRepository.findById(id).orElse(null);
      if (customerLookUpResult != null) {
        return customerLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " customer with id " + id);
  }

  @Override
  public Customer addCustomer(Customer customer) {
    boolean emailAlreadyExists = customerRepository.existsByEmail(customer.getEmail());
    if (emailAlreadyExists) {
      throw new Conflict(" Email already in use!");
    }
    try {
      return customerRepository.save(customer);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public Customer updateCustomerById(Long id, Customer customer) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    Customer updatedCustomer = null;
    if (!customerRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "customer with id " + id);
    }
    boolean emailAlreadyExists = customerRepository.existsByEmail(customer.getEmail());
    if (emailAlreadyExists) {
      throw new Conflict(" Email already in use!");
    }
    try {
      customer.setId(id);
      updatedCustomer = customerRepository.save(customer);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedCustomer;
  }

  @Override
  public void deleteCustomerById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getCustomerById(id);
    try {
      customerRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
