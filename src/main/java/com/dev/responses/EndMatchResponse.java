package com.dev.responses;

import com.dev.objects.Match;

public class EndMatchResponse extends BaseResponse{

    private Match endMatch;
    public EndMatchResponse(boolean success, Integer errorCode , Match endMatch) {
        super(success, errorCode);
        this.endMatch = endMatch;
    }



}
