package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.service.FlooringMasteryService;
import com.mthree.flooringmastery.ui.FlooringMasteryView;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryController {

  private final FlooringMasteryService service;
  private FlooringMasteryView view;

  public FlooringMasteryController(FlooringMasteryService service, FlooringMasteryView view) {
    this.service = service;
    this.view = view;
  }

  public void run() {
    boolean keepGoing = true;
    int menuSelection = 0;
    try {
      while (keepGoing) {

        menuSelection = getMenuSelection();

        switch (menuSelection) {
          case 1:
            displayOrders();
            break;
          case 2:
            addOrder();
            break;
          case 3:
            editOrder();
            break;
          case 4:
            removeOrder();
            break;
          case 5:
            exportData();
            break;
          case 6:
            keepGoing = false;
            break;
          default:
            unknownCommand();
        }

      }
      exitMessage();
    } catch (Exception e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private int getMenuSelection() {
    return view.printMenuAndGetSelection();
  }

  private void displayOrders() {
    LocalDate date = view.getDateInput();
    List<Order> orders = service.getOrdersByDate(date);
    view.displayOrders(orders);
  }

  private void addOrder() {
    LocalDate date = view.getDateAfter();

    // Validate customer name
    String customerName;
    while (true) {
      try {
        customerName = view.getCustomerName();
        service.validateCustomerName(customerName);
        break;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
  }

    // Validate state
    String state;
    while (true) {
      try {
        state = view.getState();
        service.validateState(state);
        break;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
    }

    // Fetch all products from the service and display them
    List<Product> products = service.getAllProducts();
    String productType = view.getProductSelection(products);

    // Validate area
    BigDecimal area;
    while (true) {
      try {
        area = view.getArea();
        service.validateArea(area);
        break;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
    Order newOrder = service.addOrder(date, customerName, state, productType, area);

    // Confirm before adding
    view.displayOrderDetails(newOrder);
  }

  private void editOrder() {
    LocalDate date = view.getDateInput();
    String orderNumber = view.getOrderNumber();
    Order existingOrder = service.getOrder(orderNumber, date);

    if (existingOrder == null) {
      view.displayMessage("Order not found.");
      return;
    }

    Order updatedOrder = view.getUpdatedOrderDetails(existingOrder);
    try {
      Order editedOrder = service.editOrder(orderNumber, date, updatedOrder);
      view.displayOrderDetails(editedOrder);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage("Error: " + e.getMessage());
    }
  }

  private void removeOrder() {
    LocalDate date = view.getDateInput();
    String orderNumber = view.getOrderNumber();

    Order existingOrder = service.getOrder(orderNumber, date);
    if (existingOrder == null) {
      view.displayMessage("Order not found.");
      return;
    }

    boolean confirm = view.getConfirmation("Are you sure you want to remove this order? (Y/N): ");
    if (confirm) {
      service.removeOrder(orderNumber, date);
      view.displayMessage("Order removed successfully.");
    } else {
      view.displayMessage("Order removal cancelled.");
    }
  }

  private void exportData() {
    service.exportAllData();
    view.displayMessage("All orders have been exported successfully.");
  }
  private void unknownCommand() {
    view.displayUnknownCommandBanner();
  }

  private void exitMessage() {
    view.displayExitBanner();
  }
}

