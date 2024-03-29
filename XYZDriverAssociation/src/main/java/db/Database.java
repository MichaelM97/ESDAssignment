package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

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
    private final String[] SQL_TABLES = {
        "CREATE TABLE users(\n"
        + "id VARCHAR(50) NOT NULL PRIMARY KEY,\n"
        + "password VARCHAR(50) NOT NULL,\n"
        + "name VARCHAR(50),\n"
        + "address VARCHAR(100),\n"
        + "dob DATE DEFAULT NULL,\n"
        + "dor DATE DEFAULT NULL,\n"
        + "balance DECIMAL(8,2) NOT NULL,\n"
        + "status VARCHAR(10) NOT NULL )",
        "CREATE TABLE payments(\n"
        + "id INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
        + "mem_id VARCHAR(50) NOT NULL,\n"
        + "type VARCHAR(25) NOT NULL,\n"
        + "amount DECIMAL(8,2) NOT NULL,\n"
        + "date DATE NOT NULL )",
        "CREATE TABLE claims(\n"
        + "id INTEGER NOT NULL primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),\n"
        + "mem_id VARCHAR(50) NOT NULL,\n"
        + "date DATE NOT NULL,\n"
        + "description VARCHAR(50) NOT NULL,\n"
        + "status VARCHAR(25) NOT NULL,\n"
        + "amount DECIMAL(8,2) NOT NULL )"
    };

    private Database() {
        get_conn();
        if (get_table_names() == null || get_table_names().size() != SQL_TABLES.length) {
            initialise(false);
        }

        // Create admin user if one not found.
        if (select_from("users", "admin") == null) {
            try {
                insert_user(
                        new User(
                                "admin",
                                new utils.HashHelper().hashString("password"),
                                "ADMINISTRATOR",
                                "ADMINISTRATOR",
                                new java.text.SimpleDateFormat(
                                        "yyyy-MM-dd").parse("1900-01-01"),
                                new java.text.SimpleDateFormat(
                                        "yyyy-MM-dd").parse("1900-01-02"),
                                0,
                                User.ADMIN)
                );
            } catch (java.text.ParseException ex) {
                Logger.getLogger(
                        Database.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }
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
    public static Connection get_conn() {
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
     * Initialises the Database with relevant tables.
     *
     * @param clear (boolean) - Resets the DB to original state.
     */
    protected void initialise(boolean clear) {
        if (clear) {
            clear_db();
        }
        for (String table : SQL_TABLES) {
            try {
                execute_sql(table);
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
        if (id.equals("*")) {
            try {
                String sql = "SELECT * FROM " + table;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs;
                }
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // Check if the ID is an int
            int integerID = -1;
            try {
                integerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                // ignore
            }
            // Build the SQL statement
            try {
                String sql = "SELECT * FROM " + table + " WHERE id = ?";
                PreparedStatement ps = conn.prepareStatement(sql);
                if (integerID == -1) {
                    ps.setString(1, id);
                } else {
                    ps.setInt(1, integerID);
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs;
                }
            } catch (SQLException ex) {
                Logger.getLogger(
                        Database.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
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
        final String sql = "DELETE FROM " + table + " WHERE id=" + "'" + id + "'";
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
        } catch (NullPointerException ex) {
            Logger.getLogger(
                    Database.class.getName()).log(
                    Level.SEVERE, null, ex);
            return null;
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
        String sql = "Insert INTO users(id, password, name, address, dob, dor, balance, status) "
                + "VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getId());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());
            ps.setString(4, user.getAddress());
            ps.setDate(5, user.getDobSql());
            ps.setDate(6, user.getDorSql());
            ps.setFloat(7, user.getBalance());
            ps.setString(8, user.getStatus());
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

    /**
     * Updates a user
     *
     * @param user (Payment)
     * @return boolean - True if successful, False if not
     */
    protected boolean update_user(User user) {
        String sql = "UPDATE users SET password = ?, name = ?, address = ?, dob = ?, dor = ?, balance = ?, status = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getPassword());
            ps.setString(2, user.getName());
            ps.setString(3, user.getAddress());
            ps.setDate(4, user.getDobSql());
            ps.setDate(5, user.getDorSql());
            ps.setFloat(6, user.getBalance());
            ps.setString(7, user.getStatus());
            ps.setString(8, user.getId());
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
     * Updates a payment
     *
     * @param payment (Payment)
     * @return boolean - True if successful, False if not
     */
    protected boolean update_payment(Payment payment) {
        String sql = "UPDATE payments SET mem_id = ?, type = ?, amount = ?, date = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, payment.getMem_id());
            ps.setString(2, payment.getType());
            ps.setFloat(3, payment.getAmount());
            ps.setDate(4, payment.get_sql_date());
            ps.setInt(5, payment.getId());
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
     * Updates a claim
     *
     * @param claim (Claim)
     * @return boolean - True if successful, False if not
     */
    protected boolean update_claim(Claim claim) {
        String sql = "UPDATE claims SET mem_id = ?, date = ?, description = ?, status = ?, amount = ? "
                + "WHERE id = ?";
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, claim.getMem_id());
            ps.setDate(2, claim.get_sql_date());
            ps.setString(3, claim.getDescription());
            ps.setString(4, claim.getStatus());
            ps.setFloat(5, claim.getAmount());
            ps.setInt(6, claim.getId());
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
     * Searches for a user 
     *
     * @param entry (String) the search string
     * @return ResultSet - The search results, null if no results or error
     */
    protected ResultSet search(String entry) {
        try {
            String sql = "SELECT * FROM users WHERE id LIKE ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + entry + "%");
            
            ResultSet rs = ps.executeQuery();
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
}
