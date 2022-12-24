package com.dev.responses;

import com.dev.objects.Match;

import java.util.List;

public class GetLiveMatchesResponse extends BaseResponse{

    List<Match> matches;


    public GetLiveMatchesResponse(boolean success, Integer errorCode,List<Match> matches) {
        super(success, errorCode);
        this.matches = matches;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}
