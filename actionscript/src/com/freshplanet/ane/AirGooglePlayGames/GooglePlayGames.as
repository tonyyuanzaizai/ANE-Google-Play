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

package com.freshplanet.ane.AirGooglePlayGames
{
	import flash.events.Event;
	import flash.events.EventDispatcher;
	import flash.events.StatusEvent;
	import flash.external.ExtensionContext;
	import flash.system.Capabilities;
	
	public class GooglePlayGames extends EventDispatcher
	{
		// --------------------------------------------------------------------------------------//
		//																						 //
		// 									   PUBLIC API										 //
		// 																						 //
		// --------------------------------------------------------------------------------------//
		
		/** AirAlert is supported on Android devices. */
		public static function get isSupported() : Boolean
		{
			return Capabilities.manufacturer.indexOf("Android") != -1;
		}
		
		public function GooglePlayGames()
		{ 
			if (!_instance)
			{
				_context = ExtensionContext.createExtensionContext(EXTENSION_ID, null);
				if (!_context)
				{
					throw Error("ERROR - Extension context is null. Please check if extension.xml is setup correctly.");
					return;
				}
				_context.addEventListener(StatusEvent.STATUS, onStatus);
				
				_instance = this;
			}
			else
			{
				throw Error("This is a singleton, use getInstance(), do not call the constructor directly.");
			}
		}
		
		public static function getInstance() : GooglePlayGames
		{
			return _instance ? _instance : new GooglePlayGames();
		}
		
		public function startAtLaunch():void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("startAtLaunch");
			}
		}
		
		public function signIn():void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("signIn");
			}
		}
		
		public function signOut():void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("signOut");
			}
		}
		
		public function showAchievements():void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("showAchievements");
			}
		}
		
		public function incrementAchievement(achievementId:String, percent:Number = 0):void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("incrementAchievement", achievementId, percent);
			}
		}
		public function unlockAchievement(achievementId:String):void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("unlockAchievement", achievementId);
			}
		}
		
		public function showLeaderboard(leaderboardId:String):void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("showLeaderboard", leaderboardId);
			}
		}
		
		public function submitScore(leaderboardId:String, newScore:Number):void
		{
			if (GooglePlayGames.isSupported)
			{
				_context.call("submitScore", leaderboardId, newScore);
			}
		}
		
		public function getActivePlayerName():String
		{
			var name:String;
			if (GooglePlayGames.isSupported)
			{
				name = _context.call("getActivePlayerName") as String;
			}
			return name;
		}
		
		public function getActivePlayerId():String
		{
			var name:String;
			if (GooglePlayGames.isSupported)
			{
				name = _context.call("getActivePlayerId") as String;
			}
			return name;
		}
		
		
		// --------------------------------------------------------------------------------------//
		//																						 //
		// 									 	PRIVATE API										 //
		// 																						 //
		// --------------------------------------------------------------------------------------//
		
		private static const EXTENSION_ID : String = "com.freshplanet.googleplaygames";
		
		private static var _instance : GooglePlayGames;
		
		private var _context : ExtensionContext;
		
		private function onStatus( event : StatusEvent ) : void
		{
			trace("[GooglePlayGames]", event);
			var e:Event;
			if (event.code == "ON_SIGN_IN_SUCCESS")
			{
				e = new GooglePlayGamesEvent(GooglePlayGamesEvent.ON_SIGN_IN_SUCCESS);
			} else if (event.code == "ON_SIGN_IN_FAIL")
			{
				e = new GooglePlayGamesEvent(GooglePlayGamesEvent.ON_SIGN_IN_FAIL);
			} else if (event.code == "ON_SIGN_OUT_SUCCESS")
			{
				e = new GooglePlayGamesEvent(GooglePlayGamesEvent.ON_SIGN_OUT_SUCCESS);
			} else if (event.code == "ON_LEADERBOARD_LOADED")
			{
				var jsonArray:Array = JSON.parse( event.level ) as Array;
				if( jsonArray ) {
					var leaderboard:GSLeaderboard = GSLeaderboard.fromJSONObject( jsonArray );
					if( leaderboard )
						e = new GooglePlayGamesLeaderboardEvent(GooglePlayGamesLeaderboardEvent.LEADERBOARD_LOADED, leaderboard);
				}
			} else if (event.code == "ON_LEADERBOARD_FAILED")
			{
				e = new Event(GooglePlayGamesLeaderboardEvent.LEADERBOARD_LOADING_FAILED );
			}
			
			if (e) {
				this.dispatchEvent(e);
			}
		}
	}
}