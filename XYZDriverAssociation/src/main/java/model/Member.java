package model;

import java.util.Date;

/**
* Model for DB Member.
*/
public class Member {
    
    public static final String STATUS_PENDING = "PENDING";

    private String id;
    private String name;
    private String address;
    private Date dob;
    private Date dor;
    private String status;
    private float balance;

    public Member(String id, String name,
            String address, Date dob,
            Date dor, String status,
            float balance) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.dob = dob;
        this.dor = dor;
        this.status = status;
        this.balance = balance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public java.sql.Date get_dob_sql(){
        if (dob != null){
            return new java.sql.Date((long) getDob().getTime());
        } 
        return null;
    }

    public java.sql.Date get_dor_sql(){
        if (dob != null){
            return new java.sql.Date((long) getDor().getTime());
        }
        return null;
    }

}
