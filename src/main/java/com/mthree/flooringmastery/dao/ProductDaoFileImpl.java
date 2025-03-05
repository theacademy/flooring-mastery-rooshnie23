package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDao {
  public static String PRODUCT_FILE;
  public static final String DELIMITER = ",";
  private final Map<Integer, Product> products = new HashMap<>();

  public ProductDaoFileImpl() {
    this.PRODUCT_FILE = "Data/Products.txt";
  }

  @Override
  public Product getProductType(String productType) {
    return null;
  }

  @Override
  public List<Product> getAllProducts() {
    return List.of();
  }

  @Override
  public BigDecimal getCostPerSquareFoot(String productType) {
    return null;
  }

  @Override
  public BigDecimal getLaborCostPerSquareFoot(String productType) {
    return null;
  }
  private Product unmarshallProduct(String productsAsText) {
    String[] productTokens = productsAsText.split(DELIMITER);

    String productType = productTokens[0];

    Product productFromFile = new Product(productType);

    productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
    productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

    return productFromFile;
  }

  private void loadRoster() throws FlooringMasterPersistenceException {
    Scanner scanner;
    int id = 0;

    try {
      // Create Scanner for reading the file
      scanner = new Scanner(new BufferedReader(new FileReader(PRODUCT_FILE)));
    } catch (FileNotFoundException e) {
      throw new FlooringMasterPersistenceException("-_- Could not load roster data into memory.", e);
    }
    // currentLine holds the most recent line read from the file
    String currentLine;
    // currentStudent holds the most recent student unmarshalled
    Product currentProduct;
    // Go through ROSTER_FILE line by line, decoding each line into a
    // Student object by calling the unmarshallStudent method.
    // Process while we have more lines in the file
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      // get the next line in the file
      currentLine = scanner.nextLine();
      // unmarshall the line into a Student
      currentProduct = unmarshallProduct(currentLine);

      // We are going to use the student id as the map key for our student object.
      // Put currentStudent into the map using student id as the key
      products.put(id, currentProduct);
      id++;
    }
    // close scanner
    scanner.close();
  }

  public static void main(String[] args) {
    ProductDaoFileImpl productDao = new ProductDaoFileImpl();
    productDao.loadRoster();
    System.out.println(productDao.products);
  }
}
