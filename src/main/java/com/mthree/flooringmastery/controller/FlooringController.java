package com.mthree.flooringmastery.controller;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.service.OrderServiceLayer;
import com.mthree.flooringmastery.ui.FlooringMasteryView;
import java.time.LocalDate;
import java.util.List;

public class FlooringController {
  private OrderServiceLayer orderService;
  private FlooringMasteryView view;

  public FlooringController(OrderServiceLayer orderService, FlooringMasteryView view) {
    this.orderService = orderService;
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
//    LocalDate date = view.getDate();
//    List<Order> orders = orderService.getOrdersByDate(date);
//    view.displayOrders(orders);
  }

  private void addOrder() {
//    Order order = view.getOrderInfo();
//    orderService.addOrder(order);
//    view.displayOrderSummary(order);
  }

  private void editOrder() {
//    LocalDate date = view.getDate();
//    int orderNumber = view.getOrderNumber();
//    orderService.removeOrder(date, orderNumber);
//    view.displayMessage("Order removed.");
  }
  private void removeOrder() {
//    LocalDate date = view.getDate();
//    int orderNumber = view.getOrderNumber();
//    orderService.removeOrder(date, orderNumber);
//    view.displayMessage("Order removed.");
  }
  private void unknownCommand() {
    view.displayUnknownCommandBanner();
  }

  private void exitMessage() {
    view.displayExitBanner();
  }
}

