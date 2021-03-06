package com.freshplanet.googleplaygames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.googleplaygames.Extension;

public class SubmitScoreFunction implements FREFunction {

	public SubmitScoreFunction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		// TODO Auto-generated method stub
		
		// Retrieve alert parameters
		String leaderboardId = null;
		int newScore = 0;
		try
		{
			leaderboardId = arg1[0].getAsString();
			newScore = arg1[1].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		Extension.context.submitScore(leaderboardId, newScore);
		
		return null;
	}

}
