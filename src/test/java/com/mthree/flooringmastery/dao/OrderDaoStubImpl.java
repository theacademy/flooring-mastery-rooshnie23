package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoStubImpl implements OrderDao {

  private final List<Order> orders = new ArrayList<>();

  public OrderDaoStubImpl() {
    Order testOrder = new Order();
    testOrder.setOrderNumber("1");
    testOrder.setOrderDate(LocalDate.of(2025, 3, 10));
    testOrder.setCustomerName("Test Customer");
    testOrder.setState("TX");
    testOrder.setTax(new Tax("Texas"));
    testOrder.setProduct(new Product("Tile"));
    testOrder.setArea(BigDecimal.valueOf(200));
    testOrder.setMaterialCost(BigDecimal.valueOf(500));
    testOrder.setLaborCost(BigDecimal.valueOf(300));
    testOrder.setTaxAmount(BigDecimal.valueOf(50));
    testOrder.setTotalCost(BigDecimal.valueOf(850));
    orders.add(testOrder);
  }

  @Override
  public List<Order> getOrdersByDate(LocalDate date) {
    return orders;
  }

  @Override
  public Order getOrder(String orderNumber, LocalDate date) {
    return orders.stream()
        .filter(o -> o.getOrderNumber().equals(orderNumber))
        .findFirst()
        .orElse(null);
  }

  @Override
  public void addOrder(String orderNumber, Order order) {
    orders.add(order);
  }

  @Override
  public void editOrder(String orderNumber, LocalDate date, Order order) {
    removeOrder(orderNumber, date);
    orders.add(order);
  }

  @Override
  public void removeOrder(String orderNumber, LocalDate date) {
    orders.removeIf(order -> order.getOrderNumber().equals(orderNumber));
  }

  @Override
  public void exportAllData() {

  }

}
