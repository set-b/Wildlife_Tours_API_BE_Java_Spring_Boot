package com.example.ecommerce.security;

import com.example.ecommerce.config.MyAppUserDetailsService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

  @Autowired
  JwtUtils jwtUtils;

  MyAppUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String authorizationHeaderValue = request.getHeader("Authorization");
    String jwt = null;
    String userName = null; //username??
    if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer ")) {
      jwt = authorizationHeaderValue.substring(7);
      userName = jwtUtils.extractUsername(jwt); //extract rolename??
    }
    if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

      if (jwtUtils.validateToken(jwt, userDetails)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()); //roles are authorities
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(
            authentication); //authentication authorities from userdetails will determine method access
      }
    }

    filterChain.doFilter(request, response);
  }
}
