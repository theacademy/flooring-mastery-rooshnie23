package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
  Product getProductType(String productType);
  List<Product> getAllProducts();
  BigDecimal getCostPerSquareFoot(String productType);
  BigDecimal getLaborCostPerSquareFoot(String productType);
}
