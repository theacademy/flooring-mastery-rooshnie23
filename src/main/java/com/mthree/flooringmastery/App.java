package com.mthree.flooringmastery;

import com.mthree.flooringmastery.controller.FlooringMasteryController;
import com.mthree.flooringmastery.dao.OrderDao;
import com.mthree.flooringmastery.dao.OrderDaoFileImpl;
import com.mthree.flooringmastery.dao.ProductDao;
import com.mthree.flooringmastery.dao.ProductDaoFileImpl;
import com.mthree.flooringmastery.dao.TaxDao;
import com.mthree.flooringmastery.dao.TaxDaoFileImpl;
import com.mthree.flooringmastery.service.FlooringMasteryService;
import com.mthree.flooringmastery.service.FlooringMasteryServiceImpl;
import com.mthree.flooringmastery.ui.FlooringMasteryView;
import com.mthree.flooringmastery.ui.UserIO;
import com.mthree.flooringmastery.ui.UserIOConsoleImpl;

public class App {

  public static void main(String[] args) {
    System.out.println("Hello world!");
     UserIO myIo = new UserIOConsoleImpl();
     FlooringMasteryView myView = new FlooringMasteryView(myIo);
     OrderDao myDao = new OrderDaoFileImpl();
     TaxDao myTaxDao = new TaxDaoFileImpl();
     ProductDao myProductDao = new ProductDaoFileImpl();
     FlooringMasteryService myService = new FlooringMasteryServiceImpl(myDao, myTaxDao, myProductDao);
     FlooringMasteryController controller = new FlooringMasteryController(myService, myView);
     controller.run();
  }
}