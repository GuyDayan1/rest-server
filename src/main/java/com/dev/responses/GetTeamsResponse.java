package com.dev.responses;

import com.dev.objects.Team;

import java.util.List;

public class GetTeamsResponse extends BaseResponse{

    private List<Team> teamList;


    public GetTeamsResponse(boolean success, Integer errorCode,List<Team>teamList) {
        super(success, errorCode);
        this.teamList = teamList;
    }

    public List<Team> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<Team> teamList) {
        this.teamList = teamList;
    }
}
