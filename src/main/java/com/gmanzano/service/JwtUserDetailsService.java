package com.gmanzano.service;

import java.util.ArrayList;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JwtUserDetailsService.
 * Servicio para validar usuario
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Service
public class JwtUserDetailsService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if ("gmanzano".equals(username)) {
      return new User("gmanzano", "$2a$10$KlnhckNzxpVcdjUEyNPh/u.Se0b8RBYzOIi8A1EErw.L48gdYlmVq",
          new ArrayList<>());
    } else {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
  }
}
