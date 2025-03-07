package com.mthree.flooringmastery.dao;

import com.mthree.flooringmastery.model.Order;
import java.io.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

public class OrderDaoFileImpl implements OrderDao {
  public static final String DELIMITER = ",";
  private final Map<String, Order> orders = new HashMap<>();
  private TaxDao taxDao = new TaxDaoFileImpl();
  private ProductDao productDao = new ProductDaoFileImpl();
 private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddyyyy");


  @Override
  public List<Order> getOrdersByDate(LocalDate date) throws FlooringMasterPersistenceException {
    if(doesOrderFileExist(date)){
      loadOrders("Orders/Orders_" + date.format(formatter) + ".txt");
    }
    return new ArrayList<>(orders.values());
  }

  @Override
  public Order getOrder(String orderNumber,LocalDate date) throws FlooringMasterPersistenceException {
    getOrdersByDate(date);
    return orders.get(orderNumber);
  }

  @Override
  public void addOrder(String orderNumber, Order order) throws FlooringMasterPersistenceException {
    orders.put(orderNumber, order);
    getOrdersByDate(order.getOrderDate());
    writeOrder(order.getOrderDate());
  }

  @Override
  public void editOrder(String orderNumber, LocalDate date, Order newOrder) throws FlooringMasterPersistenceException{
    getOrdersByDate(date);
    orders.remove(orderNumber);
    orders.put(newOrder.getOrderNumber(), newOrder);
    writeOrder(date);

  }

  @Override
  public void removeOrder(String orderNumber, LocalDate date) throws FlooringMasterPersistenceException{
    if (doesOrderFileExist(date)) {
      loadOrders("Orders/Orders_" + date.format(formatter) + ".txt");
      orders.remove(orderNumber);
      writeOrder(date);
    }
  }

  @Override
  public void exportAllData() throws FlooringMasterPersistenceException{
    File ordersDirectory = new File("Orders/");
    File[] orderFiles = ordersDirectory.listFiles((dir, name) -> name.startsWith("Orders_") && name.endsWith(".txt"));

    if (orderFiles == null || orderFiles.length == 0) {
      throw new FlooringMasterPersistenceException("No orders found to export.");
    }

    File exportFileLocation = new File("AllOrders.txt");

    try (PrintWriter out = new PrintWriter(new FileWriter(exportFileLocation))) {
      // Write header
      out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot," +
          "LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");

      for (File file : orderFiles) {
        loadOrders(file.getAbsolutePath()); // Load orders into memory

        for (Order order : orders.values()) {
          String orderAsText = marshallOrder(order);
          out.println(orderAsText);
        }
      }
      out.flush();
    } catch (IOException e) {
      throw new FlooringMasterPersistenceException("Could not export orders.", e);
    }
  }

  public boolean doesOrderFileExist(LocalDate date) {
    return new File("Orders/Orders_" + date.format(formatter) + ".txt").exists();
  }

  private Order unmarshallOrder(String orderAsText, String orderFile) {
    String[] orderTokens = orderAsText.split(DELIMITER);

    String orderNumber = orderTokens[0];

    Order orderFromFile = new Order();
    orderFromFile.setOrderNumber(orderNumber);
    orderFromFile.setCustomerName(orderTokens[1]);
    orderFromFile.setState(orderTokens[2]);
    orderFromFile.setTax(taxDao.getTaxByState(orderTokens[2]));
    orderFromFile.setProduct(productDao.getProductType(orderTokens[4]));
    orderFromFile.setArea(new BigDecimal(orderTokens[5]));
    orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
    orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
    orderFromFile.setTaxAmount(new BigDecimal(orderTokens[10]));
    orderFromFile.setTotalCost(new BigDecimal(orderTokens[11]));
    //this is to extract the date from the file name
    LocalDate orderDate = Pattern.compile("_(\\d{8})\\.txt$")
        .matcher(orderFile)
        .results()
        .map(m -> LocalDate.parse(m.group(1), DateTimeFormatter.ofPattern("MMddyyyy")))
        .findFirst()
        .orElse(null);
    orderFromFile.setOrderDate(orderDate);

    return orderFromFile;
  }

  private void loadOrders(String orderFile) throws FlooringMasterPersistenceException {
    Scanner scanner;

    try {
      // Create Scanner for reading the file
      scanner = new Scanner(new BufferedReader(new FileReader(orderFile)));
    } catch (FileNotFoundException e) {
      throw new FlooringMasterPersistenceException("-_- Could not load roster data into memory.", e);
    }
    // currentLine holds the most recent line read from the file
    String currentLine;
    // currentStudent holds the most recent student unmarshalled
    Order currentOrder;

    scanner.nextLine();
    while (scanner.hasNextLine()) {
      // get the next line in the file
      currentLine = scanner.nextLine();
      // unmarshall the line into a Student
      currentOrder = unmarshallOrder(currentLine,orderFile);

      orders.put(currentOrder.getOrderNumber(), currentOrder);
    }
    // close scanner
    scanner.close();
  }

  private String marshallOrder(Order aOrder) {
    String orderAsText = aOrder.getOrderNumber() + DELIMITER;

    orderAsText += aOrder.getCustomerName() + DELIMITER;

    orderAsText += aOrder.getState() + DELIMITER;
    orderAsText += aOrder.getTax().getTaxRate() + DELIMITER;
    orderAsText += aOrder.getProduct().getProductType() + DELIMITER;
    orderAsText += aOrder.getArea() + DELIMITER;
    orderAsText += aOrder.getProduct().getCostPerSquareFoot() + DELIMITER;
    orderAsText += aOrder.getProduct().getLaborCostPerSquareFoot() + DELIMITER;
    orderAsText += aOrder.getMaterialCost() + DELIMITER;
    orderAsText += aOrder.getLaborCost() + DELIMITER;
    orderAsText += aOrder.getTaxAmount() + DELIMITER;

    orderAsText += aOrder.getTotalCost();

    return orderAsText;
  }

  private void writeOrder(LocalDate date) throws FlooringMasterPersistenceException {
    PrintWriter out;

    try {
      out = new PrintWriter(new FileWriter("Orders/Orders_" + date.format(formatter) + ".txt"));
    } catch (IOException e) {
      throw new FlooringMasterPersistenceException("Could not save student data.", e);
    }
    String studentAsText;
    List<Order> orderList = new ArrayList<>(orders.values());
    out.println("OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total");
    for (Order currentOrder : orderList) {
      // turn a Student into a String
      studentAsText = marshallOrder(currentOrder);
      // write the Student object to the file
      out.println(studentAsText);
      // force PrintWriter to write line to the file
      out.flush();
    }
    // Clean up
    out.close();
  }
}
