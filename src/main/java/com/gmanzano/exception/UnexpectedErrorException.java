package com.gmanzano.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * UnexpectedErrorException.
 * Excepcion personalizada para UnexpectedErrors
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Slf4j
public class UnexpectedErrorException extends RuntimeException {
  public UnexpectedErrorException() {
    log.error("An unexpected error occurred.");
  }
}
