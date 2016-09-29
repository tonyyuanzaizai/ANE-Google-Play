//////////////////////////////////////////////////////////////////////////////////////
//
//  Copyright 2013 Freshplanet (http://freshplanet.com | opensource@freshplanet.com)
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//
//////////////////////////////////////////////////////////////////////////////////////

package com.freshplanet.googleplaygames;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.freshplanet.googleplaygames.functions.GetActivePlayerIdFunction;
import com.freshplanet.googleplaygames.functions.GetActivePlayerNameFunction;
import com.freshplanet.googleplaygames.functions.IncrementAchievementFunction;
import com.freshplanet.googleplaygames.functions.ShowAchievementsFunction;
import com.freshplanet.googleplaygames.functions.ShowLeaderboardFunction;
import com.freshplanet.googleplaygames.functions.SignInFunction;
import com.freshplanet.googleplaygames.functions.SignOutFunction;
import com.freshplanet.googleplaygames.functions.SubmitScoreFunction;
import com.freshplanet.googleplaygames.functions.UnlockAchievementFunction;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;

public class ExtensionContext extends FREContext
{

    final int RC_UNUSED = 5001;
	// Public API

	@Override
	public void dispose() { }

	@Override
	public Map<String, FREFunction> getFunctions()
	{
		Map<String, FREFunction> functionMap = new HashMap<String, FREFunction>();
		functionMap.put("signIn", new SignInFunction());
		functionMap.put("signOut", new SignOutFunction());
		functionMap.put("showLeaderboard", new ShowLeaderboardFunction());
		functionMap.put("submitScore", new SubmitScoreFunction());

		functionMap.put("showAchievements", new ShowAchievementsFunction());
		functionMap.put("unlockAchievement", new UnlockAchievementFunction());
		functionMap.put("incrementAchievement", new IncrementAchievementFunction());

		functionMap.put("getActivePlayerName", new GetActivePlayerNameFunction());
		functionMap.put("getActivePlayerId", new GetActivePlayerIdFunction());
		return functionMap;
	}

	public void dispatchEvent(String eventName)
	{
		dispatchEvent(eventName, "OK");
	}

	public void logEvent(String eventName)
	{
		Log.i("GPServicesActivity", eventName);
	}


	public void dispatchEvent(String eventName, String eventData)
	{
		if (eventData == null)
		{
			eventData = "OK";
		}
		logEvent("dispatchEvent:" + eventName +"|"+ eventData +"|"+ playerName +"|"+ playerId);
		dispatchStatusEventAsync(eventName, eventData);
	}

	public String playerName;
	public String playerId;

////////callback function///////
    public void onLeaderboardLoaded( LeaderboardScoreBuffer scores ) {
        dispatchEvent( "ON_LEADERBOARD_LOADED", scoresToJsonString(scores) );
    }
    private String scoresToJsonString( LeaderboardScoreBuffer scores ) {

        int scoresNb = scores.getCount();
        JSONArray jsonScores = new JSONArray();
        for ( int i = 0; i < scoresNb; ++i ) {
            LeaderboardScore score = scores.get(i);
            JSONObject jsonScore = new JSONObject();
            try {
                jsonScore.put("value", score.getRawScore());
                jsonScore.put("rank", score.getRank());

                Player player = score.getScoreHolder();
                JSONObject jsonPlayer = new JSONObject();
                jsonPlayer.put("id", player.getPlayerId());
                jsonPlayer.put("displayName", player.getDisplayName());
                jsonPlayer.put("picture", player.getIconImageUri());

                jsonScore.put("player", jsonPlayer);

                jsonScores.put( jsonScore );

            } catch( JSONException e ) {}
        }
        return jsonScores.toString();
    }

	public void onSignInFailed() {
		logEvent("onSignInFailed");
		dispatchEvent("ON_SIGN_IN_FAIL");
	}

	public void onSignInSucceeded() {
		logEvent("onSignInSucceeded");
		dispatchEvent("ON_SIGN_IN_SUCCESS");
	}
	public void onSignInFailedReason(int reason)
    {
		logEvent("onSignInFailedReason:" + reason);
		onSignInFailed();
    }
	///////////////////public api///////
    public boolean isSignedIn()
    {
    	init();
    	
        SharedPreferences preferences = getActivity().getPreferences(0);
        return preferences.getBoolean("AneGoogleServicesIsSignedIn", false);
    }

    public void setIsSignedIn(boolean value)
    {
    	init();
    	
        SharedPreferences preferences = getActivity().getPreferences(0);
        android.content.SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("AneGoogleServicesIsSignedIn", value);
        editor.commit();
    }
    
    public void signIn()
    {
    	init();
    	
        System.out.println("ANE signIn:");

        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("method", "signin");
        getActivity().startActivity(intent);
    }
    public void signOut()
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("method", "signout");
        getActivity().startActivity(intent);
    }

    public void submitScore(String leaderboardId, long score)
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("method", "submitScore");
        intent.putExtra("leaderboardId", leaderboardId);
        intent.putExtra("score", score);
        getActivity().startActivity(intent);
    }

    public void showLeaderboard(String leaderboardId)
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.putExtra("method", "showLeaderboard");
        intent.putExtra("leaderboardId", leaderboardId);
        getActivity().startActivity(intent);
    }

    public void unlockAchievement(String id)
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), com.freshplanet.googleplaygames.MainActivity.class);
        intent.putExtra("method", "unlockAchievement");
        intent.putExtra("achievementId", id);
        getActivity().startActivity(intent);
    }

    public void incrementAchievement(String id, int incrementSteps)
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), com.freshplanet.googleplaygames.MainActivity.class);
        intent.putExtra("method", "incrementAchievement");
        intent.putExtra("achievementId", id);
        intent.putExtra("incrementSteps", incrementSteps);
        getActivity().startActivity(intent);
    }

    public void showAchievements()
    {
    	init();
    	
        Intent intent = new Intent(getActivity().getApplicationContext(), com.freshplanet.googleplaygames.MainActivity.class);
        intent.putExtra("method", "showAchievements");
        getActivity().startActivity(intent);
    }
    
    
    ////////
    private void init()
    {
        System.out.println("Extension.init-mGoogleApiClient");
    }
}
