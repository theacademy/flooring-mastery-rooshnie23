package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.time.LocalDate;
import java.util.List;

public interface OrderDao {
  List<Order> getOrdersByDate(LocalDate date) throws FlooringMasterPersistenceException;
  Order getOrder(String orderNumber, LocalDate date)throws FlooringMasterPersistenceException;
  void addOrder(String orderNumber, Order order)throws FlooringMasterPersistenceException;
  void editOrder(String orderNumber, LocalDate date, Order order) throws FlooringMasterPersistenceException;
  void removeOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException;
  void exportAllData() throws FlooringMasterPersistenceException;
}
