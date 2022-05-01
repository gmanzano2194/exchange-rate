package com.gmanzano.exchangerate.repository;

import com.gmanzano.exchangerate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * EchangeRateRepository.
 * Repository de la entidad EchangeRate
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {
}
