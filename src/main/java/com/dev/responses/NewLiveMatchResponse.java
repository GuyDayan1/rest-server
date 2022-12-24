package com.dev.responses;


import com.dev.objects.Match;

public class NewLiveMatchResponse extends BaseResponse{


    private Match newMatch;

    public NewLiveMatchResponse(boolean success, Integer errorCode,Match newMatch) {
        super(success, errorCode);
        this.newMatch = newMatch;
    }

    public Match getNewMatch() {
        return newMatch;
    }

    public void setNewMatch(Match newMatch) {
        this.newMatch = newMatch;
    }
}
