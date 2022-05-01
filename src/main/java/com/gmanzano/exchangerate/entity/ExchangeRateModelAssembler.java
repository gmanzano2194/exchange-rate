package com.gmanzano.exchangerate.entity;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.gmanzano.exchangerate.controller.CurrencyController;
import com.gmanzano.exchangerate.controller.ExchangeRateController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * ExchangeRateModelAssembler.
 * Clase que permite armar el modelo de ExchangeRate
 *
 * @author Giancarlo Manzano
 * @version 1.0
 */
@Component
public class ExchangeRateModelAssembler implements RepresentationModelAssembler<ExchangeRate,
    EntityModel<ExchangeRate>> {

  @Override
  public EntityModel<ExchangeRate> toModel(ExchangeRate exchangeRate) {
    EntityModel<ExchangeRate> exchangeRateModel = EntityModel.of(exchangeRate,
        linkTo(methodOn(ExchangeRateController.class)
            .one(exchangeRate.getExchangeRateId())).withSelfRel(),
        linkTo(methodOn(ExchangeRateController.class).all()).withRel("exchage-rates"));

    if (exchangeRate.getStatus() == Status.ACTIVE) {
      exchangeRateModel.add(linkTo(methodOn(CurrencyController.class)
          .deactivateCurrency(exchangeRate.getExchangeRateId())).withRel("deactivate"));
    } else {
      exchangeRateModel.add(linkTo(methodOn(CurrencyController.class)
          .activateCurrency(exchangeRate.getExchangeRateId())).withRel("activate"));
    }
    return exchangeRateModel;
  }

}
