// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page:
// Decompiler options: packimports(3)
// Source File Name:   MainActivity.java

package com.freshplanet.googleplaygames;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

// Referenced classes of package com.sgn.googleservices:
//            GPServicesActivity, GoogleServices

public class MainActivity extends GPServicesActivity
{

    public MainActivity()
    {
        System.out.println("MainActivity Constructor:");
    }

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        init();
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");

        System.out.println("MainActivity.onCreate:" + method);

        if("signin".equalsIgnoreCase(method) || Extension.context.isSignedIn())
        {
            connect();
        } else
        {
            finishActivity();
        }
    }

    public void showLeaderboard(String leaderboardId)
    {
        if(!Extension.context.isSignedIn())
        {
            return;
        } else
        {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(getApiClient(), leaderboardId), REQUEST_LEADERBOARD);
            return;
        }
    }

    public void submitScore(String leaderboardId, long score)
    {
        //Extension.debug((new StringBuilder()).append("submit at leaderboardID = ").append(leaderboardId).append(" - score = ").append(score).toString(), new Object[0]);
        Games.Leaderboards.submitScoreImmediate(getApiClient(), leaderboardId, score);
        return;
    }

    protected void onSignInFailed(int reason)
    {
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");
        sendLog("onSignInFailed:" + method);
        
        if("signin".equalsIgnoreCase(method))
        {
            Extension.context.onSignInFailedReason(reason);
            finishActivity();
        } else
        {
            callMethod();
        }
    }

    protected void onSignInSucceeded()
    {
    	Extension.context.setIsSignedIn(true);
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");

        Player player = Games.Players.getCurrentPlayer(mGoogleApiClient);
        
        System.out.println("onSignInSucceeded: " + method +"|"+ player);
        sendLog("onSignInSucceeded: " + method +"|"+ player);
        
        if(player != null){
    		Extension.context.playerId = player.getPlayerId();
        	Extension.context.playerName = player.getDisplayName();
        	System.out.println("onSignInSucceeded: " + player.getPlayerId() + " " + player.toString());
    	}
    	
        if("signin".equalsIgnoreCase(method))
        {
        	Extension.context.onSignInSucceeded();
            finishActivity();
        } else
        {
            callMethod();
        }
    }

    private void callMethod()
    {
        Intent intent = getIntent();
        String method = intent.getStringExtra("method");
        
        System.out.println("callMethod: " + method);
        
        if("signout".equalsIgnoreCase(method))
        {
            signOut();
            finishActivity();
        } else
        if("showLeaderboard".equalsIgnoreCase(method))
        {
            showLeaderboard(intent.getStringExtra("leaderboardId"));
            finishActivity();
        } else
        if("submitScore".equalsIgnoreCase(method))
        {
            submitScore(intent.getStringExtra("leaderboardId"), intent.getLongExtra("score", 0L));
            finishActivity();
        } else
        if("unlockAchievement".equalsIgnoreCase(method))
        {
            unlockAchievement(intent.getStringExtra("achievementId"));
            finishActivity();
        } else
        if("incrementAchievement".equalsIgnoreCase(method))
        {
            incrementAchievement(intent.getStringExtra("achievementId"), intent.getIntExtra("incrementSteps", 0));
            finishActivity();
        } else
        if("showAchievements".equalsIgnoreCase(method))
        {
            showAchievements();
            finishActivity();
        }
    }

    public void signOut()
    {
        disconnect();
        Extension.context.setIsSignedIn(false);
    }

    public void unlockAchievement(String id)
    {
        if(!Extension.context.isSignedIn())
        {
            return;
        } else
        {
            //Extension.debug((new StringBuilder()).append("unlock Achievement = ").append(id).toString(), new Object[0]);
            Games.Achievements.unlock(getApiClient(), id);
            return;
        }
    }

    public void incrementAchievement(String id, int incrementSteps)
    {
        if(!Extension.context.isSignedIn() || incrementSteps == 0)
        {
            if(!isSignedIn())
                //Extension.debug("Is not signed in", new Object[0]);
            if(incrementSteps == 0)
                //Extension.debug("incrementSteps is zero", new Object[0]);
            return;
        } else
        {
            //Extension.debug((new StringBuilder()).append("submit at Achievement = ").append(id).append(" - incrementSteps = ").append(incrementSteps).toString(), new Object[0]);
            Games.Achievements.incrementImmediate(getApiClient(), id, incrementSteps);
            return;
        }
    }

    public void showAchievements()
    {
        if(!Extension.context.isSignedIn())
        {
            return;
        } else
        {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
            return;
        }
    }
    
    private void finishActivity(){
    	System.out.println("MainActivity try to finishActivity:");
    	//this.finish();
    }


}
