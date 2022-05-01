package com.gmanzano.exchangerate.repository;

import com.gmanzano.exchangerate.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * EchangeRateRepository.
 * Repository de la entidad EchangeRate
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

  @Query(value = "SELECT ex.exchange_rate_id, ex.description, ex.input_currency, "
      + "ex.output_currency, ex.rate, ex.create_date, ex.create_user, ex.edit_date, "
      + "ex.edit_user, ex.status "
      + "FROM public.exchange_rate ex "
      + "WHERE ex.input_currency = ?1 AND ex.output_currency = ?2", nativeQuery = true)
  ExchangeRate findExchangeRateByInputCurrencyAndOutputCurrencyById(Long inputCurrency, Long outputCurrency);
}
