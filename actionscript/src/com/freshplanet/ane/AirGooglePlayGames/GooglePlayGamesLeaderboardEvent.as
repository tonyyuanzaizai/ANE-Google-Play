package com.freshplanet.ane.AirGooglePlayGames
{
	import flash.events.Event;

	public class GooglePlayGamesLeaderboardEvent extends Event
	{
		
		public static const LEADERBOARD_LOADED:String = "GooglePlayGamesLeaderboardEvent.leaderboard_loaded";
		public static const LEADERBOARD_LOADING_FAILED:String = "GooglePlayGamesLeaderboardEvent.leaderboard_loading_failed";
		
		public var leaderboard:GSLeaderboard;
		
		public function GooglePlayGamesLeaderboardEvent( type:String, leaderboard:GSLeaderboard )
		{
			super( type );
			
			this.leaderboard = leaderboard;
			
		}
		
	}
}