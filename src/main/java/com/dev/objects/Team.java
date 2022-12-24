package com.dev.objects;


import javax.persistence.*;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public int id;
    @Column
    private String name;


    public Team(String name) {
        this.name = name;
    }
    public Team(int id , String name) {
        this.id = id;
        this.name = name;
    }
    public Team() {

    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
