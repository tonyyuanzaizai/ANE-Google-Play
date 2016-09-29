// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page:
// Decompiler options: packimports(3)
// Source File Name:   GPServicesActivity.java

package com.freshplanet.googleplaygames;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

// Referenced classes of package com.sgn.googleservices:
//            LogTools

public abstract class GPServicesActivity extends Activity
    implements com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener
{

    public GPServicesActivity()
    {
        mResolvingError = false;
    }

    public void setDebugLog(boolean value)
    {
        debugLog = value;
    }

    protected void sendLog(String msg)
    {
        if(debugLog)
            Log.d("GPServicesActivity", msg);
    }

    protected abstract void onSignInFailed(int i);

    protected abstract void onSignInSucceeded();

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mResolvingError = savedInstanceState != null && savedInstanceState.getBoolean("resolving_error", false);
    }

    public void init()
    {
        System.out.println("MainActivity.init-mGoogleApiClient:");
        mGoogleApiClient = (new com.google.android.gms.common.api.GoogleApiClient.Builder(this.getApplicationContext(), this, this)).
        		addApi(Games.API).addScope(Games.SCOPE_GAMES).
        		build();
    }

    public void connect()
    {
        mGoogleApiClient.connect();
    }

    public void disconnect()
    {
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    public void onConnectionFailed(ConnectionResult result)
    {
        sendLog((new StringBuilder()).append("onConnectionFailed - ").append(result.toString()).toString());
        if(mResolvingError)
            return;
        if(result.hasResolution())
        {
            try
            {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            }
            catch(android.content.IntentSender.SendIntentException e)
            {
                mGoogleApiClient.connect();
            }
        } else
        {
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }
    }

    public void onConnected(Bundle bundle)
    {
        sendLog("onConnected");
        onSignInSucceeded();
    }

    public void onConnectionSuspended(int arg0)
    {
        sendLog((new StringBuilder()).append("onConnectionSuspended - returnValue = ").append(arg0).toString());
        if(mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
    }

    public GoogleApiClient getApiClient()
    {
        return mGoogleApiClient;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        sendLog((new StringBuilder()).append("onActivityResult - requestCode = ").append(requestCode).append(" - resultCode = ").append(resultCode).append(" - data = ").append(data).toString());

        if(requestCode == REQUEST_RESOLVE_ERROR)
        {
            mResolvingError = false;
            switch(resultCode)
            {
            case -1:
                if(!mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected())
                {
                    sendLog("onActivityResult - reconnecting");
                    mGoogleApiClient.connect();
                }
                break;
            case 0: // '\0'
                onSignInFailed(102);
                break;
            case 10004:
                onSignInFailed(101);
                break;
            case 10002:
            case 10006:
                onSignInFailed(100);
                break;
            default:
                break;

            }
        }
        else if(requestCode == REQUEST_LEADERBOARD){
        	//
        }
        else if(requestCode == REQUEST_ACHIEVEMENTS){
        	//
        }
    }

    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putBoolean("resolving_error", mResolvingError);
    }

    protected void onStart()
    {
        super.onStart();
        if(mResolvingError);
    }

    protected void onStop()
    {
        if(mGoogleApiClient != null)
        {
            mGoogleApiClient.disconnect();
            super.onStop();
        }
    }

    private void showErrorDialog(int errorCode)
    {
        sendLog((new StringBuilder()).append("showErrorDialog with errorCode = ").append(errorCode).toString());
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, this, REQUEST_RESOLVE_ERROR);
        errorDialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {

            public void onDismiss(DialogInterface dialog)
            {
                mResolvingError = false;
            }
        });
        errorDialog.show();
    }

    public boolean isSignedIn()
    {
        return mGoogleApiClient != null && mGoogleApiClient.isConnected();
    }

    private static final int ERROR_NETWORK_FAILURE = 100;
    private static final int ERROR_APP_MISCONFIGURED = 101;
    private static final int ERROR_CANCELLED = 102;
    private static final String STATE_RESOLVING_ERROR = "resolving_error";
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    private boolean mResolvingError;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean debugLog = true;

    public static int REQUEST_LEADERBOARD = 1002;
    public static int REQUEST_ACHIEVEMENTS = 1003;

}
