package com.gmanzano.handler;

import static com.gmanzano.util.Constants.INPUT_CURRENCY_FIELD;
import static com.gmanzano.util.Constants.OUTPUT_CURRENCY_FIELD;

import com.gmanzano.exchangerate.response.ErrorResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * ValidateFieldsAdvice.
 * Handler para ValidateFields
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@ControllerAdvice
@Slf4j
public class ValidateFieldsAdvice extends ResponseEntityExceptionHandler {

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers,
      HttpStatus status, WebRequest request) {
    log.error("hidden error => " + ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String message = error.getDefaultMessage();
      errors.put(fieldName, message);
    });
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(100)
        .message("Incorrect data has been sent, please verify.")
        .detail(errors)
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * Metodo handleConstraintViolation.
   * Handler para validar las llaves foraneas
   *
   * @return ResponseEntity
   */
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<Object> handleConstraintViolation(DataIntegrityViolationException ex) {
    log.error("hidden error => " + ex.getMessage());
    Map<String, String> errors = new HashMap<>();
    if (ex.getCause() instanceof ConstraintViolationException) {
      ConstraintViolationException detailEx = (ConstraintViolationException) ex.getCause();
      if (detailEx.getCause().getLocalizedMessage().contains(INPUT_CURRENCY_FIELD)) {
        errors.put(INPUT_CURRENCY_FIELD, "inputCurrency value does not exist.");
      }
      if (detailEx.getCause().getLocalizedMessage().contains(OUTPUT_CURRENCY_FIELD)) {
        errors.put(OUTPUT_CURRENCY_FIELD, "outputCurrency value does not exist.");
      }
    }
    ErrorResponse errorResponse = ErrorResponse.builder()
        .code(100)
        .message("Incorrect data has been sent, please verify.")
        .detail(errors)
        .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

}
