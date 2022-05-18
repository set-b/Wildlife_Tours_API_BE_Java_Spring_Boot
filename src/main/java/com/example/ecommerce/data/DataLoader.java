package com.example.ecommerce.data;

import static com.example.ecommerce.constants.StringConstants.ADMIN;
import static com.example.ecommerce.constants.StringConstants.BASIC_USER;
import static com.example.ecommerce.constants.StringConstants.EMPLOYEE;

import com.example.ecommerce.models.Address;
import com.example.ecommerce.models.Contact;
import com.example.ecommerce.models.Customer;
import com.example.ecommerce.models.Tour;
import com.example.ecommerce.models.TourBooking;
import com.example.ecommerce.models.User;
import com.example.ecommerce.models.UserAccount;
import com.example.ecommerce.repositories.CustomerRepository;
import com.example.ecommerce.repositories.TourBookingRepository;
import com.example.ecommerce.repositories.TourRepository;
import com.example.ecommerce.repositories.UserAccountRepository;
import com.example.ecommerce.repositories.UserRepository;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * This class loads data into the database upon launching the application
 */
@Component
public class DataLoader implements CommandLineRunner {

  private final Logger logger = LoggerFactory.getLogger(DataLoader.class);

  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private PasswordEncoder bCryptPasswordEncoder;

  @Autowired
  private TourRepository tourRepository;

  @Autowired
  private TourBookingRepository tourBookingRepository;

  private Customer customer;
  private Customer customerTwo;
  private Customer customerThree;
  private Customer customerFour;

  private User user;
  private User userTwo;
  private User userThree;
  private User userFour;

  private Address addressOne = new Address("64 Zoo Lane", "Paris", "TX", "23592");
  private Address addressTwo = new Address("1125 Main Street", "Elwood city", "MA", "02532");
  private Address addressThree = new Address("124 Conch Street", "Bikini Bottom", "CA", "94103");
  private Address addressFour = new Address("30 Pixie way", "Dimmsdale", "CA", "90210");

  @Override
  public void run(String... args) throws Exception {
    loadTours();
    loadTourBookings();
    loadCustomers();
    loadUsers();
  }

  public Date createDate(String dateValue) {
    String datePattern = "yyyy-MM-dd";
    SimpleDateFormat dateFor = new SimpleDateFormat(datePattern);
    Date createdDate = null;
    try {
      createdDate = dateFor.parse(dateValue);
    } catch (Exception e) {
      throw new RuntimeException("Date could not be parsed");
    }
    return createdDate;
  }
  
  private void loadTours() {
    tourRepository.save(
        new Tour(10, new String[]{},
            "description", BigDecimal.valueOf(2099.99), 5, true));
    tourRepository.save(
        new Tour(10, new String[]{"TB"},
            "description", BigDecimal.valueOf(2099.99), 5, true));
  }

  private void loadTourBookings() {
    Contact contact = new Contact("Bobby", "Hill", "5128865120", "bhill@student.uml.edu");
    tourBookingRepository.save(new TourBooking(contact, "79927398713", "WUC-J7",
        createDate("2022-08-19"), 1L));
  }

  private void loadCustomers() {
    customer = customerRepository.save(
        new Customer("Lucy Vrombaut", "lvrombaut@hotmail.com", addressOne));
    customerTwo = customerRepository.save(
        new Customer("Arthur Reed", "a_reed@student.uml.edu", addressTwo));
    customerThree = customerRepository.save(
        new Customer("Spongebob Squarepants", "imready@krustykrab.com", addressThree));
    customerFour = customerRepository.save(
        new Customer("Timmy Turner", "timothy_turner@outlook.com", addressFour));

  }

  private void loadUsers() { //todo possibly replace user and customer with useraccount?
    user = userRepository.save(
        new User("Claire Redfield", "employee", EMPLOYEE, "credfield@ecommerce.com",
            "password12345"));
    userTwo = userRepository.save(
        new User("Colby Jack", "employee", EMPLOYEE, "cheesewizard@ecommerce.com",
            "pastrami25"));
    userThree = userRepository.save(
        new User("Duragin Fohrs", "Systems Administrator", ADMIN,
            "dfohrs@ecommerce.com", "theseventhseal1"));
    userFour = userRepository.save(
        new User("Michael Scott", "Boss", ADMIN, "mscott@ecommerce.com",
            "supersecretpassword1!"));

    String encodedPassword = bCryptPasswordEncoder.encode("password");
    UserAccount userAccount = userAccountRepository.save(
        new UserAccount("user", encodedPassword, true, ADMIN));
    UserAccount userAccountTwo = userAccountRepository.save(
        new UserAccount("userTwo", encodedPassword, true, EMPLOYEE));
    UserAccount userAccountThree = userAccountRepository.save(
        new UserAccount("user", encodedPassword, true, BASIC_USER));
  }


}
