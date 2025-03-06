package com.mthree.flooringmastery.ui;


import java.math.BigDecimal;
import java.time.LocalDate;

public interface UserIO {

  void print(String msg);

  int readInt(String prompt);

  int readInt(String prompt, int min, int max);

  String readString(String prompt);

  LocalDate readDate(String prompt);

  LocalDate readDateFuture(String prompt);

  BigDecimal readBigDecimal(String prompt);
}

