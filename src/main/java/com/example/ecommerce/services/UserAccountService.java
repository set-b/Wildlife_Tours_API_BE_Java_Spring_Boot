package com.example.ecommerce.services;

import com.example.ecommerce.models.UserAccount;
import java.util.List;

public interface UserAccountService {

  List<UserAccount> queryUserAccounts(UserAccount user);

  UserAccount getUserAccountById(Long id);

  UserAccount addUserAccount(UserAccount user);

  UserAccount updateUserAccountById(Long id, UserAccount user);

  UserAccount findUserAccountByUserName(String userName);

  void deleteUserAccountById(Long id);

}
