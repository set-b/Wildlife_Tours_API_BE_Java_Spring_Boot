package com.example.ecommerce.services;

import static com.example.ecommerce.constants.StringConstants.NOT_FOUND;

import com.example.ecommerce.exceptions.BadDataResponse;
import com.example.ecommerce.exceptions.Conflict;
import com.example.ecommerce.exceptions.ResourceNotFound;
import com.example.ecommerce.exceptions.ServiceUnavailable;
import com.example.ecommerce.models.UserAccount;
import com.example.ecommerce.repositories.UserAccountRepository;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

@Service
public class UserAccountServiceImpl implements UserAccountService {

  private final Logger logger = LoggerFactory.getLogger(UserAccountService.class);

  @Autowired
  private UserAccountRepository userRepository;

  @Override
  public List<UserAccount> queryUserAccounts(UserAccount user) {
    try {
      if (user.isEmpty()) {
        List<UserAccount> users = userRepository.findAll();
        users.sort(Comparator.comparing(UserAccount::getId));
        return users;
      } else {
        Example<UserAccount> userExample = Example.of(user);
        return userRepository.findAll(userExample);
      }
    } catch (Exception e) {
      logger.error(e.getMessage());
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public UserAccount getUserAccountById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    UserAccount userLookUpResult;
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
  public UserAccount addUserAccount(UserAccount user) {

    boolean emailAlreadyExists = userRepository.existsByUsername(user.getUsername());

    if (emailAlreadyExists) {
      throw new Conflict(" Username already in use!");
    }
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
  }

  @Override
  public UserAccount updateUserAccountById(Long id, UserAccount user) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    UserAccount updatedUserAccount = null;
    if (!userRepository.existsById(id)) {
      throw new ResourceNotFound(NOT_FOUND + "user with id " + id);
    }
    boolean userNameAlreadyExists = userRepository.existsByUsername(user.getUsername());
    if (userNameAlreadyExists) {
      throw new Conflict(" Username already in use!");
    }
    try {
      user.setId(id);
      updatedUserAccount = userRepository.save(user);
    } catch (Exception e) {
      throw new ServiceUnavailable(e);
    }
    return updatedUserAccount;
  }

  @Override
  public UserAccount findUserAccountByUserName(String userName) {
    UserAccount foundUserAccount = null;

    try {
      foundUserAccount = userRepository.findByUsername(userName);
    } catch (DataAccessException e) {
      logger.error(e.getMessage());
    }

    return foundUserAccount;
  }

  @Override
  public void deleteUserAccountById(Long id) {
    if (id < 1) {
      throw new BadDataResponse("id must be positive and cannot be zero");
    }
    getUserAccountById(id);
    try {
      userRepository.deleteById(id);
    } catch (Exception e) {
      throw new ServiceUnavailable("Something went wrong");
    }
  }

}
