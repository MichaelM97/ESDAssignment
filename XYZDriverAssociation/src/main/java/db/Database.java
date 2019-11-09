/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.*;
/**
 * Initialises the Database
 */
public class DatabaseInit {
    static Connection conn = null;
    static Statement statement = null;
    static ResultSet resultSet = null;

    DatabaseInit(){
        initDB();
    }
    
 private static void initDB(){
      
     String foo = ("CREATE TABLE users ("
              + "UID INT NOT NULL,"
              + "NAME VARCHAR(45) NOT NULL)");
     try {
        conn = DriverManager.getConnection(
        "jdbc:derby://localhost:1527/XYZDriverAssociation;create=true",
                "root", "password");
     } catch (SQLException ex){
         // Log out the exception
         System.out.println(ex);
     }

     try {
         if (conn != null){
             statement = conn.createStatement();
             statement.executeUpdate(foo); 
             statement.close();
             conn.close();
         }
     } catch (SQLException ex){
         System.out.println("Create Fail");
        }
     
     }

 public static void main(String[] args){
    new DatabaseInit();
     
 }
 }


