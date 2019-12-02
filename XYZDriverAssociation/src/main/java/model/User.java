package model;

import java.util.Date;

/**
 * Model for DB User
 */
public class User {

    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_SUSPENDED = "SUSPENDED";
    public static final String ADMIN = "ADMIN";

    private String id;
    private String password;
    private String name;
    private String address;
    private Date dob;
    private Date dor;
    private float balance;
    private String status;

    public User(
            String id,
            String password,
            String name,
            String address,
            Date dob,
            Date dor,
            float balance,
            String status
    ) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.dor = dor;
        this.balance = balance;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public Date getDor() {
        return dor;
    }

    public void setDor(Date dor) {
        this.dor = dor;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.sql.Date getDobSql() {
        if (dob != null) {
            return new java.sql.Date((long) getDob().getTime());
        }
        return null;
    }

    public java.sql.Date getDorSql() {
        if (dob != null) {
            return new java.sql.Date((long) getDor().getTime());
        }
        return null;
    }

}
