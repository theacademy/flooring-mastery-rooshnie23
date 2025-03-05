package com.mthree.flooringmastery.service;

import com.mthree.flooringmastery.model.Order;
import java.math.BigDecimal;

public interface FlooringMasteryService {
 BigDecimal calculateCost(Order order);
 BigDecimal calculateLaborCost(Order order);
 BigDecimal calculateTax(Order order);
 BigDecimal calculateTotalCost(Order order);
}
