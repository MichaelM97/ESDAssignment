package model;

import java.util.Date;

/*
* Model for DB Payment.
*Two constructors, one allows an ID not to passed as it's auto-generated.
* within the DB.
*/
public class Payment {

    private int id;
    private String mem_id;
    private String type;
    private float amount;
    private Date date;

    public Payment(int id, String mem_id, String type, float amount,
            Date date) {
        this.id = id;
        this.mem_id = mem_id;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public Payment(String mem_id, String type, float amount,
            Date date) {
        this.mem_id = mem_id;
        this.type = type;
        this.amount = amount;
        this.date = date;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public java.sql.Date get_sql_date() {
        if (getDate() != null) {
            return new java.sql.Date((long) getDate().getTime());
        } else {
            return null;
        }
    }
}
