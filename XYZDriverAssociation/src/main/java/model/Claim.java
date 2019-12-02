package model;

import java.util.Date;

/*
* Model for DB Claim. 
* Two constructors, one allows an ID not to passed as it's auto-generated.
* within the DB.
*/
public class Claim {
    
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_APPROVED = "APPROVED";
    public static final String STATUS_REJECTED = "REJECTED";

    private int id;
    private String mem_id;
    private Date date;
    private String description;
    private String status;
    private float amount;

    public Claim(int id, String mem_id, Date date,
            String description, String status, float amount) {
        this.id = id;
        this.mem_id = mem_id;
        this.date = date;
        this.description = description;
        this.status = status;
        this.amount = amount;
    }

    public Claim(String mem_id, Date date, String description,
            String status, float amount) {
        this.mem_id = mem_id;
        this.date = date;
        this.description = description;
        this.status = status;
        this.amount = amount;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMem_id() {
        return mem_id;
    }

    public void setMem_id(String mem_id) {
        this.mem_id = mem_id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public java.sql.Date get_sql_date() {
        if (getDate() != null) {
            return new java.sql.Date((long) getDate().getTime());
        } else {
            return null;
        }
    }

}
