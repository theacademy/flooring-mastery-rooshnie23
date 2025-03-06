package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasterPersistenceException;
import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceImpl implements FlooringMasteryService {

  private final OrderDao orderDao;
  private final TaxDao taxDao;
  private final ProductDao productDao;

  public FlooringMasteryServiceImpl(OrderDao orderDao, TaxDao taxDao, ProductDao productDao) {
    this.orderDao = orderDao;
    this.taxDao = taxDao;
    this.productDao = productDao;
  }

  @Override
  public List<Order> getOrdersByDate(LocalDate date) {
    return orderDao.getOrdersByDate(date);
  }

  @Override
  public Order getOrder(String orderNumber, LocalDate date) {
    return orderDao.getOrder(orderNumber);
  }

  @Override
  public Order addOrder(LocalDate date, String customerName, String state, String productType,
      BigDecimal area) {

    Order newOrder = new Order();
    newOrder.setOrderDate(date);
    newOrder.setCustomerName(customerName);
    newOrder.setState(state);
    newOrder.setProduct(productDao.getProductType(productType));
    newOrder.setTax(taxDao.getTaxByState(state));
    newOrder.setArea(area);

    recalculateOrder(newOrder);
    String orderNumber = generateOrderNumber(date);
    newOrder.setOrderNumber(orderNumber);

    orderDao.addOrder(orderNumber, newOrder);
    return newOrder;
  }

  @Override
  public Order editOrder(String orderNumber, LocalDate date, Order newOrderData) {
    List<Order> orders = orderDao.getOrdersByDate(date);

    // Find existing order
    Order existingOrder = orders.stream().filter(o -> o.getOrderNumber().equals(orderNumber))
        .findFirst().orElseThrow(() -> new FlooringMasterPersistenceException("Order not found!"));

    // Update fields if new values are provided
    if (!newOrderData.getCustomerName().isBlank()) {
      existingOrder.setCustomerName(newOrderData.getCustomerName());
    }

    if (!newOrderData.getState().isBlank()) {
      Tax tax = taxDao.getTaxByState(newOrderData.getState());
      if (tax != null) {
        existingOrder.setState(newOrderData.getState());
        existingOrder.setTax(tax);
      } else {
        throw new IllegalArgumentException("We do not sell in this state.");
      }
    }

    if (!newOrderData.getProduct().getProductType().isBlank()) {
      Product product = productDao.getProductType(newOrderData.getProduct().getProductType());
      if (product != null) {
        existingOrder.setProduct(product);
      } else {
        throw new IllegalArgumentException("Invalid product type.");
      }
    }

    if (newOrderData.getArea().compareTo(BigDecimal.valueOf(100)) >= 0) {
      existingOrder.setArea(newOrderData.getArea());
    }

    // Recalculate total cost after modifications
    recalculateOrder(existingOrder);

    // Persist changes
    orderDao.editOrder(orderNumber, date);
    return existingOrder;
  }

  @Override
  public void removeOrder(String orderNumber, LocalDate date) {
    orderDao.removeOrder(orderNumber, date);
  }

  @Override
  public void exportAllData() {
    orderDao.saveOrder();
  }

  @Override
  public List<Product> getAllProducts() {
    return productDao.getAllProducts();
  }

  /**
   * Helper method to generate an order number
   */
  private String generateOrderNumber(LocalDate date) {
    List<Order> orders = orderDao.getOrdersByDate(date);
    int maxOrderNumber = orders.stream().mapToInt(o -> Integer.parseInt(o.getOrderNumber())).max()
        .orElse(0);
    System.out.println(maxOrderNumber);
    return String.valueOf(maxOrderNumber + 1);
  }

  /**
   * Helper method to recalculate order total
   */
  private void recalculateOrder(Order order) {
    BigDecimal materialCost = order.getArea().multiply(order.getProduct().getCostPerSquareFoot());
    BigDecimal laborCost = order.getArea().multiply(order.getProduct().getLaborCostPerSquareFoot());
    BigDecimal taxAmount = (materialCost.add(laborCost)).multiply(
        order.getTax().getTaxRate().divide(BigDecimal.valueOf(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal totalCost = materialCost.add(laborCost).add(taxAmount);

    order.setMaterialCost(materialCost);
    order.setLaborCost(laborCost);
    order.setTaxAmount(taxAmount);
    order.setTotalCost(totalCost);
  }

  @Override
  public void validateCustomerName(String name) {
    if (name == null || !name.matches("[a-zA-Z0-9,. ]+")) {
      throw new IllegalArgumentException(
          "Invalid customer name. Only letters, numbers, spaces, commas, and periods are allowed.");
    }
  }

  @Override
  public void validateState(String state) {
    if (taxDao.getTaxByState(state) == null) {
      throw new IllegalArgumentException("Invalid state. We do not sell in this state.");
    }
  }
  @Override
  public void validateArea(BigDecimal area) {
    if (area.compareTo(BigDecimal.valueOf(100)) < 0) {
      throw new IllegalArgumentException("Invalid area. Minimum order size is 100 sq ft.");
    }
  }

}
