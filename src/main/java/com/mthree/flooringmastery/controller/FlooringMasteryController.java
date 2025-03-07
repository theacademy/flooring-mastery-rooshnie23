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
    //Validate Customer and State
    String customerName = validateCustomerName();
    String state = validateState();

    // Fetch all products from the service and display them
    List<Product> products = service.getAllProducts();
    String productType = view.getProductSelection(products);

    // Validate area
    BigDecimal area = validateArea();
    Order newOrder = service.addOrder(date, customerName, state, productType, area);

    // Confirm before adding
    view.displayOrderDetails(newOrder);
  }

  private BigDecimal validateArea() {
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
    return area;
  }

  private String validateState() {
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
    return state;
  }

//private void editOrder() {
//  Order existingOrder;
//  LocalDate date;
//  String orderNumber;
//
//  do {
//    date = view.getDateInput();
//    orderNumber = view.getOrderNumber();
//    existingOrder = service.getOrder(orderNumber, date);
//
//    if (existingOrder == null) {
//      view.displayMessage("Order not found. Please try again.");
//    }
//  } while (existingOrder == null);
//
//  // Get updates from the user
//  String newCustomerName = view.getUpdatedCustomerName(existingOrder.getCustomerName());
//  String newState = view.getUpdatedState(existingOrder.getState());
//
//  List<Product> products = service.getAllProducts();
//  view.displayProductUpdateMessage( existingOrder.getProduct().getProductType());
//  String newProductType = view.getProductSelection(products);
//
//  String areaInput = view.getUpdatedArea(existingOrder.getArea().toString());
//
//  // Pass the changes to the service layer for validation & processing
//  try {
//    Order updatedOrder = service.editOrder(orderNumber, date, newCustomerName, newState, newProductType, areaInput);
//    view.displayOrderDetails(updatedOrder);
//    view.displayMessage("Order successfully updated!");
//  } catch (IllegalArgumentException e) {
//    view.displayErrorMessage(e.getMessage());
//  }
//}
private void editOrder() {
  Order existingOrder;
  LocalDate date;
  String orderNumber;

  // Keep prompting until a valid order is found
  do {
    date = view.getDateInput();
    orderNumber = view.getOrderNumber();
    existingOrder = service.getOrder(orderNumber, date);

    if (existingOrder == null) {
      view.displayMessage("Order not found. Please try again.");
    }
  } while (existingOrder == null);

  // Get updated values from the user, ensuring valid input
  String newCustomerName = validateUpdatedCustomerName(existingOrder.getCustomerName());
  String newState = validateUpdatedState(existingOrder.getState());

  List<Product> products = service.getAllProducts();
  view.displayProductUpdateMessage( existingOrder.getProduct().getProductType());
  String newProductType = view.getProductSelection(products);

  String areaInput = validateUpdatedArea(existingOrder.getArea());

  // Pass the changes to the service layer for validation & processing
  try {
    Order updatedOrder = service.editOrder(orderNumber, date, newCustomerName, newState, newProductType, areaInput);
    view.displayOrderDetails(updatedOrder);
    view.displayMessage("Order successfully updated!");
  } catch (IllegalArgumentException e) {
    view.displayErrorMessage(e.getMessage());
  }
}
  private String validateUpdatedCustomerName(String oldName) {
    String customerName;
    while (true) {
      customerName = view.getUpdatedCustomerName(oldName);
      if (customerName.isBlank()) return oldName; // Keep old value if blank
      try {
        service.validateCustomerName(customerName);
        return customerName;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
  }

  private String validateUpdatedState(String oldState) {
    String state;
    while (true) {
      state = view.getUpdatedState(oldState);
      if (state.isBlank()) return oldState; // Keep old value if blank
      try {
        service.validateState(state);
        return state;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
  }

  private String validateUpdatedArea(BigDecimal oldArea) {
    while (true) {
      String areaInput = view.getUpdatedArea(oldArea.toString());
      if (areaInput.isBlank()) return oldArea.toString(); // Keep old value if blank
      try {
        BigDecimal area = new BigDecimal(areaInput);
        service.validateArea(area);
        return areaInput;
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage("Invalid area. Please enter a valid number (minimum 100 sq ft).");
      }
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
  private String validateCustomerName(){
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
    return customerName;
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

