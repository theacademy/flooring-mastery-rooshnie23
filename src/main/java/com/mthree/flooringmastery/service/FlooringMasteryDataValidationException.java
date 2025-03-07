package com.mthree.flooringmastery.service;

public class FlooringMasteryDataValidationException extends Exception {

  public FlooringMasteryDataValidationException(String message) {
    super(message);
  }
  public FlooringMasteryDataValidationException(String message,
      Throwable cause) {
    super(message, cause);
  }
//  public FlooringMasteryDuplicateDateException(
//      Throwable cause) {
//    super("An order with this date already exits", cause);
//  }
}
