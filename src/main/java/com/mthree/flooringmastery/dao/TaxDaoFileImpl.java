package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Tax;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TaxDaoFileImpl implements TaxDao {
  public static String TAX_FILE;
  public static final String DELIMITER = ",";
  private final Map<String, Tax> taxes = new HashMap<>();

  public TaxDaoFileImpl() {
    this.TAX_FILE = "Data/Taxes.txt";
    loadTaxes();
  }

  @Override
  public Tax getTaxByState(String stateAbbr) {
    return taxes.get(stateAbbr);
  }

  @Override
  public String getStateName() {
    return "";
  }

  @Override
  public String getStateAbbr() {
    return "";
  }

  private Tax unmarshallTax(String taxesAsText) {
    String[] taxTokens = taxesAsText.split(DELIMITER);

    String stateName = taxTokens[1];

    Tax taxFromFile = new Tax(stateName);

    taxFromFile.setStateAbbreviation(taxTokens[0]);
    taxFromFile.setTaxRate(new BigDecimal(taxTokens[2]));

    return taxFromFile;
  }

  private void loadTaxes() throws FlooringMasterPersistenceException {
    Scanner scanner;

    try {
      // Create Scanner for reading the file
      scanner = new Scanner(new BufferedReader(new FileReader(TAX_FILE)));
    } catch (FileNotFoundException e) {
      throw new FlooringMasterPersistenceException("-_- Could not load roster data into memory.", e);
    }
    // currentLine holds the most recent line read from the file
    String currentLine;
    // currentStudent holds the most recent student unmarshalled
    Tax currentTax;
    // Go through ROSTER_FILE line by line, decoding each line into a
    // Student object by calling the unmarshallStudent method.
    // Process while we have more lines in the file
    scanner.nextLine();
    while (scanner.hasNextLine()) {
      // get the next line in the file
      currentLine = scanner.nextLine();
      // unmarshall the line into a Student
      currentTax = unmarshallTax(currentLine);

      // We are going to use the student id as the map key for our student object.
      // Put currentStudent into the map using student id as the key
      taxes.put(currentTax.getStateAbbreviation(), currentTax);
    }
    // close scanner
    scanner.close();
  }

}
