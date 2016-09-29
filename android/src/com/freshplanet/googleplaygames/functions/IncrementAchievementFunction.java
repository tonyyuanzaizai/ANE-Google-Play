package com.freshplanet.googleplaygames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.googleplaygames.Extension;

public class IncrementAchievementFunction implements FREFunction {

	public IncrementAchievementFunction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		// TODO Auto-generated method stub
		
		// Retrieve alert parameters
		String achievementId = null;
		int newAchievement = 0;
		try
		{
			achievementId = arg1[0].getAsString();
			newAchievement = arg1[1].getAsInt();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		Extension.context.incrementAchievement(achievementId, newAchievement);
		
		return null;
	}

}
