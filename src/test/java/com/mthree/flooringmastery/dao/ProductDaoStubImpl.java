package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoStubImpl implements ProductDao {

  private final List<Product> products = new ArrayList<>();

  public ProductDaoStubImpl() {
    Product tile = new Product("Tile");
    tile.setCostPerSquareFoot(BigDecimal.valueOf(3.50));
    tile.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.15));

    Product wood = new Product("Wood");
    wood.setCostPerSquareFoot(BigDecimal.valueOf(5.25));
    wood.setLaborCostPerSquareFoot(BigDecimal.valueOf(4.75));

    products.add(tile);
    products.add(wood);
  }

  @Override
  public Product getProductType(String productType) {
    return products.stream()
        .filter(p -> p.getProductType().equalsIgnoreCase(productType))
        .findFirst()
        .orElse(null);
  }

  @Override
  public List<Product> getAllProducts() {
    return new ArrayList<>(products);
  }

}
