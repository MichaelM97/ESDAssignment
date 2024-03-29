package db;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.*;

/**
 * Class which interfaces with the DB. Public facing DB methods should be added
 * here.
 */
public class DatabaseFactory {

    Database db = null;

    public DatabaseFactory() {
        this.db = Database.get_db();
    }

    /**
     * Interface for Database.get_table_columns.
     *
     * @param table_name (String) - Name of table sought.
     * @return ArrayList<String> - List of columns found.
     */
    public ArrayList<String> get_table_columns(String table_name) {
        return db.get_table_columns(table_name);

    }

    /**
     * Interface for Database.delete_from_table.
     *
     * @param table_name (String) - Name of table sought.
     * @param id (String) - ID to delete from table.
     * @return boolean - True if successful, False if not.
     */
    public boolean delete_from_table(String table_name, String id) {
        return db.delete_from_table(table_name, id);
    }

    /**
     * Interface for Database.get_table_names.
     *
     * @return ArrayList<String> - List of table names.
     */
    public ArrayList<String> get_table_names() {
        return db.get_table_names();
    }

    /**
     * Checks if a table is present.
     *
     * @param table_name (String) - Table name to find.
     */
    public boolean table_exists(String table_name) {
        return db.get_table_names().contains(table_name.toUpperCase());
    }

    /**
     * Resets the Database with relevant tables read in from init_db.json.
     */
    public void reset_db() {
        db.initialise(true);
    }

    /**
     * Gets a a field from a table.
     *
     * @param table_name (String) - Table name.
     * @param id (String) - Primary key in table or * for all.
     * @return ResultSet - Results of query.
     */
    public ResultSet get_from_table(String table_name, String id) {
        if (table_exists(table_name)) {
            return db.select_from(table_name, id);
        }
        return null;
    }

    /**
     * Interface for carrying out an insert.
     *
     * @param entry (Object) - Either of type Claim, Payment, User.
     * @return True if successful, False if not
     */
    public boolean insert(Object entry) {
        if (entry instanceof Claim) {
            return db.insert_claim((Claim) entry);
        } else if (entry instanceof Payment) {
            return db.insert_payment((Payment) entry);
        } else if (entry instanceof User) {
            return db.insert_user((User) entry);
        }
        return false;
    }

    /**
     * Interface for carrying out an update.
     *
     * @param entry (Object) - Either of type Claim, Payment, User.
     * @return True if successful, False if not
     */
    public boolean update(Object entry) {
        if (entry instanceof Claim) {
            return db.update_claim((Claim) entry);
        } else if (entry instanceof Payment) {
            return db.update_payment((Payment) entry);
        } else if (entry instanceof User) {
            return db.update_user((User) entry);
        }
        return false;
    }

    /**
     * Interface for carrying out a search.
     *
     * @param entry (String) - The search string.
     * @return Search results
     */
    public ResultSet user_search(String entry) {
        return db.search(entry);
    }
}
