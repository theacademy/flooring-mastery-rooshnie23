package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.util.List;

public interface ProductDao {
  Product getProductByType(String productType);
  List<Product> getAllProducts();
}
