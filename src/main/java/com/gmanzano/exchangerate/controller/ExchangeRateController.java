package com.gmanzano.exchangerate.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.gmanzano.exception.CurrencyNotFoundException;
import com.gmanzano.exception.ExchangeRateNotFoundException;
import com.gmanzano.exchangerate.dto.CalculateAmountDto;
import com.gmanzano.exchangerate.dto.ExchangeRateDto;
import com.gmanzano.exchangerate.entity.Currency;
import com.gmanzano.exchangerate.entity.ExchangeRate;
import com.gmanzano.exchangerate.entity.ExchangeRateModelAssembler;
import com.gmanzano.exchangerate.entity.Status;
import com.gmanzano.exchangerate.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.validation.Valid;

import com.gmanzano.exchangerate.response.CalculateAmountResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * ExchangeRateController.
 * Controlador de ExchangeRates
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@RestController
public class ExchangeRateController {

  @Autowired
  private ExchangeRateRepository exchangeRateRepository;
  @Autowired
  private ExchangeRateModelAssembler exchangeRateModelAssembler;

  /**
   * Metodo all.
   * Retorna todos los exchangeRATES
   *
   * @return CollectionModel
   */
  @GetMapping("/exchange-rates")
  public CollectionModel<EntityModel<ExchangeRate>> all() {
    List<EntityModel<ExchangeRate>> exchangeRates = exchangeRateRepository.findAll().stream()
        .map(exchangeRateModelAssembler::toModel)
        .collect(Collectors.toList());
    return CollectionModel.of(exchangeRates,
        linkTo(methodOn(ExchangeRateController.class).all()).withSelfRel());
  }

