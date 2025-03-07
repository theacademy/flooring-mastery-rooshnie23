package com.mthree.flooringmastery.ui;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryView {

  private final UserIO io;

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
    io.print("6. Export Active Orders");
    io.print("7. Quit");
    io.print("*\n" + "* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
    return io.readInt("Please select from the above choices.", 1, 7);
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

  public void displayOrders(List<Order> orders) {
    if (orders.isEmpty()) {
      io.print("No orders found.");
    } else {
      for (Order order : orders) {
        displayOrderDetails(order);
      }
    }
  }

  public LocalDate getDateInput() {
    return io.readDate("Enter order date (MM-DD-YYYY): ");
  }
  public LocalDate getDateAfter() {
    return io.readDateFuture("Enter order date (MM-DD-YYYY): ");
  }

  public String getCustomerName() {
    return io.readString("Enter Customer Name: ");
  }

  public String getState() {
    return io.readString("Enter state Abbreviation(e.g., CA, TX): ");
  }

  public BigDecimal getArea() {
    return io.readBigDecimal("Enter area (sq ft, min 100): ");
  }

  public String getProductSelection(List<Product> products) {
    io.print("\nAvailable Products:");
    for (int i = 0; i < products.size(); i++) {
      io.print((i + 1) + ". " + products.get(i).getProductType());
    }
    int choice = io.readInt("Please enter the number of the product you want: ", 1, products.size());

    return products.get(choice - 1).getProductType(); // Return selected product type
  }

  public void displayOrderDetails(Order savedOrder) {
    io.print("Order Details:");
    io.print("Date: " + savedOrder.getOrderDate());
    io.print("Order Number: " + savedOrder.getOrderNumber());
    io.print("Customer Name: " + savedOrder.getCustomerName());
    io.print("State: " + savedOrder.getState());
    io.print("Tax Rate: " + savedOrder.getTax().getTaxRate() + "%");
    io.print("Product: " + savedOrder.getProduct().getProductType());
    io.print("Area: " + savedOrder.getArea() + " sq ft");
    io.print("Cost Per Square Foot: " + savedOrder.getProduct().getCostPerSquareFoot() + "$");
    io.print("Labor Cost Per Square Foot: " + savedOrder.getProduct().getLaborCostPerSquareFoot() + "$");
    io.print("Material Cost: " + savedOrder.getMaterialCost() + "$");
    io.print("Tax: " + savedOrder.getTaxAmount() + "$");
    io.print("Total Cost: " + savedOrder.getTotalCost() + "$\n");
  }

  public String getOrderNumber() {
    return io.readString("Enter order number:");
  }

  public void displayMessage(String s) {
    io.print(s);
  }

  public boolean getConfirmation(String s) {
    io.print(s);
    return io.readString("").trim().equalsIgnoreCase("y");
  }

  public String getUpdatedCustomerName(String oldName) {
    return io.readString("Enter new Customer Name (" + oldName + "): ");
  }

  public String getUpdatedState(String oldState) {
    return io.readString("Enter new State (" + oldState + "): ");
  }

  public String getUpdatedArea(String oldArea) {
    return io.readString("Enter new Area (" + oldArea + " sq ft): ");
  }
  public void displayProductUpdateMessage(String oldProductType) {
    io.print("Enter new product type (" + oldProductType + "): ");
  }

  public void displayExportAllOrdersSuccess() {
    io.print("All orders have been successfully exported to an external file.");
  }
  public void displayExportActiveOrdersSuccess() {
    io.print("All active orders have been successfully exported to Backup/DataExport.txt.");
  }

}
