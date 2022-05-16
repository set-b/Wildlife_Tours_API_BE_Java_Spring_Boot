package com.example.ecommerce.security;

public interface AuthService {

  AuthToken login(Credential credentials);
}
