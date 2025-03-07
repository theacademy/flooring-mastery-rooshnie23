package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryService {
 Order addOrder(LocalDate date, String customerName, String state, String productType, BigDecimal area);
 void removeOrder(String orderNumber, LocalDate date);
 Order getOrder(String orderNumber, LocalDate date);
 Order editOrder(String orderNumber, LocalDate date, String newCustomerName, String newState, String newProductType, String newArea);
 void exportAllData();
 List<Order> getOrdersByDate(LocalDate date);
 List<Product> getAllProducts();
 void validateCustomerName(String name);
 void validateState(String state);
 void validateArea(BigDecimal area);
}
