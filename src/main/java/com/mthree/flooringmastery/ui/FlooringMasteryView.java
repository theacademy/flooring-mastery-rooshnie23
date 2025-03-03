package com.mthree.flooringmastery.ui;

public class FlooringMasteryView {
  private UserIO io;

  public FlooringMasteryView(UserIO io) {
    this.io = io;
  }
  public int printMenuAndGetSelection() {
    io.print("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    io.print("<<Flooring Program>>");
    io.print("1. Display Orders");
    io.print("2. Add an Order");
    io.print("3. Edit an Order");
    io.print("4. Remove an Order");
    io.print("5. Export All Data");
    io.print("6. Quit");
    io.print("*\n"+"* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    return io.readInt("Please select from the above choices.", 1, 6);
  }
  public void displayExitBanner() {
    io.print("Good Bye!!!");
  }

  public void displayUnknownCommandBanner() {
    io.print("Unknown Command!!!");
  }
  public void displayErrorMessage(String errorMsg) {
    io.print("=== ERROR ===");
    io.print(errorMsg);
  }
}
