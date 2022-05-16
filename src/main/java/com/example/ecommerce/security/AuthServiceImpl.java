package com.example.ecommerce.security;

import static com.example.ecommerce.constants.StringConstants.EMPLOYEE_ROLE_TYPE;
import static com.example.ecommerce.constants.StringConstants.INVALID_EMAIL_PASSWORD;
import static com.example.ecommerce.constants.StringConstants.ISSUER;
import static com.example.ecommerce.constants.StringConstants.MANAGER_ROLE_TYPE;
import static com.example.ecommerce.constants.StringConstants.ROLES_ATTRIBUTE;
import static com.example.ecommerce.constants.StringConstants.SECRET_KEY;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.ecommerce.exceptions.BadRequest;
import com.example.ecommerce.models.User;
import com.example.ecommerce.services.UserService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public AuthServiceImpl(UserService userservice, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userservice;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  private static boolean userRoleManagerPermissions(String role) {
    return role.equals(MANAGER_ROLE_TYPE);
  }

  @Override
  public AuthToken login(Credential credentials) {
    if (credentials.getEmail() == null || credentials.getPassword() == null) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }

    String email = credentials.getEmail();
    String password = credentials.getPassword();

    User user = userService.findUserByEmail(email); // might take out find

    if (user == null) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }

    String userPassword = user.getPassword();

    if (!bCryptPasswordEncoder.matches(password, userPassword)) {
      throw new BadRequest(INVALID_EMAIL_PASSWORD);
    }

    String userRole = EMPLOYEE_ROLE_TYPE;

    if (userRoleManagerPermissions(userRole)) {
      userRole = MANAGER_ROLE_TYPE;
    }

    Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
    String jwtToken =
        JWT.create()
            .withIssuer(ISSUER)
            .withClaim(ROLES_ATTRIBUTE, userRole)
            .withSubject(email)
            .withIssuedAt(new Date())
            .sign(algorithm);

    return new AuthToken(jwtToken);
  }
}
