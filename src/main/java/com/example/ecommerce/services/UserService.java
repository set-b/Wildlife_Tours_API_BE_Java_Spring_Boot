package com.example.ecommerce.services;

import com.example.ecommerce.models.User;
import java.util.List;

/**
 * This interface contains the methods which are implemented in the UserServiceImpl class.
 */
public interface UserService {

  List<User> queryUsers(User user);

  User getUserById(Long id);

  User addUser(User user);

  User updateUserById(Long id, User user);

  User findUserByEmail(String email);

  void deleteUserById(Long id);

}
