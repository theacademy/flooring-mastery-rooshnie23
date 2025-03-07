package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasterPersistenceException;
import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
  public List<Order> getOrdersByDate(LocalDate date) throws FlooringMasterPersistenceException{
    return orderDao.getOrdersByDate(date);
  }

  @Override
  public Order getOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException {
    return orderDao.getOrder(orderNumber,date);
  }

  @Override
  public Order addOrder(LocalDate date, String customerName, String state, String productType, BigDecimal area) throws FlooringMasterPersistenceException,FlooringMasteryDataValidationException{
    if (taxDao.getTaxByState(state) == null) {
      throw new FlooringMasteryDataValidationException("Invalid state. We do not sell in this state.");
    }

    Order newOrder = new Order();
    newOrder.setOrderDate(date);
    newOrder.setCustomerName(customerName);
    newOrder.setState(state);
    newOrder.setProduct(productDao.getProductType(productType));
    newOrder.setTax(taxDao.getTaxByState(state));
    validateArea(area);
    newOrder.setArea(area);

    recalculateOrder(newOrder);
    String orderNumber = generateOrderNumber(date);
    newOrder.setOrderNumber(orderNumber);

    orderDao.addOrder(orderNumber, newOrder);
    return newOrder;
  }

  @Override
  public Order editOrder(String orderNumber, LocalDate date, String newCustomerName, String newState, String newProductType, String newArea) throws FlooringMasterPersistenceException, FlooringMasteryDataValidationException {
    Order existingOrder = orderDao.getOrder(orderNumber,date);
    boolean changed = false;

    if (!newCustomerName.equals(existingOrder.getCustomerName())) {
      validateCustomerName(newCustomerName);
      existingOrder.setCustomerName(newCustomerName);
    }

    if (!newState.equals(existingOrder.getState())) {
      validateState(newState);
      existingOrder.setTax(taxDao.getTaxByState(newState));
      existingOrder.setState(newState);
      changed = true;
    }

    if (!newProductType.equals(existingOrder.getProduct().getProductType())) {
      existingOrder.setProduct(productDao.getProductType(newProductType));
      changed = true;
    }

    if (!newArea.equals(existingOrder.getArea().toString())) {
      validateArea(new BigDecimal(newArea));
      existingOrder.setArea(new BigDecimal(newArea));
      changed = true;
    }


   if (changed) {
     recalculateOrder(existingOrder);
   }

    // Persist the changes
    orderDao.editOrder(orderNumber, date,existingOrder);
    return existingOrder;
  }

  @Override
  public void removeOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException {
    Order existingOrder = orderDao.getOrder(orderNumber,date);
    if (existingOrder == null) {
      throw new FlooringMasterPersistenceException("Order does not exist.");
    }

    orderDao.removeOrder(orderNumber, date);
  }

  @Override
  public void exportAllData() {
    try {
      orderDao.exportAllData();
    } catch (FlooringMasterPersistenceException e) {
      throw new RuntimeException("Failed to export orders: " + e.getMessage(), e);
    }
  }

  @Override
  public List<Product> getAllProducts() throws FlooringMasterPersistenceException{
    return productDao.getAllProducts();
  }

  private String generateOrderNumber(LocalDate date) {
    List<Order> orders = orderDao.getOrdersByDate(date);
    int maxOrderNumber = orders.stream().mapToInt(o -> Integer.parseInt(o.getOrderNumber())).max()
        .orElse(0);
    System.out.println(maxOrderNumber);
    return String.valueOf(maxOrderNumber + 1);
  }

  private void recalculateOrder(Order order) {
    BigDecimal materialCost = order.getArea().multiply(order.getProduct().getCostPerSquareFoot());
    BigDecimal laborCost = order.getArea().multiply(order.getProduct().getLaborCostPerSquareFoot());
    BigDecimal taxAmount = (materialCost.add(laborCost)).multiply(
        order.getTax().getTaxRate().divide(BigDecimal.valueOf(100),2, RoundingMode.HALF_UP));
    BigDecimal totalCost = materialCost.add(laborCost).add(taxAmount);

    order.setMaterialCost(materialCost);
    order.setLaborCost(laborCost);
    order.setTaxAmount(taxAmount);
    order.setTotalCost(totalCost);
  }

  @Override
  public void validateCustomerName(String name) throws FlooringMasteryDataValidationException {
    if (name == null || !name.matches("[a-zA-Z0-9,. ]+")) {
      throw new FlooringMasteryDataValidationException(
          "Invalid customer name. Only letters, numbers, spaces, commas, and periods are allowed.");
    }
  }

  @Override
  public void validateState(String state) throws FlooringMasteryDataValidationException {
    if (taxDao.getTaxByState(state.toUpperCase()) == null) {
      throw new FlooringMasteryDataValidationException("Invalid state. We do not sell in this state.");
    }
  }
  public void validateArea(BigDecimal area) throws FlooringMasteryDataValidationException {
    if (area == null || area.compareTo(BigDecimal.ZERO) < 0) {
      throw new FlooringMasteryDataValidationException("Invalid area. Area cannot be negative.");
    }
    if (area.compareTo(BigDecimal.valueOf(100)) < 0) {
      throw new FlooringMasteryDataValidationException("Invalid area. Minimum order size is 100 sq ft.");
    }
  }

}
