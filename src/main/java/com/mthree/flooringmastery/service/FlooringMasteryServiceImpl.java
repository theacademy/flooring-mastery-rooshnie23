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
    return orderDao.getOrder(orderNumber,date);
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
  public Order editOrder(String orderNumber, LocalDate date, String newCustomerName, String newState, String newProductType, String newArea) {
    Order existingOrder = orderDao.getOrder(orderNumber,date);
    boolean changed = false;

    if (!newCustomerName.equals(existingOrder.getCustomerName())) {
      existingOrder.setCustomerName(newCustomerName);
    }

    if (!newState.equals(existingOrder.getState())) {
      existingOrder.setState(newState);
      changed = true;
    }

    if (!newProductType.equals(existingOrder.getProduct().getProductType())) {
      existingOrder.setProduct(productDao.getProductType(newProductType));
      changed = true;
    }

    if (!newArea.equals(existingOrder.getArea().toString())) {
      existingOrder.setArea(new BigDecimal(newArea));
      changed = true;
    }



//    if (!newCustomerName.isBlank()) {
//      validateCustomerName(newCustomerName);
//      existingOrder.setCustomerName(newCustomerName);
//    }
//
//    if (!newState.isBlank()) {
//      validateState(newState);
//      Tax newTax = taxDao.getTaxByState(newState);
//      existingOrder.setState(newState);
//      existingOrder.setTax(newTax);
//    }
//
//    if (!newProductType.isBlank()) {
//      Product newProduct = productDao.getProductType(newProductType);
//      existingOrder.setProduct(newProduct);
//    }
//
//    if (!newArea.isBlank()) {
//      try {
//        BigDecimal areaValue = new BigDecimal(newArea);
//        validateArea(areaValue);
//        existingOrder.setArea(areaValue);
//        changed = true;
//      } catch (NumberFormatException e) {
//        throw new IllegalArgumentException("Invalid area format. Please enter a valid number.");
//      }
//    }

   if (changed) {
     recalculateOrder(existingOrder);
   }

    // Persist the changes
    orderDao.editOrder(orderNumber, date,existingOrder);
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
