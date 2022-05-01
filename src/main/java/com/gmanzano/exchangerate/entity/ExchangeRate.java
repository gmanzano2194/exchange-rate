package com.gmanzano.exchangerate.entity;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ExchangeRate Entity.
 * Entidad de Monedas para el uso en Tipos de Cambio.
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Entity
@Table(name = "EXCHANGE_RATE")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRate {
  private @Id @GeneratedValue Long exchangeRateId;
  private String description;
  @ManyToOne
  @JoinColumn(name = "INPUT_CURRENCY")
  private Currency inputCurrency;
  @ManyToOne
  @JoinColumn(name = "OUTPUT_CURRENCY")
  private Currency outputCurrency;
  private BigDecimal rate;
  private Status status;
  private Date createDate;
  private String createUser;
  private Date editDate;
  private String editUser;
}
