package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.util.List;
import java.time.LocalDate;

public class OrderDaoFileImpl implements OrderDao {

  @Override
  public List<Order> getOrdersByDate(LocalDate date) {
    return null;
  }

  @Override
  public Order getOrder(LocalDate date, int orderNumber) {
    return null;
  }

  @Override
  public void addOrder(Order order) {

  }

  @Override
  public void editOrder(Order order) {

  }

  @Override
  public void removeOrder(LocalDate date, int orderNumber) {

  }

  @Override
  public void exportAllData() {

  }
}
