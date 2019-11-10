/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import db.DatabaseFactory;

import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
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
    * Test which instantiates DBF
    */
    
    @Test
    public void init_dbf(){
        DatabaseFactory dbf = new DatabaseFactory();
        Assert.assertNotNull(dbf);
    }
    
    /**
    * Test which populates the claim table.
    * Verifies information is present in DB after.
    * @throws SQLException
    */
    @Test
    public void add_claim() throws SQLException{
        final String[] mem_id = {"me-aydin", "me-aydin", "e-simons"};
        final String[] desc = {"change mirror", "repair scratch", "polishing tyers"};
        
        DatabaseFactory dbf = new DatabaseFactory();
        dbf.init();
        for(int i = 0; i < mem_id.length; i++){
            ArrayList<Object> foo = new ArrayList();
            foo.add(mem_id[i]);
            foo.add(new Date().getTime());
            foo.add(desc[i]);
            foo.add("APPROVED");
            foo.add(ThreadLocalRandom.current().nextFloat() * 10000);
            dbf.insert("claims", foo);
        }
        // Verify Population suceeds
        for(int i = 0; i < mem_id.length; i++){
            ResultSet rs = dbf.get_from_table("claims", "*");
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
}
