package com.gmanzano.exchangerate.dto;

import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * CalculateAmountDto.
 * Dto para CalculateAmount
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class CalculateAmountDto {

  @NonNull
  @DecimalMin(value = "0", inclusive = false, message = "Input amount value must be greater than 0")
  private BigDecimal inputAmount;

  @NotNull
  @Min(value = 1, message = "Input currency must be greater than 0")
  private Long inputCurrency;

  @NotNull
  @Min(value = 1, message = "Output currency must be greater than 0")
  private Long outputCurrency;
}
