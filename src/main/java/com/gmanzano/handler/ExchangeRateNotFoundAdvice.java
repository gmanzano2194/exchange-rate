package com.gmanzano.handler;

import com.gmanzano.exception.ExchangeRateNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * ExchangeRateNotFoundAdvice.
 * Handler para ExchangeRateNotFound
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class ExchangeRateNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(ExchangeRateNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String exchangeRateNotFoundHandler(ExchangeRateNotFoundException ex) {
    return ex.getMessage();
  }

}
