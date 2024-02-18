package com.example.banking_system_fx;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {
   private static Connection connection;

   public static Connection getConnection() {
      if (connection == null) {
         try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/bankingsystem_db";
            String username = "root";
            String password = "admin";
            System.out.println("Connection started...");
            connection = DriverManager.getConnection(url, username, password);
         } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
         }
      }
      return connection;
   }

   public static void addAccountToDatabase(Customer customer) {
      try (Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(
                   "INSERT INTO accounts (name, cnp, age, sex, balance) VALUES (?, ?, ?, ?, ?)",
                   Statement.RETURN_GENERATED_KEYS)) {

         if (isValidCustomer(customer)) {
            statement.setString(1, customer.getCustomerName());
            statement.setString(2, customer.getCNP());
            statement.setInt(3, customer.getAge());
            statement.setString(4, customer.getSex());
            statement.setDouble(5, 0.0); // Initial balance is set to 0

            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int accountId = -1; // Initialize with a default value
            if (generatedKeys.next()) {
               accountId = generatedKeys.getInt(1);
               System.out.println("Account added to database with ID: " + accountId);
            }

            // Now insert the user with the obtained account ID
            try (PreparedStatement userStatement = connection.prepareStatement(
                    "INSERT INTO users (username, password, idaccount, role) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

               userStatement.setString(1, customer.getUsername());
               userStatement.setString(2, customer.getPassword());
               userStatement.setInt(3, accountId);
               userStatement.setString(4, customer.getRole());

               // Execute the user statement to insert into the users table
               userStatement.executeUpdate();

               // Get the generated keys for the users table
               try (ResultSet userGeneratedKeys = userStatement.getGeneratedKeys()) {
                  if (userGeneratedKeys.next()) {
                     int userId = userGeneratedKeys.getInt(1);
                     System.out.println("User added to database with ID: " + userId);
                  } else {
                     throw new SQLException("Failed to get the generated keys for the user");
                  }
               }
            }
         } else {
            System.out.println("Invalid customer data. Account not added to the database.");
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

   public static Customer getAccountById(int accountId) {
      try (Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(
                   "SELECT * FROM accounts WHERE id = ?")) {

         statement.setInt(1, accountId);

         try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
               String name = resultSet.getString("name");
               String cnp = resultSet.getString("cnp");
               int age = resultSet.getInt("age");
               String sex = resultSet.getString("sex");

               return new Customer(name, cnp, age, sex);
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return null; // Return null if no account is found for the given ID
   }

   public static boolean isUsernameExists(Connection connection, String username) {
      try (PreparedStatement statement = connection.prepareStatement(
              "SELECT COUNT(*) AS count FROM users WHERE username = ?")) {

         statement.setString(1, username);

         try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
               int count = resultSet.getInt("count");
               return count > 0;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return false;
   }

   public static void addUserToDatabase(Customer customer) {
      try (Connection connection = getConnection()) {
         System.out.println(1);
         if (isUsernameExists(connection, customer.getUsername())) {
            System.out.println("Username already exists. Please choose a different one.");
            return;
         }
         try {
            if (isValidCustomer(customer)) {
               int accountId = -1; // Initialize with a default value

               // Continue with adding the account if the username is unique
               try (PreparedStatement accountStatement = connection.prepareStatement(
                       "INSERT INTO accounts (name, cnp, age, sex, balance) VALUES (?, ?, ?, ?, ?)",
                       Statement.RETURN_GENERATED_KEYS)) {

                  accountStatement.setString(1, customer.getCustomerName());
                  accountStatement.setString(2, customer.getCNP());
                  accountStatement.setInt(3, customer.getAge());
                  accountStatement.setString(4, customer.getSex());
                  accountStatement.setDouble(5, 0.0); // Initial balance is set to 0

                  // Execute the account statement to insert into the accounts table
                  accountStatement.executeUpdate();

                  // Get the generated keys for the accounts table
                  try (ResultSet accountGeneratedKeys = accountStatement.getGeneratedKeys()) {
                     if (accountGeneratedKeys.next()) {
                        accountId = accountGeneratedKeys.getInt(1);
                        System.out.println("Account added to database with ID: " + accountId);
                     } else {
                        throw new SQLException("Failed to get the generated keys for the account");
                     }
                  }
               }

               // Now insert the user with the obtained account ID
               try (PreparedStatement userStatement = connection.prepareStatement(
                       "INSERT INTO users (username, password, idaccount, role) VALUES (?, ?, ?, ?)",
                       Statement.RETURN_GENERATED_KEYS)) {

                  userStatement.setString(1, customer.getUsername());
                  userStatement.setString(2, customer.getPassword());
                  userStatement.setInt(3, accountId);
                  userStatement.setString(4, customer.getRole());

                  // Execute the user statement to insert into the users table
                  userStatement.executeUpdate();

                  // Get the generated keys for the users table
                  try (ResultSet userGeneratedKeys = userStatement.getGeneratedKeys()) {
                     if (userGeneratedKeys.next()) {
                        int userId = userGeneratedKeys.getInt(1);
                        System.out.println("User added to database with ID: " + userId);
                     } else {
                        throw new SQLException("Failed to get the generated keys for the user");
                     }
                  }
               }
            }
         }catch (SQLException e) {
            e.printStackTrace();
         }
      } catch (SQLException e) {
         throw new RuntimeException(e);
      }
   }


   public static boolean validateUserCredentials(String username, String password, String role) {
      try (PreparedStatement statement = Database.getConnection().prepareStatement(
              "SELECT * FROM users WHERE username = ? AND password = ? AND role = ?")) {

         statement.setString(1, username);
         statement.setString(2, password);
         statement.setString(3, role);

         try (ResultSet resultSet = statement.executeQuery()) {
            return resultSet.next();
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return false;
   }

   public static double getBalance(String username, String role) {
      try (Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(
                   "SELECT balance FROM accounts WHERE id = (SELECT idaccount FROM users WHERE username = ?)")) {

         statement.setString(1, username);

         try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
               return resultSet.getDouble("balance");
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
      return -1;
   }

   public static void deposit(Connection connection, BankAccount bankAccount, double amount) {
      try {
         // The following line is not needed, as you're already passing the connection as a parameter
         // connection = getConnection();

         try (PreparedStatement updateStatement = connection.prepareStatement(
                 "UPDATE accounts SET balance = balance + ? WHERE id = ?")) {
            updateStatement.setDouble(1, amount);
            updateStatement.setInt(2, bankAccount.getAccountID());
            updateStatement.executeUpdate();
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }


   public static void withdraw(Connection connection, BankAccount bankAccount, double amount) {
      try {
         // The following line is not needed, as you're already passing the connection as a parameter
         // connection = getConnection();

         try (PreparedStatement updateStatement = connection.prepareStatement(
                 "UPDATE accounts SET balance = balance - ? WHERE id = ?")) {
            updateStatement.setDouble(1, amount);
            updateStatement.setInt(2, bankAccount.getAccountID());
            updateStatement.executeUpdate();
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }
   }

      private static boolean isValidCustomer(Customer customer) {
         // Implement your validation logic here
         // For example, check if required fields are not null or empty
         // Check if age is within a valid range, etc.
         // Also, check data types of the fields

         return customer != null &&
                 isValidString(customer.getCustomerName()) &&
                 isValidString(customer.getCNP()) &&
                 isValidInt(customer.getAge()) &&
                 isValidString(customer.getSex());
      }

      private static boolean isValidString(String value) {
         // Check if the string is not null and not empty
         return value != null && !value.isEmpty();
      }

      private static boolean isValidInt(int value) {
         // Implement additional validation for int values if needed
         // For example, check if the age is within a valid range
         return value > 0 && value < 150; // Example age validation
      }

   public static BankAccount getAccountByUsername(Connection connection, String username) {
      try (
           PreparedStatement statement = connection.prepareStatement(
                   "SELECT a.*, u.* FROM accounts a " +
                           "JOIN users u ON a.id = u.idaccount " +
                           "WHERE u.username = ?")) {

         statement.setString(1, username);

         try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
               int accountId = resultSet.getInt("id");
               String name = resultSet.getString("name");
               String cnp = resultSet.getString("cnp");
               int age = resultSet.getInt("age");
               String sex = resultSet.getString("sex");
               double balance = resultSet.getDouble("balance");
               String role = resultSet.getString("role");

               Customer customer = new Customer(name, cnp, age, sex, username, "", role);
               BankAccount bankAccount = new BankAccount(customer);
               bankAccount.setAccountID(accountId);
               bankAccount.setBalance(balance);

               return bankAccount;
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return null;
   }


   public static void closeConnection() {
      if (connection != null) {
         try {
            connection.close();
            System.out.println("Connection closed.");
         } catch (SQLException e) {
            e.printStackTrace();
         }
      }
   }

   public static List<Double> getAllUserBalances() {
      List<Double> userBalances = new ArrayList<>();

      try (Connection connection = getConnection();
           PreparedStatement statement = connection.prepareStatement(
                   "SELECT balance FROM accounts")) {

         try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
               double balance = resultSet.getDouble("balance");
               userBalances.add(balance);
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
      }

      return userBalances;
   }
}