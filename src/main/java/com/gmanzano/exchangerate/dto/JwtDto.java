package com.gmanzano.exchangerate.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JwtDto.
 * Dto para Jwt
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtDto {
  private String username;
  private String password;
}
