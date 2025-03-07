package com.mthree.flooringmastery.service;

import static org.junit.jupiter.api.Assertions.*;

import com.mthree.flooringmastery.dao.FlooringMasterPersistenceException;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringMasteryServiceImplTest {

  private FlooringMasteryService service;

  public FlooringMasteryServiceImplTest() {
    ApplicationContext ctx =
        new ClassPathXmlApplicationContext("applicationContext.xml");
    service = ctx.getBean("serviceLayer", FlooringMasteryService.class);
  }

  @BeforeAll
  public static void setUpClass() {
  }

  @AfterAll
  public static void tearDownClass() {
  }

  @BeforeEach
  public void setUp() {
  }

  @AfterEach
  public void tearDown() {
  }

  // ✅ Validate Customer Name
  @Test
  public void testValidateCustomerName_Valid() {
    assertDoesNotThrow(() -> service.validateCustomerName("John Doe"));
  }

  @Test
  public void testValidateCustomerName_Empty() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateCustomerName(""));
  }

  @Test
  public void testValidateCustomerName_SpecialCharacters() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateCustomerName("@$%^&*"));
  }

  // ✅ Validate State
  @Test
  public void testValidateState_Valid() {
    assertDoesNotThrow(() -> service.validateState("TX"));
  }

  @Test
  public void testValidateState_Invalid() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateState("ZZ"));
  }

  @Test
  public void testValidateState_CaseInsensitive() {
    assertDoesNotThrow(() -> service.validateState("tx"));
  }

  // ✅ Validate Area
  @Test
  public void testValidateArea_Valid() {
    assertDoesNotThrow(() -> service.validateArea(BigDecimal.valueOf(150)));
  }

  @Test
  public void testValidateArea_BelowMinimum() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateArea(BigDecimal.valueOf(50)));
  }

  @Test
  public void testValidateArea_Zero() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateArea(BigDecimal.ZERO));
  }

  @Test
  public void testValidateArea_Negative() {
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.validateArea(BigDecimal.valueOf(-10)));
  }

  // ✅ Add Order
  @Test
  public void testAddOrder_Success() throws FlooringMasterPersistenceException, FlooringMasteryDataValidationException{
    LocalDate date = LocalDate.of(2025, 3, 10);
    Order newOrder = service.addOrder(date, "Jane Doe", "TX", "Tile", BigDecimal.valueOf(200));

    assertNotNull(newOrder);
    assertEquals("Jane Doe", newOrder.getCustomerName());
    assertEquals("TX", newOrder.getState());
    assertEquals("Tile", newOrder.getProduct().getProductType());
    assertEquals(BigDecimal.valueOf(200), newOrder.getArea());
  }

  @Test
  public void testAddOrder_InvalidState() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.addOrder(date, "John Doe", "ZZ", "Tile", BigDecimal.valueOf(200)));
  }

  @Test
  public void testAddOrder_InvalidProduct() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    assertThrows(NullPointerException.class, () -> service.addOrder(date, "John Doe", "TX", "InvalidProduct", BigDecimal.valueOf(200)));
  }

  @Test
  public void testAddOrder_NegativeArea() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    assertThrows(FlooringMasteryDataValidationException.class, () -> service.addOrder(date, "John Doe", "TX", "Tile", BigDecimal.valueOf(-100)));
  }

  // ✅ Edit Order
  @Test
  public void testEditOrder_Success() throws FlooringMasterPersistenceException, FlooringMasteryDataValidationException {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String orderNumber = "1";

    Order updatedOrder = service.editOrder(orderNumber, date, "New Name", "OH", "Wood", "250");

    assertNotNull(updatedOrder);
    assertEquals("New Name", updatedOrder.getCustomerName());
    assertEquals("OH", updatedOrder.getState());
    assertEquals("Wood", updatedOrder.getProduct().getProductType());
    assertEquals(BigDecimal.valueOf(250), updatedOrder.getArea());
  }

  @Test
  public void testEditOrder_KeepOriginalValues() throws FlooringMasterPersistenceException, FlooringMasteryDataValidationException {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String orderNumber = "1";

    Order existingOrder = service.getOrder(orderNumber, date);
    Order updatedOrder = service.editOrder(orderNumber, date, existingOrder.getCustomerName(), existingOrder.getState(), existingOrder.getProduct().getProductType(), existingOrder.getArea().toString());

    assertEquals(existingOrder.getCustomerName(), updatedOrder.getCustomerName());
    assertEquals(existingOrder.getState(), updatedOrder.getState());
    assertEquals(existingOrder.getProduct().getProductType(), updatedOrder.getProduct().getProductType());
    assertEquals(existingOrder.getArea(), updatedOrder.getArea());
  }

  // ✅ Remove Order
  @Test
  public void testRemoveOrder_Success() throws FlooringMasterPersistenceException {
    LocalDate date = LocalDate.of(2025, 3, 10);
    String orderNumber = "1";

    assertNotNull(service.getOrder(orderNumber, date));

    service.removeOrder(orderNumber, date);
    assertNull(service.getOrder(orderNumber, date));
  }

  @Test
  public void testRemoveOrder_InvalidOrder() {
    LocalDate date = LocalDate.of(2025, 3, 10);
    assertThrows(FlooringMasterPersistenceException.class, () -> service.removeOrder("999", date));
  }

  // ✅ Export All Orders
  @Test
  public void testExportAllOrders() {
    assertDoesNotThrow(() -> service.exportAllData());
  }

  // ✅ Get Orders By Date
  @Test
  public void testGetOrdersByDate_ValidDate() throws FlooringMasterPersistenceException {
    LocalDate date = LocalDate.of(2025, 3, 10);
    List<Order> orders = service.getOrdersByDate(date);

    assertNotNull(orders);
    assertFalse(orders.isEmpty());
  }
}
