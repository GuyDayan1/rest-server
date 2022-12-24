package com.dev.responses;

import com.dev.objects.Match;

public class UpdateMatchResponse extends BaseResponse{

    private Match match;
    public UpdateMatchResponse(boolean success, Integer errorCode,Match match) {
        super(success, errorCode);
        this.match=match;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
