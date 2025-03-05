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
  public List<Order> getOrdersByDate(LocalDate date) {
    if(!doesOrderFileExist(date)){
      loadOrders("Orders/Orders_" + date.format(formatter) + ".txt");
    }
    return new ArrayList<>(orders.values());
  }

  @Override
  public Order getOrder(String orderNumber) {
    return orders.get(orderNumber);
  }

  @Override
  public void addOrder(String orderNumber, Order order) {
    orders.put(orderNumber, order);
    writeOrder(order.getOrderDate());
  }

  @Override
  public void editOrder(String orderNumber, LocalDate date) { //maybe the object

  }

  @Override
  public void removeOrder(String orderNumber, LocalDate date) {

  }

  @Override
  public void saveOrder() {

  }

  public boolean doesOrderFileExist(LocalDate date) {
    return new File("Orders/Orders_" + date.format(formatter) + ".txt").exists();
  }

  private Order unmarshallOrder(String orderAsText, String orderFile) {
    String[] orderTokens = orderAsText.split(DELIMITER);

    String orderNumber = orderTokens[0];

    Order orderFromFile = new Order(orderNumber);
    orderFromFile.setCustomerName(orderTokens[1]);
    orderFromFile.setState(orderTokens[2]);
    orderFromFile.setTax(taxDao.getTaxByState(orderTokens[2]));
    orderFromFile.setProduct(productDao.getProductType(orderTokens[4]));
    orderFromFile.setArea(new BigDecimal(orderTokens[5]));
    orderFromFile.setMaterialCost(new BigDecimal(orderTokens[8]));
    orderFromFile.setLaborCost(new BigDecimal(orderTokens[9]));
    orderFromFile.setTaxAmount(new BigDecimal(orderTokens[10]));
    orderFromFile.setTotalCost(new BigDecimal(orderTokens[11]));
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
    List<Order> orderList = this.getOrdersByDate(date);
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

//public static void main(String[] args) {
//    OrderDaoFileImpl orderDao = new OrderDaoFileImpl();
//   // orderDao.loadOrders("Orders/Orders_06022013.txt");
//  Order orderFromFile = new Order("1");
//  orderFromFile.setCustomerName("m");
//  orderFromFile.setState("CA");
//  orderFromFile.setTax(new Tax(orderFromFile.getState()));
//  orderFromFile.setProduct(new Product("Tile"));
//  orderFromFile.setArea(new BigDecimal("1.4"));
//  orderFromFile.setMaterialCost(new BigDecimal("1.4"));
//  orderFromFile.setLaborCost(new BigDecimal("1.4"));
//  orderFromFile.setTaxAmount(new BigDecimal("1.4"));
//  orderFromFile.setTotalCost(new BigDecimal("1.4"));
//  orderFromFile.setOrderDate(LocalDate.now());
//  orderDao.addOrder("1",orderFromFile);
//
//  orderDao.addOrder("1", orderFromFile);
//    System.out.println(orderDao.orders);
//    System.out.println(orderDao.doesOrderFileExist(LocalDate.now()));
//}
}
