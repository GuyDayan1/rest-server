package com.dev.objects;

import javax.persistence.*;

@Entity
@Table(name = "users_matches_map")
public class UserToMatchMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    private Integer userId;
    @Column
    private Integer matchId;

    public UserToMatchMap(Integer userId, Integer matchId) {
        this.userId = userId;
        this.matchId = matchId;
    }

    public UserToMatchMap() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }
}
