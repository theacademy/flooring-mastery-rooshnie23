package com.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {

  private String productType;
  private BigDecimal costPerSquareFoot;
  private BigDecimal laborCostPerSquareFoot;

  public Product(String productType) {
    this.productType = productType;
  }

  public String getProductType() {
    return productType;
  }

  public void setProductType(String productType) {
    this.productType = productType;
  }

  public BigDecimal getCostPerSquareFoot() {
    return costPerSquareFoot;
  }

  public void setCostPerSquareFoot(BigDecimal costPerSquareFoot) {
    this.costPerSquareFoot = costPerSquareFoot;
  }

  public BigDecimal getLaborCostPerSquareFoot() {
    return laborCostPerSquareFoot;
  }

  public void setLaborCostPerSquareFoot(BigDecimal laborCostPerSquareFoot) {
    this.laborCostPerSquareFoot = laborCostPerSquareFoot;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Product product = (Product) o;
    return Objects.equals(productType, product.productType) && Objects.equals(
        costPerSquareFoot, product.costPerSquareFoot) && Objects.equals(
        laborCostPerSquareFoot, product.laborCostPerSquareFoot);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productType, costPerSquareFoot, laborCostPerSquareFoot);
  }

  @Override
  public String toString() {
    return "Product{" +
        "productType='" + productType + '\'' +
        ", costPerSquareFoot=" + costPerSquareFoot +
        ", laborCostPerSquareFoot=" + laborCostPerSquareFoot +
        '}';
  }
}
