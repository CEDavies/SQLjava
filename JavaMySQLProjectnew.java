package com.mysql.javamysqlproject;
import java.util.*; import java.sql.*;
import java.math.BigDecimal;

public class JavaMySQLProjectnew {
    public static void main(String[] args) { 
        Scanner scnr = new Scanner(System.in);
        System.out.println("Please enter your last name");
        String last = scnr.nextLine();
        System.out.println("Please enter your first name");
        String first = scnr.nextLine();
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:4242/cfg?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "groot", "iamgroot");
            Statement stmt = conn.createStatement();
            )
        {
            String nameQuery = "SELECT LastName, FirstName FROM Rep WHERE LastName = '" + last + "' AND FirstName = '" + first + "'";
            ResultSet rset = stmt.executeQuery(nameQuery);
            int rowCount = 0;
            while(rset.next()) {
                System.out.println("");
                System.out.println("Success! We found a representative named "+rset.getString("FirstName")+" "+rset.getString("LastName")+".");
                ++rowCount;
            }
            
            if(rowCount == 0){
            System.out.println("There is no representative with that name!");
            Task5();
            }
            System.out.println("Please enter the password.");
            String password = scnr.nextLine();
            if (password.equals("pineapple")) {
                System.out.println("Correct password! Welcome!");
            } else {
                  Task5();
                  }
          } catch(SQLException ex) {
            }
        
        boolean valid = false;
        while(!valid) {               
            System.out.println("Welcome! Please choose from the following options: ");
            System.out.println("Press 1 to search for a representative by first and last name, and generate a report listing the rep's number of customers and their average balance.");
            System.out.println("Press 2 to search for a customer, and generate a report showing the total quoted price of all the customer's orders.");
            System.out.println("Press 3 to add a new representative.");
            System.out.println("Press 4 to search for a customer, and update the customer’s credit limit.");
            System.out.println("Press 5 to exit the system.");
            System.out.println("");
            int choice = scnr.nextInt();
            switch (choice) {
                case 1:
                    Task1();
                    break;
                    case 2:
                    Task2();
                    break;
                case 3:
                    Task3();
                    break;
                case 4:
                    Task4();
                    break;
                case 5:
                    Task5();
                    break;
                default:
                    System.out.println("That was not a valid option!");
                    System.out.println("");
            }
            
            Scanner scnnr = new Scanner(System.in);
            System.out.println("Would you like to return to the main menu? Y/N"); System.out.println("");
            String userChoice = scnnr.nextLine();
            if (userChoice.equalsIgnoreCase("y")) {
                System.out.println("");
            }
            if (userChoice.equalsIgnoreCase("n")) {
                System.out.println("");
                Task5();
            }
        }   
    }
    public static void Task1() {
        Scanner scnr = new Scanner(System.in);
        System.out.println(""); System.out.println("Please enter a representative's last name:"); System.out.println("");
        String repLastName = scnr.nextLine();
        System.out.println(""); System.out.println("Please enter a representative's first name:"); System.out.println("");
        String repFirstName = scnr.nextLine();

        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:4242/cfg?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "groot", "iamgroot");
            Statement stmt = conn.createStatement();
            )
        {
            String nameQuery = "SELECT LastName, FirstName FROM Rep WHERE LastName = '" + repLastName + "' AND FirstName = '" + repFirstName + "'";
            ResultSet rset = stmt.executeQuery(nameQuery);
            int rowCount = 0;
            while(rset.next()) {
                System.out.println("");
                System.out.println("Success! We found a representative named "+rset.getString("FirstName")+" "+rset.getString("LastName")+".");
                ++rowCount;
            }
            
            if(rowCount == 0){
            System.out.println("There is no representative with that name!");
            return;
            }
            
            int repNum = 0;
            nameQuery = "SELECT RepNum FROM Rep WHERE LastName = '" + repLastName + "' AND FirstName = '" + repFirstName + "'";
            rset = stmt.executeQuery(nameQuery);
            rowCount = 0;
            while(rset.next()) {
                repNum = rset.getInt("RepNum");
                ++rowCount;
            }
        
            String strSelect = "SELECT count(*) FROM Customer WHERE RepNum = '" + repNum + "'";
            rset = stmt.executeQuery(strSelect);
            rowCount = 0;
            while(rset.next()) {
                System.out.println("This representative has "+rset.getInt("count(*)")+" customers.");
                ++rowCount;
            }
            
            strSelect = "select avg(balance) from customer where repnum = '" + repNum + "'";
            rset = stmt.executeQuery(strSelect);
            rowCount = 0;
            while(rset.next()) {
               System.out.println("This representative's customer(s) have an average balance of "+rset.getInt("avg(balance)")+".");
                System.out.println("");
               ++rowCount;
            }
        } catch(SQLException ex) {
        }
    }
    public static void Task2() {
    
        Scanner scnr = new Scanner(System.in);
        System.out.println("");
        System.out.println("Please enter the customer's name:");
        System.out.println("");
        String custName = scnr.nextLine();
        
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:4242/cfg?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "groot", "iamgroot");
            Statement stmt = conn.createStatement();
            )
        {
            String nameQuery = "SELECT CustomerName FROM Customer WHERE CustomerName = '" + custName + "'";
            ResultSet rset = stmt.executeQuery(nameQuery);
            int rowCount = 0;
            while(rset.next()) {
                System.out.println("");
                System.out.println("Success! We found a customer named "+rset.getString("CustomerName")+".");
                ++rowCount;
            }
            
            if(rowCount == 0){
            System.out.println("There is no customer with that name!");
            return;
            }
            
            nameQuery = "SELECT CustomerNum FROM Customer WHERE CustomerName = '" + custName + "'";
            rset = stmt.executeQuery(nameQuery);
            int custNum = 0;
            rowCount = 0;
            while(rset.next()) {
                custNum = rset.getInt("CustomerNum");
                ++rowCount;
            }
            
            String totalQuotedPrice = "SELECT SUM(NumOrdered * QuotedPrice) FROM orders Left JOIN orderline ON orders.ordernum = orderline.ordernum WHERE orders.CustomerNum = '" + custNum + "';";
            rset = stmt.executeQuery(totalQuotedPrice);
            rowCount = 0;
            while(rset.next()) {
                System.out.println("");
                System.out.println("The customer's total quoted price is "+rset.getString("SUM(NumOrdered * QuotedPrice)"));
                ++rowCount;
            }
            } catch(SQLException ex) {
        }
    }
    public static void Task3() {
            Scanner scnr = new Scanner(System.in);
            
            System.out.println("");
            System.out.println("Please enter the new representative's representative number:"); System.out.println("");
            String newRepRepNum = scnr.nextLine();
            
            System.out.println("");
            System.out.println("Please enter the new representative's last name:"); System.out.println("");
            String newRepLastName = scnr.nextLine();
            
            System.out.println("");
            System.out.println("Please enter the new representative's first name:"); System.out.println("");
            String newRepFirstName = scnr.nextLine();

            System.out.println("");
            System.out.println("Please enter the new representative's street address:"); System.out.println("");
            String newRepStreet = scnr.nextLine();

            System.out.println("");
            System.out.println("Please enter the new representative's city:"); System.out.println("");
            String newRepCity = scnr.nextLine();

            System.out.println("");
            System.out.println("Please enter the new representative's state:"); System.out.println("");
            String newRepState = scnr.nextLine();

            System.out.println("");
            System.out.println("Please enter the new representative's 5 digit postal code:"); System.out.println("");
            String newRepPostalCode = scnr.nextLine();

            System.out.println("");
            System.out.println("Please enter the new representative's commission:"); System.out.println("");
            BigDecimal newRepCommission = scnr.nextBigDecimal();

            System.out.println("");
            System.out.println("Please enter the new representative's rate:"); System.out.println("");
            BigDecimal newRepRate = scnr.nextBigDecimal();  
            
            try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:4242/cfg?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "groot", "iamgroot");
               Statement stmt = conn.createStatement();
                ){
                
                String insertQuery = "INSERT INTO Rep (RepNum, LastName, FirstName, Street, City, State, PostalCode, Commission, Rate)"
                     + " VALUES ('" + newRepRepNum + "','" + newRepLastName + "','" + newRepFirstName + "','" + newRepStreet + "','" + newRepCity + "','" + newRepState + "','" + newRepPostalCode + "','" + newRepCommission + "','" + newRepRate+"');"; 
                     
                     System.out.println(insertQuery);
                     stmt.execute(insertQuery);
                     System.out.println("Success!");
                     
    } catch(SQLException ex) {
    }
}
    public static void Task4() {
        Scanner scnr = new Scanner(System.in);
        System.out.println("");
        System.out.println("Please enter the customer's name:");System.out.println("");
        String custName = scnr.nextLine();
        
        try (
            Connection conn = DriverManager.getConnection(
               "jdbc:mysql://localhost:4242/cfg?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
               "groot", "iamgroot");
            Statement stmt = conn.createStatement();
            )
        {
            String nameQuery = "SELECT CustomerName FROM Customer WHERE CustomerName = '" + custName + "'";
            ResultSet rset = stmt.executeQuery(nameQuery);
            int rowCount = 0;
            while(rset.next()) {
                System.out.println("");
                System.out.println("Success! We found a customer named "+rset.getString("CustomerName")+".");
                ++rowCount;
            }
            
            if(rowCount == 0){
            System.out.println("");
            System.out.println("There is no customer with that name!");
            return;
            }
            
            System.out.println("Please enter the new credit limit for that customer:"); System.out.println("");
            int newCreditLimit = scnr.nextInt();

            String creditLimitUpdate = "UPDATE customer SET CreditLimit='"+newCreditLimit+"' WHERE CustomerName='" + custName + "';";
            stmt.execute(creditLimitUpdate);
            System.out.println("");
            System.out.println("Success! The customer's credit limit has been updated.");
            
            } catch(SQLException ex) {
        }
    }
    public static void Task5() {
        System.out.println("");
        System.out.println("Goodbye!");
        System.exit(0);
    }
}