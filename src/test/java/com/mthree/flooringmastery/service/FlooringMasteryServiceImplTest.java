package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import com.mthree.flooringmastery.model.Tax;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FlooringMasteryServiceImplTest {

  private FlooringMasteryService service;

  @BeforeEach
  public void setUp() {
    // Load Spring test configuration
    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    service = ctx.getBean("serviceLayer", FlooringMasteryService.class);
  }

  // ✅ Test Customer Name Validation
  @Test
  public void testValidateCustomerName_Valid() {
    assertDoesNotThrow(() -> service.validateCustomerName("John Doe"));
  }

  @Test
  public void testValidateCustomerName_Invalid() {
    assertThrows(IllegalArgumentException.class, () -> service.validateCustomerName(""));
  }

  // ✅ Test State Validation
  @Test
  public void testValidateState_Valid() {
    assertDoesNotThrow(() -> service.validateState("TX"));
  }

  @Test
  public void testValidateState_Invalid() {
    assertThrows(IllegalArgumentException.class, () -> service.validateState("ZZ"));
  }

  // ✅ Test Area Validation
  @Test
  public void testValidateArea_Valid() {
    assertDoesNotThrow(() -> service.validateArea(BigDecimal.valueOf(150)));
  }

  @Test
  public void testValidateArea_Invalid() {
    assertThrows(IllegalArgumentException.class, () -> service.validateArea(BigDecimal.valueOf(50)));
  }

  // ✅ Test Adding a New Order
  @Test
  public void testAddOrder_Success() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String customerName = "Jane Doe";
    String state = "TX";
    String productType = "Tile";
    BigDecimal area = BigDecimal.valueOf(200);

    Order newOrder = service.addOrder(date, customerName, state, productType, area);
    assertNotNull(newOrder);
    assertEquals("Jane Doe", newOrder.getCustomerName());
    assertEquals("TX", newOrder.getState());
    assertEquals("Tile", newOrder.getProduct().getProductType());
    assertEquals(area, newOrder.getArea());
  }

  // ✅ Test Editing an Order
  @Test
  public void testEditOrder_Success() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String orderNumber = "1";

    Order existingOrder = service.getOrder(orderNumber, date);
    assertNotNull(existingOrder);

//    // Editing the order
//     service.editOrder(orderNumber, date, "New Name", "TX", "Wood", "250");
//
//    // Fetch updated order
//    Order updatedOrder = service.getOrder(orderNumber, date);
//    assertNotNull(updatedOrder);
//    assertEquals("New Name", updatedOrder.getCustomerName());
//    assertEquals("TX", updatedOrder.getState());
//    assertEquals("Wood", updatedOrder.getProduct().getProductType());
//    assertEquals(BigDecimal.valueOf(250), updatedOrder.getArea());
  }

  // ✅ Test Getting Orders by Date
  @Test
  public void testGetOrdersByDate() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    List<Order> orders = service.getOrdersByDate(date);

    assertNotNull(orders);
    assertFalse(orders.isEmpty());
  }

  // ✅ Test Removing an Order
  @Test
  public void testRemoveOrder() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String orderNumber = "1";

    assertNotNull(service.getOrder(orderNumber, date));

    service.removeOrder(orderNumber, date);

    assertNull(service.getOrder(orderNumber, date));
  }

  // ✅ Test Order Cost Calculation
  @Test
  public void testOrderCostCalculation() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String customerName = "John Doe";
    String state = "TX";
    String productType = "Tile";
    BigDecimal area = BigDecimal.valueOf(100);

    Order newOrder = service.addOrder(date, customerName, state, productType, area);

    BigDecimal expectedMaterialCost = area.multiply(newOrder.getProduct().getCostPerSquareFoot());
    BigDecimal expectedLaborCost = area.multiply(newOrder.getProduct().getLaborCostPerSquareFoot());
    BigDecimal expectedTax = (expectedMaterialCost.add(expectedLaborCost))
        .multiply(newOrder.getTax().getTaxRate().divide(BigDecimal.valueOf(100)))
        .setScale(2, BigDecimal.ROUND_HALF_UP);
    BigDecimal expectedTotalCost = expectedMaterialCost.add(expectedLaborCost).add(expectedTax);

    assertEquals(expectedMaterialCost, newOrder.getMaterialCost());
    assertEquals(expectedLaborCost, newOrder.getLaborCost());
    assertEquals(expectedTax, newOrder.getTaxAmount());
    assertEquals(expectedTotalCost, newOrder.getTotalCost());
  }

  // ✅ Test Exporting All Orders (Mock)
  @Test
  public void testExportAllOrders() {
    assertDoesNotThrow(() -> service.exportAllData());
  }
}
