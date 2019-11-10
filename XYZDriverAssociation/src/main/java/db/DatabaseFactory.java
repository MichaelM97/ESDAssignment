package db;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
* Class which interfaces with the DB.
* Public facing DB methods should be added here.
*/
public class DatabaseFactory {
    Database db = null;

    public DatabaseFactory(){
        this.db = Database.get_db();
    }
    
   /**
    * Interface for Database.get_table_columns.
    * @param table_name (String) - Name of table sought.
    * @return ArrayList<String> - List of columns found.
    */
   public ArrayList<String> get_table_columns(String table_name){
       return db.get_table_columns(table_name);
   }

   /**
    * Interface for Database.delete_from_table.
    * @param table_name (String) - Name of table sought.
    * @param id (String) - ID to delete from table.
    * @return boolean - True if successful, False if not.
    */
   public boolean delete_from_table(String table_name, String id){
       return db.delete_from_table(table_name, id);
   }

   /**
    * Interface for Database.get_table_names.
    * @return ArrayList<String> - List of table names.
    */
   public ArrayList<String> get_table_names(){
       return db.get_table_names();
   }

   /**
    * Checks if a table is present.
    * @param table_name (String) - Table name to find.
    */
   public boolean table_exists(String table_name){
       return db.get_table_names().contains(table_name.toUpperCase());
   }

   /**
    * Initialises the Database with relevant tables read in from init_db.json.
    */
   public void init(){
       db.initialise(true);
   }
   
   /**
    * Gets a a field from a table.
    * @param table_name (String) - Table name.
    * @param id (String) - Primary key in table or * for all.
    * @return ResultSet - Results of query. 
    */
   public ResultSet get_from_table(String table_name, String id){
       if(table_exists(table_name)){
           return db.select_from(table_name, id);
       }
       return null;
   }

   /**
    * Interface for carrying out an insert.
    * @param table_name (String) - Name of a table.
    * @param vals (ArrayList <Object>) - Fields to be added to specified table.
    * @return True if successful, False if not
    */
   public boolean insert(String table_name, ArrayList <Object> vals){
       if (table_exists(table_name)){
           switch(table_name.toLowerCase()){
               case "users":
                   return db.insert_user(vals);
               case "members":
                   return db.insert_members(vals);
               case "payments":
                   return db.insert_payments(vals);
               case "claims":
                   return db.insert_claims(vals);
           }
       }
       return false;
   }
}
