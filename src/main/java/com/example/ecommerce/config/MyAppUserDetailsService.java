package com.example.ecommerce.config;

import com.example.ecommerce.models.UserAccount;
import com.example.ecommerce.repositories.UserAccountRepository;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyAppUserDetailsService implements UserDetailsService {

  @Autowired
  UserAccountRepository userAccountRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserAccount userAccount = userAccountRepository.findByUsername(username);

    List<GrantedAuthority> grantedAuthorities = List.of(
        new SimpleGrantedAuthority(userAccount.getRole()));
    return new User(userAccount.getUsername(), userAccount.getPassword(), grantedAuthorities);
  }
}
