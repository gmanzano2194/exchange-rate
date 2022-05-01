package com.gmanzano.handler;

import com.gmanzano.exception.CurrencyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CurrencyNotFoundAdvice.
 * Handler para CurrencyNotFound
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class CurrencyNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(CurrencyNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String currencyNotFoundHandler(CurrencyNotFoundException ex) {
    return ex.getMessage();
  }
}
