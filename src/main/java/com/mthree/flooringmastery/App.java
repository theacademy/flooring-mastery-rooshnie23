package com.mthree.flooringmastery;

import com.mthree.flooringmastery.controller.FlooringMasteryController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
  public static void main(String[] args) {

    ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

    // Get the controller bean
    FlooringMasteryController controller = ctx.getBean("controller", FlooringMasteryController.class);

    // Run the application
    controller.run();
  }
}
