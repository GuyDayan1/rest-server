package com.dev.responses;

import com.dev.objects.TeamStats;

import java.util.List;

public class GetTableResponse extends BaseResponse{

    List<TeamStats> teamStats;


    public GetTableResponse(boolean success, Integer errorCode , List<TeamStats> teamStats) {
        super(success, errorCode);
        this.teamStats = teamStats;
    }

    public List<TeamStats> getTeamStats() {
        return teamStats;
    }
}
