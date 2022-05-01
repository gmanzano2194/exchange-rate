package com.gmanzano.handler;

import com.gmanzano.exchangerate.response.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * AuthorizationAdvice.
 * Handler para Authorization
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class AuthorizationAdvice {

  @ResponseBody
  @ExceptionHandler({IllegalArgumentException.class, ExpiredJwtException.class})
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  ResponseEntity<Object> unauthorizedHandler(Exception ex) {
    log.error("hidden error => " + ex.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(401)
        .message("Access token is invalid.")
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
  }

}
