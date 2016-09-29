package com.freshplanet.googleplaygames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.googleplaygames.Extension;

/**
 * Created by renaud on 09/09/2014.
 */
public class ShowLeaderboardFunction implements FREFunction {

    @Override
    public FREObject call(FREContext arg0, FREObject[] arg1) {
        // Retrieve alert parameters
        String leaderboardId = null;
        try
        {
            leaderboardId = arg1[0].getAsString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        Extension.context.showLeaderboard( leaderboardId );
		return null;
    }
}