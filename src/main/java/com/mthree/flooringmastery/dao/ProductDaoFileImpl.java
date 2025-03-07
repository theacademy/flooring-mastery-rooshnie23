package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Product;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ProductDaoFileImpl implements ProductDao {
  public static String PRODUCT_FILE;
  public static final String DELIMITER = ",";
  private final Map<String, Product> products = new HashMap<>();

  public ProductDaoFileImpl() {
    this.PRODUCT_FILE = "Data/Products.txt";
    loadProducts();
  }

  @Override
  public Product getProductType(String productType) throws FlooringMasterPersistenceException{
    return products.get(productType);
  }

  @Override
  public List<Product> getAllProducts() throws FlooringMasterPersistenceException{
    return new ArrayList<>(products.values());
  }

  @Override
  public BigDecimal getCostPerSquareFoot(String productType) throws FlooringMasterPersistenceException{
    return products.get(productType).getCostPerSquareFoot();
  }

  @Override
  public BigDecimal getLaborCostPerSquareFoot(String productType) throws FlooringMasterPersistenceException{
    return products.get(productType).getLaborCostPerSquareFoot();
  }
  private Product unmarshallProduct(String productsAsText) {
    String[] productTokens = productsAsText.split(DELIMITER);

    String productType = productTokens[0];

    Product productFromFile = new Product(productType);

    productFromFile.setCostPerSquareFoot(new BigDecimal(productTokens[1]));
    productFromFile.setLaborCostPerSquareFoot(new BigDecimal(productTokens[2]));

    return productFromFile;
  }

  private void loadProducts() throws FlooringMasterPersistenceException {
    Scanner scanner;

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
      products.put(currentProduct.getProductType(), currentProduct);

    }
    // close scanner
    scanner.close();
  }

}
