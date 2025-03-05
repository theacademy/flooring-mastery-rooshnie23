package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.time.LocalDate;

public interface OrderDao {
  Order getOrderByDate(LocalDate date);
  Order getOrder(int orderNumber);
  void addOrder(int orderNumber, Order order);
  void editOrder(LocalDate date);
  void removeOrder(LocalDate date);
  void saveOrder();
}
