package com.mthree.flooringmastery.service;

public class FlooringMasteryDuplicateDateException extends Exception {

  public FlooringMasteryDuplicateDateException(String message) {
    super(message);
  }
  public FlooringMasteryDuplicateDateException(String message,
      Throwable cause) {
    super(message, cause);
  }
//  public FlooringMasteryDuplicateDateException(
//      Throwable cause) {
//    super("An order with this date already exits", cause);
//  }
}
