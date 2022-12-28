
package com.dev.utils;

import com.dev.objects.Match;
import com.dev.objects.Team;
import com.dev.objects.User;
import com.dev.objects.UserToMatchMap;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.*;

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist(SessionFactory sf) {
        this.sessionFactory = sf;
    }


    @PostConstruct
    public void createConnectionToDatabase() {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
            createTeams();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<User> getAllUsersHibernate() {
        return sessionFactory.openSession().createQuery
                ("FROM User ").list();
    }

    public void saveUser(User user) {
        sessionFactory.openSession().
                save(user);
    }


    public User addUserHibernate(String username, String token) {
        User user = new User(username, token);
        sessionFactory.openSession().save(user);
        return user;
    }

    public boolean userHasPermissions(int userId, String token) {
        boolean hasPermissions = false;
        User user = (User) sessionFactory.openSession().
                createQuery("FROM User WHERE id=:userId").
                setParameter("userId", userId).getSingleResult();
        if (user.getToken().equals(token)) hasPermissions = true;
        return hasPermissions;

    }


    public void createTeams() {
        List teamList = sessionFactory.openSession()
                .createQuery("FROM Team ").list();
        if (teamList.size() == 0) {
            List<String> teamNames = Arrays.asList(
                    "Manchester-City", "Arsenal", "Maccabi-Haifa", "Hapoel-Ashkelon",
                    "Tottenham", "Bayern-Munich", "Inter", "Real-Madrid", "Barcelona",
                    "PSV", "Juventus", "Milan-AC");

            for (String teamName : teamNames) {
                sessionFactory.openSession().save(new Team(teamName));
            }
        } else {
            System.out.println("Teams has already been initialize");
        }

    }

    public Match addNewMatch(int team1Id, int team2Id, int userId) {
        List<Integer> teamsId = new ArrayList<>();
        teamsId.add(team1Id);
        teamsId.add(team2Id);
        List teamList = sessionFactory.openSession().
                createQuery("FROM Team WHERE id IN :teamsId").
                setParameterList("teamsId", teamsId).list();
        User currentUser = (User) sessionFactory.openSession()
                .createQuery("FROM User WHERE id=:userId ").
                setParameter("userId", userId).getSingleResult();
        Match match = new Match((Team) teamList.get(0), (Team) teamList.get(1),currentUser.getId());
        sessionFactory.openSession().save(match);
        updateUserMatchesMap(match.getUserId(),match.getId());
        return match;
    }

    public void updateUserMatchesMap(int userId , int matchId){
        UserToMatchMap userToMatchMap = new UserToMatchMap(userId, matchId);
        sessionFactory.openSession().save(userToMatchMap);
    }

    public Match endMatch(int matchId) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Match match = session.get(Match.class, matchId);
        match.setLive(false);
        session.update(match);
        transaction.commit();
        session.close();
        return match;
    }

    public boolean isTeamsOnOtherLiveMatch(int team1Id , int team2Id){
        List<Integer> teamsId = new ArrayList<>();
        teamsId.add(team1Id);
        teamsId.add(team2Id);
        List list = sessionFactory.openSession().createQuery("FROM Match WHERE team1.id IN(:teamsId) AND live=TRUE")
                .setParameterList("teamsId", teamsId).list();
        List list2 = sessionFactory.openSession().createQuery("FROM Match WHERE team2.id IN(:teamsId) AND live=TRUE")
                .setParameterList("teamsId", teamsId).list();
        System.out.println(list2.size()+ list.size());
        return list.size() + list2.size() > 0;
    }

    public boolean isMatchBelongToUser(int userId, int matchId) {
        List userToMatchList = sessionFactory.openSession().createQuery
                        ("FROM UserToMatchMap WHERE userId=:userId AND matchId = :matchId")
                .setParameter("userId", userId).setParameter("matchId", matchId).list();
        return userToMatchList.size() != 0;
    }

    public boolean isMatchOnLive(int matchId) {
        Match match = (Match) sessionFactory.openSession().createQuery
                        ("FROM Match WHERE id=:matchId")
                .setParameter("matchId", matchId).getSingleResult();
        return match.isLive();
    }

    public Match updateMatchScore(int matchId,int team1Goals , int team2Goals){
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Match match = session.get(Match.class, matchId);
        match.setTeam1_goals(team1Goals);
        match.setTeam2_goals(team2Goals);
        session.update(match);
        transaction.commit();
        session.close();
        return match;
    }


    public List<Match> getLiveMatches(int userId) {
        return sessionFactory.openSession().createQuery
                        ("FROM Match WHERE live=TRUE AND userId = :userId")
                .setParameter("userId", userId).list();
    }

    public List<Match> getAllMatches() {
        return sessionFactory.openSession().createQuery
                ("FROM Match ").list();
    }

    public List<Team> getAllTeams() {
        return sessionFactory.openSession().createQuery
                ("FROM Team").list();
    }

    public List<Match> getEndedMatches() {
        return sessionFactory.openSession().createQuery
                ("FROM Match WHERE live=FALSE ").list();
    }

    public boolean usernameAvailable(String username) {
        List users = sessionFactory.openSession().createQuery
                        ("FROM User WHERE username= :username ").
                setParameter("username", username).list();
        return users.size() == 0;
    }


    public User getUserByToken(String token) {
        User user = null;
        try {
            PreparedStatement preparedStatement = this.connection
                    .prepareStatement(
                            "SELECT id, username FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                user = new User(username, token);
                user.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }


    public boolean isUserApproveToSignIn(String username, String token) {
        List users = sessionFactory.openSession()
                .createQuery("FROM User WHERE username=:username AND token=:token")
                .setParameter("username", username).setParameter("token", token)
                .list();
        return users.size() != 0;

    }

}
