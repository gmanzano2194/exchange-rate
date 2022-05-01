package com.gmanzano.exchangerate.entity;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Currency Entity.
 * Entidad de Monedas para el uso en Tipos de Cambio.
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@ToString
@Entity
@Table(name = "CURRENCY")
public class Currency {
  private @Id @GeneratedValue Long currencyId;
  private String name;
  private String symbol;
  private Status status;
  private Date createDate;
  private String createUser;
  private Date editDate;
  private String editUser;
}
