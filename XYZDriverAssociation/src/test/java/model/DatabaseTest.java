/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import db.Database;

import java.util.Date;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import java.sql.*;

import org.junit.Assert;
import org.junit.Test;


/**
* Generic DB tests. Ensuring basic functionality
*/
public class DatabaseTest {
    public DatabaseTest(){}
    /**
    * Test which initialises the DB and ensures a non null instance is returned.
    */
    @Test
    public void init_DB(){
        Database db = Database.get_DB();
        db.clear_DB();
        Assert.assertNotNull(db);
    }
    
    /**
    * Test which ensures instantiating the db several times returns the same
    * instance.
    */
    @Test
    public void db_singleton(){
        Database db1 = Database.get_DB();
        db1.clear_DB();

        Database db2 = Database.get_DB();
        db2.clear_DB();

        Assert.assertSame(db1.get_DB(), db2.get_DB());
    }
    
    /**
    * Test which populates the claim table. Verifies information is present
    * in DB after.
    * @throws SQLException
    */
    @Test
    public void add_claim() throws SQLException{
        String sql = "INSERT INTO claims(mem_id, date, description, status, amount) "
                    + "VALUES(?,?,?,?,?)";
        final String[] mem_id = {"me-aydin", "me-aydin", "e-simons"};
        final String[] desc = {"change mirror", "repair scratch", "polishing tyers"};
        Database db = Database.get_DB();
        db.clear_DB();
        Connection connection = db.getConn();
        
        // Populate DB
        for(int i = 0; i< mem_id.length; i++){
         PreparedStatement statement = connection.prepareStatement(sql);
         statement.setString(1, mem_id[i]);
         statement.setDate(2, new java.sql.Date(new Date().getTime()));
         statement.setString(3, desc[i]);
         statement.setString(4, "APPROVED");
         statement.setFloat(5, ThreadLocalRandom.current().nextFloat() * 100);
         statement.executeUpdate();
        }
        // Verify Population suceeds
        sql = "Select id, mem_id, date, description, status, amount FROM claims";
        ResultSet rs = connection.createStatement().executeQuery(sql);
        while(rs.next()){
            int r_id = rs.getInt("id");
            String r_mem_id = rs.getString("mem_id");
            Date r_date = rs.getDate("date");
            String r_descrip = rs.getString("description");
            String r_status = rs.getString("status");
            float r_amount = rs.getFloat("amount");
            
            // Verify what's in the initial array is in the DB
            Assert.assertTrue(
                Arrays.asList(mem_id).contains(r_mem_id));
            Assert.assertTrue(
                Arrays.asList(desc).contains(r_descrip));
        }

    }
}
