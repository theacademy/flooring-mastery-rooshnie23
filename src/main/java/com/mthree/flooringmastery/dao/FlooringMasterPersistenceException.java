package com.mthree.flooringmastery.dao;

public class FlooringMasterPersistenceException extends RuntimeException {

  public FlooringMasterPersistenceException(String message) {
    super(message);
  }
  public FlooringMasterPersistenceException(String message, Throwable cause) {
    super(message, cause);
  }
}
