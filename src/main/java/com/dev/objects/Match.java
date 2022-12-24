package com.dev.objects;
import javax.persistence.*;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "team1_id")
    private Team team1;
    @ManyToOne
    @JoinColumn(name = "team2_id")
    private Team team2;
    @Column
    private Integer team1_goals;
    @Column
    private Integer team2_goals;
    @Column
    private Boolean live;

    @Column(name = "user_id")
    private Integer userId;

    public Match() {

    }
    public Match (Team team1 , Team team2 , Integer userId){
        this.team1 = team1;
        this.team2 = team2;
        this.userId = userId;
        this.team1_goals= 0;
        this.team2_goals = 0;
        this.live = true;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Team getTeam1() {
        return team1;
    }

    public void setTeam1(Team team1) {
        this.team1 = team1;
    }

    public Team getTeam2() {
        return team2;
    }

    public void setTeam2(Team team2) {
        this.team2 = team2;
    }

    public int getTeam1_goals() {
        return team1_goals;
    }

    public void setTeam1_goals(int team1_goals) {
        this.team1_goals = team1_goals;
    }

    public int getTeam2_goals() {
        return team2_goals;
    }


    public void setTeam2_goals(Integer team2_goals) {
        this.team2_goals = team2_goals;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public void setTeam1_goals(Integer team1_goals) {
        this.team1_goals = team1_goals;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getLive() {
        return live;
    }

    public void setLive(Boolean live) {
        this.live = live;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
