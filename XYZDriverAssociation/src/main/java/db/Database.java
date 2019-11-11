package db;

import model.*;

import java.sql.*;

import java.io.FileReader;

import java.io.IOException;

import java.nio.file.Paths;
import java.util.ArrayList;

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
    private final String DB_JSON = "src/main/java/db/init_db.json";
    private final String JSON_PATH = (Paths.get(".").toAbsolutePath().toString() + DB_JSON);

    private Database() {
        getConn();
    }

    /**
     * Singleton db creation pattern.
     *
     * @return Database Thread Safe Singleton instance of a db with access
     * methods.
     */
    protected static Database get_db() {
        synchronized (Database.class) {
            if (instance == null) {
                instance = new Database();
            }
            return instance;
        }
    }

    /**
     * Clear_db - Deletes all tables
     */
    private void clear_db() {
        try {
            DatabaseMetaData md = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = md.getTables(null, null, "%", types);
            while (rs.next()) {
                execute_sql("DROP TABLE " + rs.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
    }

    /**
     * Singleton Connection creation pattern.
     *
     * @return Connection - Thread Safe Singleton instance of a connection.
     */
    public static Connection getConn() {
        synchronized (Database.class) {
            if (conn == null) {
                try {
                    conn = DriverManager.getConnection(
                            DERBY_URL, ROOT_USR, ROOT_PW);
                } catch (SQLException ex) {
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
     *
     * @param clear (boolean) - Resets the DB to original state.
     */
    protected void initialise(boolean clear) {
        if (clear) {
            clear_db();
        }
        String sql;
        boolean first;
        JSONObject j_reader = null;

        // Load JSON DB initialisation layout
        try {
            j_reader = (JSONObject) new JSONParser().parse(
                    new FileReader(DB_JSON));
        } catch (IOException | ParseException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        // Loop through table names
        for (Object table_name : j_reader.keySet()) {
            sql = "CREATE TABLE ";
            first = true;
            // Read columns from json by using table name
            sql += (String) table_name + "(";
            Map columns = (Map) j_reader.get(table_name);
            // Generate iterator which contains k,v pairs for sets of table
            // fields.
            Iterator<Map.Entry> cols = columns.entrySet().iterator();
            while (cols.hasNext()) {
                if (first == false) {
                    sql += ',';
                }
                first = false;
                Map.Entry pair = cols.next();
                sql += ("\n " + pair.getKey() + " " + pair.getValue());
            }
            sql += ")";
            try {
                execute_sql(sql);
            } catch (SQLException ex) {
                Logger.getLogger(
                        Database.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Private method for adding SQL to database.
     *
     * @param sql (String) - SQL Query to execute
     * @throws SQLException - Allows caller to handle exception in relevant
     * fashion.
     */
    private void execute_sql(String sql) throws SQLException {
        Logger.getLogger(
                Database.class.getName()).log(
                Level.INFO, sql);
        conn.createStatement().executeUpdate(sql);
    }

    /**
     * Selects from a specified table
     *
     * @param table(String) - Desired SQL table.
     * @param id(String) - ID (Primary key) sought / can be * to return the
     * whole table.
     * @return ResultSet - Results returned from the query.
     */
    protected ResultSet select_from(String table, String id) {
        String sql = "SELECT * FROM " + table + " WHERE id=";
        try {
            Integer.parseInt(id);
            sql += id;
        } catch (NumberFormatException ex) {
            sql += ("\"" + id + "\"");
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.INFO, null, ex);
        }
        if (id.equals("*")) {
            sql = "SELECT * FROM " + table;
        }
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs;
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Deletes from a specified table by an id.
     *
     * @param table(String) - Desired SQL table.
     * @param id(String) - ID (Primary key) sought.
     * @return boolean - True if successful, False if not.
     */
    protected boolean delete_from_table(String table, String id) {
        String sql = "DELETE FROM " + table + " WHERE id=" + id;
        int success = -1;
        try {
            Statement stmt = conn.createStatement();
            success = stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        if (success == 1) {
            return true;
        }
        return false;
    }

    /**
     * Gets the names of a column in a table.
     *
     * @param table_name (String) - Desired SQL table.
     * @return ArrayList<String> - List of columns in a table.
     */
    protected ArrayList<String> get_table_columns(String table_name) {
        ArrayList<String> t_cols = new ArrayList<>();
        try {
            ResultSet columns = conn.getMetaData().getColumns(
                    null, null, table_name, null);
            while (columns.next()) {
                t_cols.add(columns.getString("COLUMN_NAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return t_cols;
    }

    /**
     * Get the names of all the tables known by the DB.
     *
     * @return ArrayList<String> - List of tables in DB.
     */
    protected ArrayList<String> get_table_names() {
        ArrayList<String> t_names = new ArrayList<>();
        String[] types = {"TABLE"};
        try {
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "%", types);
            while (tables.next()) {
                t_names.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return t_names;
    }

    /**
     * Add a user
     *
     * @param user (user)
     * @return boolean - True if successful, False if not
     */
    protected boolean insert_user(User user) {
        String sql = "Insert INTO users(id, password, status) "
                + "VALUES(?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getStatus());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Add a member
     *
     * @param member (Member)
     * @return boolean - True if successful, False if not
     */
    protected boolean insert_member(Member member) {
        String sql = "Insert INTO members(id ,name, address, dob, dor, status, balance) "
                + "VALUES(?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, member.getId());
            ps.setString(2, member.getName());
            ps.setString(3, member.getAddress());
            ps.setDate(4, member.get_dob_sql());
            ps.setDate(5, member.get_dor_sql());
            ps.setString(6, member.getStatus());
            ps.setFloat(7, member.getBalance());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Add a payment
     *
     * @param payment (Payment)
     * @return boolean - True if successful, False if not
     */
    protected boolean insert_payment(Payment payment) {
        String sql = "Insert INTO payments(mem_id, type, amount, date) "
                + "VALUES(?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, payment.getMem_id());
            ps.setString(2, payment.getType());
            ps.setFloat(3, payment.getAmount());
            ps.setDate(4, payment.get_sql_date());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Add a claim
     *
     * @param claim (Claim).
     * @return boolean - True if successful, False if not
     */
    protected boolean insert_claim(Claim claim) {
        String sql = "Insert INTO claims(mem_id, date, description, status, amount) "
                + "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, claim.getMem_id());
            ps.setDate(2, claim.get_sql_date());
            ps.setString(3, claim.getDescription());
            ps.setString(4, claim.getStatus());
            ps.setFloat(5, claim.getAmount());
            if (ps.executeUpdate() == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
        }
        return false;
    }
}
