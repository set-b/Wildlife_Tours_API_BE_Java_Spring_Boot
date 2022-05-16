package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.User;
import com.example.ecommerce.repositories.UserRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

/**
 * This class contains the methods that are called by the UserController, and implemented from the
 * UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

  private final Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<User> queryUsers(User user) {
    try {
      if (user.isEmpty()) {
        List<User> users = userRepository.findAll();
        users.sort(Comparator.comparing(User::getId));
        return users;
      } else {
        Example<User> userExample = Example.of(user);
        return userRepository.findAll(userExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public User getUserById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    User userLookUpResult;
    try {
      userLookUpResult = userRepository.findById(id).orElse(null);
      if (userLookUpResult != null) {
        return userLookUpResult;
      }
    } catch (Exception e) {
      throw new ServiceUnavailable(e.getMessage());
    }
    throw new ResourceNotFound(NOT_FOUND + " user with id " + id);
  }

  @Override
  public User addUser(User user) {

    boolean emailAlreadyExists = userRepository.existsByEmail(user.getEmail());

    if (emailAlreadyExists) {
      throw new Conflict(" Email already in use!");
    }
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public User updateUserById(Long id, User user) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    User updatedUser = null;
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "user with id " + id);
    }
    boolean emailAlreadyExists = userRepository.existsByEmail(user.getEmail());
    if (emailAlreadyExists) {
      throw new Conflict(" Email already in use!");
    }
    try {
      user.setId(id);
      updatedUser = userRepository.save(user);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedUser;
  }

  @Override
  public void deleteUserById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getUserById(id);
    try {
      userRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }
}
