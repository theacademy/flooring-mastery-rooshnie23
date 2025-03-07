package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;

public interface TaxDao {
  Tax getTaxByState(String state) throws FlooringMasterPersistenceException;
  String getStateName() throws FlooringMasterPersistenceException;
  String getStateAbbr() throws FlooringMasterPersistenceException;
}
