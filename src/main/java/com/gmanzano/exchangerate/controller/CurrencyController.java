package com.gmanzano.exchangerate.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.gmanzano.exception.CurrencyNotFoundException;
import com.gmanzano.exchangerate.dto.CurrencyDto;
import com.gmanzano.exchangerate.entity.Currency;
import com.gmanzano.exchangerate.entity.CurrencyModelAssembler;
import com.gmanzano.exchangerate.entity.Status;
import com.gmanzano.exchangerate.repository.CurrencyRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
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
 * CurrencyController.
 * Controlador de Currencies
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@RestController
public class CurrencyController {

  @Autowired
  private CurrencyRepository currencyRepository;
  @Autowired
  private CurrencyModelAssembler currencyModelAssembler;

  /**
   * Metodo all.
   * Retorna todos los currencies
   *
   * @return CollectionModel
   */
  @GetMapping("/currencies")
  public CollectionModel<EntityModel<Currency>> all() {
    List<EntityModel<Currency>> currencies = currencyRepository.findAll().stream()
        .map(currencyModelAssembler::toModel)
        .collect(Collectors.toList());
    return CollectionModel.of(currencies,
        linkTo(methodOn(CurrencyController.class).all()).withSelfRel());
  }

  /**
   * Metodo newCurrency.
   * Crea un nuevo currency
   *
   * @return ResponseEntity
   */
  @PostMapping("/currencies")
  public ResponseEntity<Object> newCurrency(@Valid @RequestBody CurrencyDto currencyDto) {
    UserDetails user = (UserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Currency newCurrency = Currency.builder()
        .name(currencyDto.getName())
        .symbol(currencyDto.getSymbol())
        .status(Status.ACTIVE)
        .createUser(user.getUsername())
        .createDate(new Date())
        .build();
    EntityModel<Currency> entityModel = currencyModelAssembler.toModel(currencyRepository
        .save(newCurrency));
    return ResponseEntity
        .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
        .body(entityModel);
  }

  /**
   * Metodo one.
   * Obtiene un currency segun el id enviado
   *
   * @return EntityModel
   */
  @GetMapping("/currencies/{id}")
  public EntityModel<Currency> one(@PathVariable Long id) {
    Currency currency = currencyRepository.findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException(id));
    return currencyModelAssembler.toModel(currency);
  }

  /**
   * Metodo replaceCurrency.
   * Actualiza un currency
   *
   * @return ResponseEntity
   */
  @PutMapping("/currencies/{id}")
  public ResponseEntity<Object> replaceCurrency(@RequestBody CurrencyDto currencyDto,
                                           @PathVariable Long id) {
    UserDetails user = (UserDetails) SecurityContextHolder.getContext()
        .getAuthentication().getPrincipal();
    Currency updatedCurrency = currencyRepository.findById(id)
        .map(currency -> {
          currency.setName(currencyDto.getName());
          currency.setSymbol(currencyDto.getSymbol());
          currency.setEditUser(user.getUsername());
          currency.setEditDate(new Date());
          return currencyRepository.save(currency);
        })
        .orElseGet(() -> {
          Currency newCurrency = Currency.builder()
              .name(currencyDto.getName())
              .symbol(currencyDto.getSymbol())
              .status(Status.ACTIVE)
              .createUser(user.getUsername())
              .createDate(new Date())
              .build();
          newCurrency.setCurrencyId(id);
          return currencyRepository.save(newCurrency);
        });
    EntityModel<Currency> entityModel = currencyModelAssembler.toModel(updatedCurrency);
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
  @DeleteMapping("/currencies/{id}/")
  public ResponseEntity<Object> deleteCurrency(@PathVariable Long id) {
    currencyRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  /**
   * Metodo activateCurrency.
   * Activa un currency
   *
   * @return ResponseEntity
   */
  @PatchMapping("/currencies/{id}/activate")
  public ResponseEntity<Object> activateCurrency(@PathVariable Long id) {
    Currency currency = currencyRepository.findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException(id));
    if (currency.getStatus() == Status.INACTIVE) {
      currency.setStatus(Status.ACTIVE);
      return ResponseEntity.ok(currencyModelAssembler.toModel(currencyRepository.save(currency)));
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
   * Metodo deactivateCurrency.
   * Desactiva un currency
   *
   * @return ResponseEntity
   */
  @PatchMapping("/currencies/{id}/deactivate")
  public ResponseEntity<Object> deactivateCurrency(@PathVariable Long id) {
    Currency currency = currencyRepository.findById(id)
        .orElseThrow(() -> new CurrencyNotFoundException(id));
    if (currency.getStatus() == Status.ACTIVE) {
      currency.setStatus(Status.INACTIVE);
      return ResponseEntity.ok(currencyModelAssembler.toModel(currencyRepository.save(currency)));
    }
    return ResponseEntity
        .status(HttpStatus.METHOD_NOT_ALLOWED)
        .header(HttpHeaders.CONTENT_TYPE, MediaTypes.HTTP_PROBLEM_DETAILS_JSON_VALUE)
        .body(Problem.create()
            .withTitle("Method not allowed")
            .withDetail("You can't activate a currency that is in the "
                + Status.INACTIVE + " status"));
  }
}
