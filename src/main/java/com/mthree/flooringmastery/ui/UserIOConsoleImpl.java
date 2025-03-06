package com.mthree.flooringmastery.ui;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class UserIOConsoleImpl implements UserIO {
  private final Scanner scanner;

  public UserIOConsoleImpl() {
    this.scanner = new Scanner(System.in);
  }

  @Override
  public void print(String message) {
    System.out.println(message);
  }

  @Override
  public String readString(String prompt) {
    print(prompt);
    return scanner.nextLine();
  }

  @Override
  public int readInt(String prompt) {
    while (true) {
      try {
        print(prompt);
        return Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        print("Invalid input. Please enter a valid integer.");
      }
    }
  }

  @Override
  public int readInt(String prompt, int min, int max) {
    int value;
    do {
      value = readInt(prompt + " (between " + min + " and " + max + "):");
    } while (value < min || value > max);
    return value;
  }

  @Override
  public LocalDate readDate(String prompt) {
    while (true) {
      try {
        print(prompt);
        return LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("MM-dd-yyyy"));
      } catch (DateTimeParseException e) {
        print("Invalid input. Please enter a valid date in MM-DD-YYYY format.");
      }
    }
  }

  @Override
  public LocalDate readDateFuture(String prompt) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

    while (true) {
      try {
        print(prompt);
        LocalDate date = LocalDate.parse(scanner.nextLine(), formatter);

        if (date.isBefore(LocalDate.now())) {
          print("Invalid input. The date cannot be in the past. Please enter a valid future or present date.");
          continue;
        }

        return date;
      } catch (DateTimeParseException e) {
        print("Invalid input. Please enter a valid date in MM-DD-YYYY format.");
      }
    }
  }

  @Override
  public BigDecimal readBigDecimal(String prompt) {
    while (true) {
      try {
        print(prompt);
        return new BigDecimal(scanner.nextLine());
      } catch (NumberFormatException e) {
        print("Invalid input. Please enter a valid decimal number.");
      }
    }
  }

}

