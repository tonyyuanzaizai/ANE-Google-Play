package com.freshplanet.googleplaygames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.freshplanet.googleplaygames.Extension;

public class ShowAchievementsFunction implements FREFunction {
	
    final int RC_UNUSED = 5001;
    
	public ShowAchievementsFunction() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
        Extension.context.showAchievements();
		return null;
	}

}
