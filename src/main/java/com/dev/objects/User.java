package com.dev.objects;

import javax.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column
    private String username;
    @Column
    private String token;
    @Column
    private Date creationDate;



    public User(String username, String token) {
        this.username = username;
        this.token = token;
        this.creationDate = new Date();
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
