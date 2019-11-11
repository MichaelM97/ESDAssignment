package model;

/**
* Model for DB User
*/
public class User {

    private String id;
    private String password;
    private String status;

    public User(String id, String password, String status) {
        this.id = id;
        this.password = password;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

}
