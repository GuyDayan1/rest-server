package com.dev.controllers;

import com.dev.objects.Match;
import com.dev.objects.Team;
import com.dev.objects.TeamStats;
import com.dev.objects.User;
import com.dev.responses.*;
import com.dev.utils.ErrorCodes;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;


@RestController
public class MainController {


    @Autowired
    public Utils utils;

    @Autowired
    private Persist persist;

    @PostConstruct
    public void init () {

    }




//    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})
//    public List<UserObject> getAllUsers () {
//        return persist.getAllUsersHibernate();
//    }


    @RequestMapping(value = "/update-match", method = RequestMethod.GET)
    public BaseResponse updateMatchScore(int userId , int matchId , int team1Goals , int team2Goals , String token){
        final int MATCH_ALREADY_ENDED_ERROR=77;
        BaseResponse response = null;
        if (userHasPermissions(userId,token)){
            if (persist.isMatchBelongToUser(userId,matchId)){
                if (persist.isMatchOnLive(matchId)){
                    Match updatedMatch = persist.updateMatchScore(matchId,team1Goals,team2Goals);
                    response = new UpdateMatchResponse(true,null,updatedMatch);
                }else {
                    response = new BaseResponse(false,MATCH_ALREADY_ENDED_ERROR);
                }
            }else {
                response = new BaseResponse(false,ErrorCodes.PERMISSION_ERROR_CODE);
            }
        }else {
            response = new BaseResponse(false,ErrorCodes.PERMISSION_ERROR_CODE);
        }
        return response;
    }


    @RequestMapping(value = "/get-teams", method = RequestMethod.GET)
    public BaseResponse getTeams(int userId , String token){
        BaseResponse response;
        if (userHasPermissions(userId,token)){
            List<Team> teams = persist.getAllTeams();
            response = new GetTeamsResponse(true,null,teams);
        }else {
            response = new BaseResponse(false,ErrorCodes.PERMISSION_ERROR_CODE);
        }
        return response;
    }

    @RequestMapping(value = "/add-new-live-match", method = RequestMethod.POST)
    public BaseResponse addNewLiveMatch(int team1Id , int team2Id , int userId , String token){
        BaseResponse response;
        if (userHasPermissions(userId,token)){
            Match newMatch = persist.addNewMatch(team1Id , team2Id , userId);
            response = new NewLiveMatchResponse(true,null , newMatch);
        }else {
            response = new BaseResponse(false,ErrorCodes.PERMISSION_ERROR_CODE);
        }
        return response;
    }

/// /end-match(int userId , String token)
    @RequestMapping(value = "/end-match", method = RequestMethod.POST)
    public BaseResponse endMatch(int matchId,int userId , String token){
        BaseResponse response = null;
        if (userHasPermissions(userId,token)){
            Match currentEndMatch =  persist.endMatch(matchId);
            if (!currentEndMatch.isLive()){
                response = new EndMatchResponse(true,null);
            }else {
                response = new EndMatchResponse(false , 100);
            }
        }
        return response;
    }


    // /get-live-matches(int userId , String token)
    @RequestMapping(value = "/get-live-matches", method = RequestMethod.GET)
    public BaseResponse getLivesMatches(int userId , String token){
        BaseResponse response;
        List<Match> userMatches;
        if (userHasPermissions(userId,token)){
            userMatches= persist.getLiveMatches(userId);
            response = new GetLiveMatchesResponse(true,null,userMatches);
        }else {
            response = new BaseResponse(false,ErrorCodes.PERMISSION_ERROR_CODE);
        }
        return response;
    }

  // c
    @RequestMapping(value = "/get-table", method = RequestMethod.GET)
    public List<TeamStats> getTable(int userId , String token){
       List<Match> endedMatches = persist.getEndedMatches();
       List<Team> teams = persist.getAllTeams();
       List<TeamStats> teamStats = utils.calculateTable(teams,endedMatches);
       return teamStats;
    }
/// /get-live-table(int userId , String token)
    @RequestMapping(value = "/get-live-table", method = RequestMethod.GET)
    public BaseResponse getLiveTable(int userId , String token){
        List<TeamStats> teamStats;
        BaseResponse baseResponse;
        if (userHasPermissions(userId,token)){
            List<Match> allMatches = persist.getAllMatches();
            List<Team> teams = persist.getAllTeams();
            teamStats = utils.calculateTable(teams,allMatches);
            baseResponse = new GetLiveTableResponse(true,null,teamStats);
        }else {
            baseResponse = new BaseResponse(false, ErrorCodes.PERMISSION_ERROR_CODE);
        }
        return baseResponse;
    }



    @RequestMapping(value = "/sign-in", method = {RequestMethod.POST})
    public BaseResponse signIn(String username, String password) {
        final int WRONG_CREDENTIALS=1;
        BaseResponse baseResponse;
        String hash = createHash(username, password);
        boolean isApproveToSignIn = persist.isUserApproveToSignIn(username, hash);
        if (isApproveToSignIn) {
            User user = persist.getUserByToken(hash);
            baseResponse = new SignInResponse(true, null, user);
        } else {
            baseResponse = new BaseResponse(false, WRONG_CREDENTIALS);
        }
        return baseResponse;
    }

    @RequestMapping(value = "/create-user", method = {RequestMethod.GET, RequestMethod.POST})
    public BaseResponse createUser(String username, String password) {
        final int USER_CONTAINS_CHAR_ERROR_CODE = 1;
        final int PASSWORD_LENGTH_ERROR_CODE = 2;
        final int USERNAME_EXIST_ERROR_CODE = 3;
        BaseResponse createUserResponse;
        User newUser;
        if (utils.validateUsername(username)) {
            if (utils.validatePassword(password)) {
                if (persist.usernameAvailable(username)) {
                    String token = createHash(username, password);
                    newUser = persist.addUserHibernate(username, token);
                    createUserResponse = new CreateUserResponse(true,null, newUser);
                } else {
                    createUserResponse = new BaseResponse(false,USERNAME_EXIST_ERROR_CODE);
                }
            } else {
                createUserResponse = new BaseResponse(false , PASSWORD_LENGTH_ERROR_CODE);
            }
        } else {
            createUserResponse = new BaseResponse(false ,USER_CONTAINS_CHAR_ERROR_CODE);
        }
        return createUserResponse;
    }



    public String createHash (String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash ;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    public boolean userHasPermissions(int userId, String token){
        return persist.userHasPermissions(userId,token);
    }






}
