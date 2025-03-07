package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
  Product getProductType(String productType) throws FlooringMasterPersistenceException;
  List<Product> getAllProducts() throws FlooringMasterPersistenceException;
}
