package com.gmanzano.exchangerate.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.gmanzano.exchangerate.controller.CurrencyController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * CurrencyModelAssembler.
 * Clase que permite armar el modelo de Currency
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Component
public class CurrencyModelAssembler implements RepresentationModelAssembler<Currency,
    EntityModel<Currency>> {

  @Override
  public EntityModel<Currency> toModel(Currency currency) {
    EntityModel<Currency> currencyModel = EntityModel.of(currency,
        linkTo(methodOn(CurrencyController.class).one(currency.getCurrencyId())).withSelfRel(),
        linkTo(methodOn(CurrencyController.class).all()).withRel("currencies"));

    if (currency.getStatus() == Status.ACTIVE) {
      currencyModel.add(linkTo(methodOn(CurrencyController.class)
          .deactivateCurrency(currency.getCurrencyId())).withRel("deactivate"));
    } else {
      currencyModel.add(linkTo(methodOn(CurrencyController.class)
          .activateCurrency(currency.getCurrencyId())).withRel("activate"));
    }
    return currencyModel;
  }
}
