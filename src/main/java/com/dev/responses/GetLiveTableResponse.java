package com.dev.responses;

import com.dev.objects.TeamStats;

import java.util.ArrayList;
import java.util.List;

public class GetLiveTableResponse extends BaseResponse{

    List<TeamStats> teamStats;

    public GetLiveTableResponse(boolean success, Integer errorCode ,  List<TeamStats> teamStats) {
        super(success, errorCode);
        this.teamStats = teamStats;
    }

    public List<TeamStats> getTeamStats() {
        return teamStats;
    }

    public void setTeamStats(List<TeamStats> teamStats) {
        this.teamStats = teamStats;
    }
}
