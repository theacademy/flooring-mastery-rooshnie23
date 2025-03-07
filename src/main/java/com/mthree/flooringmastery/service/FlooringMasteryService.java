package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.dao.FlooringMasterPersistenceException;
import com.mthree.flooringmastery.model.Order;
import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface FlooringMasteryService {
 Order addOrder(LocalDate date, String customerName, String state, String productType, BigDecimal area) throws FlooringMasterPersistenceException, FlooringMasteryDataValidationException;
 void removeOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException;
 Order getOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException;
 Order editOrder(String orderNumber, LocalDate date, String newCustomerName, String newState, String newProductType, String newArea) throws FlooringMasterPersistenceException,FlooringMasteryDataValidationException;
 void exportAllData() throws FlooringMasterPersistenceException;
 void exportActiveOrders() throws FlooringMasterPersistenceException;
 List<Order> getOrdersByDate(LocalDate date) throws FlooringMasterPersistenceException;
 List<Product> getAllProducts() throws FlooringMasterPersistenceException;
 void validateCustomerName(String name) throws FlooringMasteryDataValidationException;
 void validateState(String state) throws FlooringMasteryDataValidationException;
 void validateArea(BigDecimal area) throws FlooringMasteryDataValidationException;
}
