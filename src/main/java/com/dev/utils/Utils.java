package com.dev.utils;

import com.dev.objects.Match;
import com.dev.objects.Team;
import com.dev.objects.TeamStats;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Utils {

    public boolean validateUsername(String username) {
        boolean valid = false;
        if (username != null) {
            if (username.contains("@") && username.length() >=5) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validatePassword(String password) {
        boolean valid = false;
        if (password != null) {
            if (password.length() >= 8) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validateNote(String note) {
        boolean valid = false;
        if (note != null && note.length() > 0) {
            valid = true;
        }
        return valid;
    }

    public List<TeamStats> calculateTable(List<Team> teams, List<Match> matches) {
        List<TeamStats> teamStats = new ArrayList<>();
        teamStats = initializeTeams(teams, teamStats);
        for (Match match : matches) {
            Integer winnerTeamId = null;
            Integer loserTeamId = null;
            Integer winnerTeamGoals = null;
            Integer loserTeamGoals = null;
            int team1Id = match.getTeam1().getId();
            int team2Id = match.getTeam2().getId();
            int team1Goals = match.getTeam1_goals();
            int team2Goals = match.getTeam2_goals();
            if (team1Goals == team2Goals) {
                updateTeamStats(teamStats, team1Id, team2Id, team1Goals, team2Goals);
            } else {
                winnerTeamId = team1Goals > team2Goals ? team1Id : team2Id;
                loserTeamId = winnerTeamId == team1Id ? team2Id : team1Id;
                winnerTeamGoals = winnerTeamId == team1Id ? team1Goals : team2Goals;
                loserTeamGoals = winnerTeamGoals == team1Goals ? team2Goals : team1Goals;
                updateTeamStats(teamStats, winnerTeamId, loserTeamId, winnerTeamGoals, loserTeamGoals);
            }


        }
        return teamStats;
    }

    public void updateTeamStats(List<TeamStats> teamStatsList, int winnerTeamId, int loserTeamId, int winnerTeamGoals, int loserTeamGoals) {
        int updatedTeams = 0;
        MatchType matchType;
        int balance = winnerTeamGoals - loserTeamGoals;
        while (updatedTeams < 2) {
            for (TeamStats teamStats : teamStatsList) {
                if (teamStats.getId() == winnerTeamId) {
                    matchType = winnerTeamGoals == loserTeamGoals ? MatchType.DRAW : MatchType.WIN;
                    teamStats.setGoalsFor(teamStats.getGoalsFor() + winnerTeamGoals);
                    teamStats.setGoalsAgainst(teamStats.getGoalsAgainst() + loserTeamGoals);
                    teamStats.setTotalGames(teamStats.getTotalGames() + 1);
                    teamStats.setGoalsBalance(teamStats.getGoalsBalance() + balance);
                    switch (matchType) {
                        case DRAW:
                            teamStats.setNumberOfDraws(teamStats.getNumberOfDraws() + 1);
                            teamStats.setPoints(teamStats.getPoints() + 1);
                            break;
                        case WIN:
                            teamStats.setNumberOfWins(teamStats.getNumberOfWins() + 1);
                            teamStats.setPoints(teamStats.getPoints() + 3);
                            break;
                    }
                    updatedTeams++;
                }
                if (teamStats.getId() == loserTeamId) {
                    matchType = winnerTeamGoals == loserTeamGoals ? MatchType.DRAW : MatchType.LOSE;
                    teamStats.setGoalsFor(teamStats.getGoalsFor() + loserTeamGoals);
                    teamStats.setGoalsAgainst(teamStats.getGoalsAgainst() + winnerTeamGoals);
                    teamStats.setTotalGames(teamStats.getTotalGames() + 1);
                    teamStats.setGoalsBalance(teamStats.getGoalsBalance() + (balance * (-1)));

                    switch (matchType) {
                        case DRAW:
                            teamStats.setNumberOfDraws(teamStats.getNumberOfDraws() + 1);
                            teamStats.setPoints(teamStats.getPoints() + 1);
                            break;
                        case LOSE:
                            teamStats.setNumberOfLoses(teamStats.getNumberOfLoses() + 1);
                            // lose 0 points
                            break;
                    }
                    updatedTeams++;
                }
            }
        }
    }



    public List<TeamStats> initializeTeams(List<Team> teams, List<TeamStats> teamStats) {
        for (Team team : teams) {
            teamStats.add(new TeamStats(team.getId(), team.getName()));
        }
        return teamStats;
    }
}

///*
//
//                if (teamStats.getId() == team1Id){
//                    teamStats.setGoalsFor(teamStats.getGoalsFor() + team1Goals);
//                    teamStats.setGoalsAgainst(teamStats.getGoalsAgainst() + team2Goals);
//                    teamStats.setNumberOfDraws(teamStats.getNumberOfDraws()+1);
//                    teamStats.setPoints(teamStats.getPoints()+1);
//                    teamStats.setTotalGames(teamStats.getTotalGames()+1);
//                    teamStats.setGoalsBalance(teamStats.getGoalsBalance() + balance);
//                    updatedTeams++;
//                }
//                if (teamStats.getId() == team2Id){
//                    teamStats.setGoalsFor(teamStats.getGoalsFor() + team2Goals);
//                    teamStats.setGoalsAgainst(teamStats.getGoalsAgainst() + team1Goals);
//                    teamStats.setNumberOfDraws(teamStats.getNumberOfDraws()+1);
//                    teamStats.setPoints(teamStats.getPoints()+1);
//                    teamStats.setTotalGames(teamStats.getTotalGames()+1);
//                    teamStats.setGoalsBalance(teamStats.getGoalsBalance() + (balance * (-1)));
//                    updatedTeams++;
//                }
//            }
// */
