package com.gmanzano.exchangerate.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * CurrencyDto.
 * Dto para Currencies
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CurrencyDto {
  @NotBlank
  @Size(min = 1, max = 50, message = "Name must be at maximun 50 characters long")
  @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Name must contain only letters and numbers")
  private String name;

  @NotBlank
  @Size(min = 1, max = 5, message = "Symbol must be at maximun 5 characters long")
  private String symbol;
}
