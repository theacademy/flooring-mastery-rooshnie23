package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.util.List;
import java.time.LocalDate;

public interface OrderDao {
  List<Order> getOrdersByDate(LocalDate date);
  Order getOrder(LocalDate date, int orderNumber);
  void addOrder(Order order);
  void editOrder(Order order);
  void removeOrder(LocalDate date, int orderNumber);
  void exportAllData();
}
