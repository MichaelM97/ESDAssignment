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
* Database representation.
*/
public class Database {
    private static Database instance = null;
    private static Connection conn = null;
    private static final String DERBY_URL = ("jdbc:derby://localhost:1527/"
            + "XYZDriverAssociation;create=true");
    private static final String ROOT_USR = "root";
    private static final String ROOT_PW = "password";
    private final String DB_INIT_JSON = "src/main/java/db/init_db.json";

    private Database(){
        init_tables();
    }


    /**
    * Singleton DB creation pattern.
    * @return Database
    * Thread Safe Singleton instance of a DB with access methods.
    */
    public static Database get_DB(){
        synchronized(Database.class){
            if(instance == null){
                instance = new Database();
            }
            return instance;
        }
    }

    /**
    * Clear_DB - Deletes all tables
    */
    public void clear_DB(){
        try{
            getConn();
            DatabaseMetaData md = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = md.getTables(null, null, "%", types);
            while (rs.next()) {
                execute_sql("DROP TABLE " + rs.getString("TABLE_NAME"));
            }
        } catch(SQLException ex){
            Logger.getLogger(
                Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        // Rebuild DB
        init_tables();
    }

    /**
    * Singleton Connection creation pattern.
    * @return Connection - 
    * Thread Safe Singleton instance of a connection.
    */
    public static Connection getConn() {
        synchronized(Database.class){
            if(conn == null){
                try{
                    conn = DriverManager.getConnection(
                            DERBY_URL, ROOT_USR, ROOT_PW);
                } catch (SQLException ex){
                    Logger.getLogger(
                        Database.class.getName()).log(
                            Level.SEVERE, null, ex);
                }
            }
        }
        return conn;
        }

   /**
    * Initialises the Database with relevant tables read in from init_db.json
    */
    private void init_tables(){
        String sql;
        boolean first;
        JSONObject j_reader = null;

        // Load JSON DB initialisation layout
        try{
            j_reader = (JSONObject) new JSONParser().parse(
                new FileReader(DB_INIT_JSON));
        } catch(IOException | ParseException ex){
            Logger.getLogger(
                Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        // Loop through table names
        for (Object table_name: j_reader.keySet()){
            sql = "CREATE TABLE ";
            first = true;
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
                 Logger.getLogger(
                    Database.class.getName()).log(
                        Level.SEVERE, null, ex);
             }
        }
    }

    /**
    * Private method for adding SQL to database.
    * @param sql (String) - SQL Query to execute
    * @throws SQLException - Allows caller to handle exception in relevant
    *   fashion.
    */
    public void execute_sql(String sql) throws SQLException{
        Logger.getLogger(
            Database.class.getName()).log(
                Level.INFO, sql);
        getConn();
        conn.createStatement().executeUpdate(sql);
    }
 }
