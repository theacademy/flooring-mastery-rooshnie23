package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TaxDaoStubImpl implements TaxDao {

  private final Map<String, Tax> taxRates = new HashMap<>();

  public TaxDaoStubImpl() {
    Tax texas = new Tax("Texas");
    texas.setStateAbbreviation("TX");
    texas.setTaxRate(BigDecimal.valueOf(6.25));

    Tax ohio = new Tax("Ohio");
    ohio.setStateAbbreviation("OH");
    ohio.setTaxRate(BigDecimal.valueOf(5.75));

    taxRates.put("TX", texas);
    taxRates.put("OH", ohio);
  }

  @Override
  public Tax getTaxByState(String stateAbbr) {
    return taxRates.get(stateAbbr);
  }

}
