package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
  List<Order> getOrdersByDate(LocalDate date);
  Order getOrder(String orderNumber);
  void addOrder(String orderNumber, Order order);
  void editOrder(String orderNumber, LocalDate date);
  void removeOrder(String orderNumber, LocalDate date);
  void saveOrder();
}
