package com.gmanzano.handler;

import com.gmanzano.exception.UnexpectedErrorException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * UnexpectedErrorAdvice.
 * Handler para Unexpected Errors
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class UnexpectedErrorAdvice {
  @ResponseBody
  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  String inputOutputHandler(UnexpectedErrorException ex) {
    return ex.getMessage();
  }
}