  /**
   * Metodo newExchangeRate.
   * Crea un nuevo exchangeRATE
   *
   * @return ResponseEntity
   */
  @PostMapping("/exchange-rates")
  public ResponseEntity<Object> newExchangeRate(
      @Valid @RequestBody ExchangeRateDto exchangeRateDto) {
    UserDetails user = (UserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Currency inputCurrency = Currency.builder()
        .currencyId(exchangeRateDto.getInputCurrency())
        .build();
    Currency outputCurrency = Currency.builder()
        .currencyId(exchangeRateDto.getOutputCurrency())
        .build();
    ExchangeRate newExchangeRate = ExchangeRate.builder()
        .description(exchangeRateDto.getDescription())
        .inputCurrency(inputCurrency)
        .outputCurrency(outputCurrency)
        .rate(exchangeRateDto.getRate())
        .status(Status.ACTIVE)
        .createUser(user.getUsername())
        .createDate(new Date())
        .build();
    EntityModel<ExchangeRate> entityModel = exchangeRateModelAssembler
        .toModel(exchangeRateRepository.save(newExchangeRate));
    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  /**
   * Metodo one.
   * Obtiene un exchangeRate segun el id enviado
   *
   * @return EntityModel
   */
  @GetMapping("/exchange-rates/{id}")
  public EntityModel<ExchangeRate> one(@PathVariable Long id) {
    ExchangeRate exchangeRate = exchangeRateRepository.findById(id)
        .orElseThrow(() -> new ExchangeRateNotFoundException(id));
    return exchangeRateModelAssembler.toModel(exchangeRate);
  }

  /**
   * Metodo replaceExchangeRate.
   * Actualiza un exchangeRate
   *
   * @return ResponseEntity
   */
  @PutMapping("/exchange-rates/{id}")
  public ResponseEntity<Object> replaceExchangeRate(@RequestBody ExchangeRateDto exchangeRateDto,
                                                    @PathVariable Long id) {
    UserDetails user = (UserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Currency inputCurrency = Currency.builder()
        .currencyId(exchangeRateDto.getInputCurrency())
        .build();
    Currency outputCurrency = Currency.builder()
        .currencyId(exchangeRateDto.getOutputCurrency())
        .build();
    ExchangeRate updatedExchangeRate = exchangeRateRepository.findById(id)
        .map(exchangeRate -> {
          exchangeRate.setDescription(exchangeRateDto.getDescription());
          exchangeRate.setInputCurrency(inputCurrency);
          exchangeRate.setOutputCurrency(outputCurrency);
          exchangeRate.setRate(exchangeRateDto.getRate());
          exchangeRate.setEditUser(user.getUsername());
          exchangeRate.setEditDate(new Date());
          return exchangeRateRepository.save(exchangeRate);
        })
        .orElseGet(() -> {
          ExchangeRate newExchangeRate = ExchangeRate.builder()
              .description(exchangeRateDto.getDescription())
              .inputCurrency(inputCurrency)
              .outputCurrency(outputCurrency)
              .rate(exchangeRateDto.getRate())
              .status(Status.ACTIVE)
              .createUser(user.getUsername())
              .createDate(new Date())
              .build();
          newExchangeRate.setExchangeRateId(id);
          return exchangeRateRepository.save(newExchangeRate);
        });
    EntityModel<ExchangeRate> entityModel = exchangeRateModelAssembler.toModel(updatedExchangeRate);
    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  /**
   * Metodo deleteCurrency.
   * Elimina un currency
   *
   * @return ResponseEntity
   */
  @DeleteMapping("/exchange-rates/{id}/")
  public ResponseEntity<Object> deleteExchangeRate(@PathVariable Long id) {
    exchangeRateRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Metodo activateExchangeRate.
   * Activa un exchangeate
   *
   * @return ResponseEntity
   */
  @PatchMapping("/exchange-rates/{id}/activate")
  public ResponseEntity<Object> activateExchangeRate(@PathVariable Long id) {
    ExchangeRate exchangeRate = exchangeRateRepository.findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException(id));
    if (exchangeRate.getStatus() == Status.INACTIVE) {
      exchangeRate.setStatus(Status.ACTIVE);
      return ResponseEntity.ok(exchangeRateModelAssembler
          .toModel(exchangeRateRepository.save(exchangeRate)));
    }
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(Problem.create()
            .withTitle("Method not allowed")
            .withDetail("You can't activate a currency that is in the "
                + Status.ACTIVE + " status"));
  }

  /**
   * Metodo deactivateExchangeRate.
   * Desactiva un exchangeRate
   *
   * @return ResponseEntity
   */
  @PatchMapping("/exchange-rates/{id}/deactivate")
  public ResponseEntity<Object> deactivateExchangeRate(@PathVariable Long id) {
    ExchangeRate exchangeRate = exchangeRateRepository.findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException(id));
    if (exchangeRate.getStatus() == Status.ACTIVE) {
      exchangeRate.setStatus(Status.INACTIVE);
      return ResponseEntity.ok(exchangeRateModelAssembler
          .toModel(exchangeRateRepository.save(exchangeRate)));
    }
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(Problem.create()
            .withTitle("Method not allowed")
            .withDetail("You can't activate an exchangeRate that is in the "
                + Status.INACTIVE + " status"));
  }

  /**
   * Metodo calculateAmount.
   * Calcula un monto de acuerdo al tipo de cambio
   *
   * @return ResponseEntity
   */
  @PostMapping("/exchange-rates/calculate-amount")
  public ResponseEntity<Object> calculateAmount(
      @Valid @RequestBody CalculateAmountDto calculateAmountDto) {
    CalculateAmountResponse response = null;
    ExchangeRate exchangeRate = exchangeRateRepository.findExchangeRateByInputCurrencyAndOutputCurrencyById(
        calculateAmountDto.getInputCurrency(), calculateAmountDto.getOutputCurrency()
    );
    if (Objects.nonNull(exchangeRate)) {
      Currency inputCurrency = Currency.builder()
          .name(exchangeRate.getInputCurrency().getName())
          .currencyId(exchangeRate.getInputCurrency().getCurrencyId())
          .build();
      Currency outputCurrency = Currency.builder()
          .name(exchangeRate.getOutputCurrency().getName())
          .currencyId(exchangeRate.getOutputCurrency().getCurrencyId())
          .build();
      ExchangeRate formattedExchangeRate = ExchangeRate.builder()
          .exchangeRateId(exchangeRate.getExchangeRateId())
          .inputCurrency(inputCurrency)
          .outputCurrency(outputCurrency)
          .rate(exchangeRate.getRate())
          .build();
      BigDecimal calculatedAmount = calculateAmountDto.getInputAmount().multiply(exchangeRate.getRate());
      String formattedResult = exchangeRate.getOutputCurrency().getSymbol().concat(calculatedAmount.toString());
      response = CalculateAmountResponse.builder()
          .amount(calculateAmountDto.getInputAmount())
          .calculatedAmount(formattedResult)
          .exchangeRate(formattedExchangeRate)
          .build();
    } else {
      throw new ExchangeRateNotFoundException(0L);
    }
    return ResponseEntity
        .ok()
        .body(response);
  }

}
