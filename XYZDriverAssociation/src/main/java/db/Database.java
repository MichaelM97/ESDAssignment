/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;
import java.sql.*;

import java.io.FileReader;

import java.io.IOException;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * Initialises the Database
 */
public class Database {
    private static Database instance = null;
    private static Connection conn = null;
    private final String DERBY_URL = ("jdbc:derby://localhost:1527/"
            + "XYZDriverAssociation;create=true");
    private final String DB_INIT_JSON = "src/main/java/db/init_db.json";
    private final String ROOT_USR = "root";
    private final String ROOT_PW = "password";

    private Database(){
        //Establish Connection
        init_tables();
        
    }

    /**
    * Singleton DB creation pattern.
    * @return Database -
    *   Thread Safe Singleton instance of a DB with access methods.
    */
    public static Database get_DB(){
        // Thread Safe
        synchronized(Database.class){
            if(instance == null){
                instance = new Database();
            }
            return instance;
        }
    }

    /**
    * Singleton Connection creation pattern.
    * @return Connection - 
    *   Thread Safe Singleton instance of a connection.
    */
    private Connection getConn() {
        // Thread Safe 
        synchronized(this){
            if(conn == null){
                try{
                    conn = DriverManager.getConnection(
                            DERBY_URL, ROOT_USR, ROOT_PW);
                } catch (SQLException ex){
                    //log it out
                }
            }
        }
        return conn;
        }

    /**
    * Initialises the Database with relevant tables read in from init_db.json
    */
    private void init_tables(){
        JSONObject j_reader = null;
        // Load JSON DB initialisation layout
        try{
            j_reader = (JSONObject) new JSONParser().parse(
                new FileReader(DB_INIT_JSON));
        } catch(IOException | ParseException ex){
            System.out.println(ex);
        }
        // Loop through table names
        for (Object table_name: j_reader.keySet()){
            String sql = "CREATE TABLE ";
            boolean first = true;
            // Read columns from json by using table name
             sql += (String) table_name + "(";
             Map columns = (Map)j_reader.get(table_name);
             // Generate iterator which contains k,v pairs for sets of table
             // fields.
             Iterator<Map.Entry> cols = columns.entrySet().iterator();
             while (cols.hasNext()){
                 if (first == false){
                     sql += ',';
                 }
                 first = false;
                 Map.Entry pair = cols.next();
                 sql+= ("\n " + pair.getKey() + " " + pair.getValue());
             }
             sql += ")";
             try{
             execute_sql(sql);
             } catch(SQLException ex){
                 System.out.println(ex);
             }
        }
    }
    /**
    * Private method for adding SQL to database, this should not be used
    * generally.
    */
    private void execute_sql(String sql) throws SQLException{
        // Log out what's being created
        System.out.println(sql);
        getConn();
        conn.createStatement().executeUpdate(sql);
    }


 public static void main(String[] args){
    new Database();
     
 }
 }
