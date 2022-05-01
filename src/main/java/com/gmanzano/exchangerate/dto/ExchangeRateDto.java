package com.gmanzano.exchangerate.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * ExchangeRateDto.
 * Dto para ExchangeRate
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ExchangeRateDto {
  @NotBlank
  @Size(min = 1, max = 100, message = "Description must be at maximun 100 characters long")
  @Pattern(regexp = "^[a-zA-Z0-9 ]*$",
      message = "Description must contain only letters and numbers")
  private String description;

  @NotNull
  @Min(value = 1, message = "Input currency must be greater than 0")
  private Long inputCurrency;

  @NotNull
  @Min(value = 1, message = "Output currency must be greater than 0")
  private Long outputCurrency;

  @NonNull
  @DecimalMin(value = "0", inclusive = false, message = "Rate value must be greater than 0")
  private BigDecimal rate;
}
