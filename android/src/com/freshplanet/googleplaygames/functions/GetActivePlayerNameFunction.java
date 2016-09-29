package com.freshplanet.googleplaygames.functions;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.freshplanet.googleplaygames.Extension;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

public class GetActivePlayerNameFunction implements FREFunction {

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		FREObject playerName = null;
		try {
			playerName = FREObject.newObject(Extension.context.playerName);
		} catch (FREWrongThreadException e) {
			e.printStackTrace();
		}
		
		return playerName;
	}

}
