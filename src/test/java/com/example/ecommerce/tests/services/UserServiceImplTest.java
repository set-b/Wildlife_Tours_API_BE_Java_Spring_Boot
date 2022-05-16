package com.example.ecommerce.tests.services;

import static com.example.ecommerce.constants.StringConstants.EMPLOYEE;
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
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.services.UserService;
import com.example.ecommerce.services.UserServiceImpl;
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
 * Contains the unit tests for the UserServiceImpl.
 */
class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserService userService;
  private AutoCloseable closeable;

  @InjectMocks
  UserServiceImpl userServiceImpl;

  User user;
  List<User> userList = new ArrayList<>();

  @BeforeEach
  public void setUp() throws Exception {
    closeable = MockitoAnnotations.openMocks(this);
    user = new User("Test Testerton", "employee", EMPLOYEE,
        "test@test.com",
        "password12345");
  }

  @AfterEach
  public void tearDown() throws Exception {
    closeable.close();
  }

  @Test
  public void queryUsersReturnsCorrectUsers() {
    when(userRepository.findAll()).thenReturn(userList);
    List<User> result = userServiceImpl.queryUsers(new User());
    assertEquals(userList, result);
  }

  @Test
  public void queryUsersByExample() {
    when(userRepository.findAll(any(Example.class))).thenReturn(userList);
    List<User> result = userServiceImpl.queryUsers(user);
    assertEquals(userList, result);
  }

  @Test
  public void queryUsersThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(userRepository).findAll();
    assertThrows(ServiceUnavailable.class,
        () -> userServiceImpl.queryUsers(new User()));
  }

  @Test
  public void getUserByExistingIdReturnsUser() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    User result = userServiceImpl.getUserById(1L);
    assertEquals(user, result);
  }

  @Test
  public void getUserByNonExistentIdThrowsNotFound() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> userServiceImpl.getUserById(999L));
  }

  @Test
  public void getUserByNegativeIdThrowsBadDataResponse() {
    when(userRepository.findById(any(Long.class))).thenThrow(BadDataResponse.class);
    assertThrows(BadDataResponse.class, () -> userServiceImpl.getUserById(-18L));
  }

  @Test
  public void getUserByIdThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(userRepository).findById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> userServiceImpl.getUserById(1L));
  }

  @Test
  public void postUserWithValidBodyCreatesSuccessfully() {
    when(userRepository.save(any(User.class))).thenReturn(user);
    User result = userServiceImpl.addUser(new User());
    assertEquals(user, result);
  }

  @Test
  void postUserWithExistingEmailThrowsConflict409() {
    when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> userServiceImpl.addUser(user));
  }

  @Test
  public void postUserThrowsServiceUnavailable() {
    doThrow(ServiceUnavailable.class).when(userRepository).save(any(User.class));
    assertThrows(ServiceUnavailable.class,
        () -> userServiceImpl.addUser(new User()));
  }

  @Test
  public void updateUserWithValidIdAndBodyReturnsUserSuccessfully() {
    when(userRepository.existsById(any(Long.class))).thenReturn(true);
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    when(userRepository.save(any(User.class))).thenReturn(user);
    User result = userServiceImpl.updateUserById(1L, new User());
    assertEquals(user, result);
  }

  @Test
  public void updateUserWithNonExistentIdThrowsNotFound() {
    when(userRepository.existsById(any(Long.class))).thenReturn(false);
    assertThrows(ResourceNotFound.class,
        () -> userServiceImpl.updateUserById(999L, new User()));
  }

  @Test
  public void updateUserWithNegativeIdThrowsBadDataResponse() {
    assertThrows(BadDataResponse.class,
        () -> userServiceImpl.updateUserById(-8L, new User()));
  }

  @Test
  public void updateUserThrowsServiceUnavailable() {
    when(userRepository.existsById(any(Long.class))).thenReturn(true);
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    doThrow(ServiceUnavailable.class).when(userRepository).save(any(User.class));
    assertThrows(ServiceUnavailable.class,
        () -> userServiceImpl.updateUserById(1L, new User()));
  }

  @Test
  public void updateUserWithEmailThatAlreadyExistsThrowsConflict409() {
    when(userRepository.existsById(any(Long.class))).thenReturn(true);
    when(userRepository.existsByEmail(any(String.class))).thenReturn(true);
    assertThrows(Conflict.class, () -> userServiceImpl.updateUserById(1L, user));
  }

  @Test
  public void deleteUserByExistentIdReturns204NoContent() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    userServiceImpl.deleteUserById(1L);
    verify(userRepository).deleteById(any());
  }

  @Test
  public void deleteUserByNonExistentIdThrows404NotFound() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> userServiceImpl.deleteUserById(999L));
  }

  @Test
  public void deleteUserByNegativeIdThrows400BadDataResponse() {
    assertThrows(BadDataResponse.class, () -> userServiceImpl.deleteUserById(-9L));
  }

  @Test
  public void deleteUserThrowsServiceUnavailable() {
    when(userRepository.existsById(any(Long.class))).thenReturn(true);
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
    doThrow(ServiceUnavailable.class).when(userRepository).deleteById(any(Long.class));
    assertThrows(ServiceUnavailable.class, () -> userServiceImpl.deleteUserById(1L));
  }

  @Test
  public void deleteEmptyUserThrowsResourceNotFound() {
    when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());
    assertThrows(ResourceNotFound.class, () -> userServiceImpl.deleteUserById(40L));
  }
}