package com.mthree.flooringmastery.ui;

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
  public double readDouble(String prompt) {
    while (true) {
      try {
        print(prompt);
        return Double.parseDouble(scanner.nextLine());
      } catch (NumberFormatException e) {
        print("Invalid input. Please enter a valid double.");
      }
    }
  }

  @Override
  public double readDouble(String prompt, double min, double max) {
    double value;
    do {
      value = readDouble(prompt + " (between " + min + " and " + max + "):");
    } while (value < min || value > max);
    return value;
  }

  @Override
  public float readFloat(String prompt) {
    while (true) {
      try {
        print(prompt);
        return Float.parseFloat(scanner.nextLine());
      } catch (NumberFormatException e) {
        print("Invalid input. Please enter a valid float.");
      }
    }
  }

  @Override
  public float readFloat(String prompt, float min, float max) {
    float value;
    do {
      value = readFloat(prompt + " (between " + min + " and " + max + "):");
    } while (value < min || value > max);
    return value;
  }

  @Override
  public long readLong(String prompt) {
    while (true) {
      try {
        print(prompt);
        return Long.parseLong(scanner.nextLine());
      } catch (NumberFormatException e) {
        print("Invalid input. Please enter a valid long.");
      }
    }
  }

  @Override
  public long readLong(String prompt, long min, long max) {
    long value;
    do {
      value = readLong(prompt + " (between " + min + " and " + max + "):");
    } while (value < min || value > max);
    return value;
  }
}

