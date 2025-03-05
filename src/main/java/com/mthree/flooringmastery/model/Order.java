package com.mthree.flooringmastery.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Order {
  private LocalDate orderDate;
  private Integer orderNumber;
  private String customerName;
  private String state;
  private Tax tax;
  private Product product;
  private BigDecimal area;
  private BigDecimal materialCost;
  private BigDecimal laborCost;
  private BigDecimal taxAmount;
  private BigDecimal totalCost;

  public Order(LocalDate orderDate){
    this.orderDate = orderDate;
  }
  public LocalDate getOrderDate() {
    return orderDate;
  }

  public Integer getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(Integer orderNumber) {
    this.orderNumber = orderNumber;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Tax getTax() {
    return tax;
  }

  public void setTax(Tax tax) {
    this.tax = tax;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public BigDecimal getArea() {
    return area;
  }

  public void setArea(BigDecimal area) {
    this.area = area;
  }

  public BigDecimal getMaterialCost() {
    return materialCost;
  }

  public void setMaterialCost(BigDecimal materialCost) {
    this.materialCost = materialCost;
  }

  public BigDecimal getLaborCost() {
    return laborCost;
  }

  public void setLaborCost(BigDecimal laborCost) {
    this.laborCost = laborCost;
  }

  public BigDecimal getTaxAmount() {
    return taxAmount;
  }

  public void setTaxAmount(BigDecimal taxAmount) {
    this.taxAmount = taxAmount;
  }

  public BigDecimal getTotalCost() {
    return totalCost;
  }

  public void setTotalCost(BigDecimal totalCost) {
    this.totalCost = totalCost;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Order order = (Order) o;
    return Objects.equals(orderDate, order.orderDate)
        && Objects.equals(orderNumber, order.orderNumber)
        && Objects.equals(customerName, order.customerName)
        && Objects.equals(state, order.state)
        && Objects.equals(tax.getTaxRate(), order.tax.getTaxRate())
        && Objects.equals(product.getProductType(), order.product.getProductType())
        && Objects.equals(area, order.area)
        && Objects.equals(product.getCostPerSquareFoot(), order.product.getCostPerSquareFoot())
        && Objects.equals(product.getLaborCostPerSquareFoot(), order.product.getLaborCostPerSquareFoot())
        && Objects.equals(materialCost, order.materialCost) && Objects.equals(laborCost, order.laborCost)
        && Objects.equals(taxAmount, order.taxAmount) && Objects.equals(totalCost, order.totalCost);
  }

  @Override
  public int hashCode() {
    return Objects.hash(orderNumber, customerName, state, tax, product, area, materialCost,
        laborCost,
        taxAmount, totalCost);
  }

  @Override
  public String toString() {
    return "Order{" +
        "orderDate=" + orderDate +
        "orderNumber=" + orderNumber +
        ", customerName='" + customerName + '\'' +
        ", state='" + state + '\'' +
        ", taxRate=" + tax.getTaxRate() +
        ", productType=" + product.getProductType() +
        ", area=" + area +
        ", costPerSquareFoot=" + product.getCostPerSquareFoot() +
        ", laborCostPerSquareFoot="+ product.getLaborCostPerSquareFoot() +
        ", materialCost=" + materialCost +
        ", laborCost=" + laborCost +
        ", taxAmount=" + taxAmount +
        ", totalCost=" + totalCost +
        '}';
  }
}
