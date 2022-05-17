package com.example.ecommerce.config;

import com.example.ecommerce.models.UserAccount;
import com.example.ecommerce.repositories.UserAccountRepository;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class MyAppUserDetails implements UserDetailsService {

  @Autowired
  UserAccountRepository userAccountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount userAccount = userAccountRepository.findByUsername(username); // username is email

    return new User(userAccount.getUsername(), userAccount.getPassword(), new ArrayList<>()); // NOT USER MODEL!!!
    //array list is the authorities (roles???)
  }
}
