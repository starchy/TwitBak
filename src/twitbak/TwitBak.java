/*
 * Copyright 2010 David "Starchy" Grant
 *
 * This file is part of TwitBak.
 * 
 * TwitBak is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package twitbak;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Main control object for providing the Twitter object, which represents the entire service, and the user.
 * 
 * @author David @starchy Grant
 * @param username
 * @param password
 * @param filename
 * @throws TwitterException
 */
public class TwitBak {
	
	private final String username, password, filename;
	final Twitter twitter;
	final User user;
	
	public TwitBak(String username, String password, String filename) throws TwitterException {
		this.username = username;
		this.password = password;
		this.filename = filename;
		
		TwitterFactory twitterFactory = new TwitterFactory();
		this.twitter = twitterFactory.getInstance(username,password);
		this.user = this.twitter.verifyCredentials();
	}
	
	/**
	 * Returns a JSONObject containing user data.
	 * @param user
	 * @return
	 * @throws TwitterException
	 * @throws JSONException
	 */
	//TODO - should be void to match other Bak classes 
	static JSONObject userToJson(User user) throws TwitterException, JSONException {
		JSONObject result = new JSONObject();
		JSONObject userData	= new JSONObject();
		userData.put("ID",user.getId());
		userData.put("Screen Name",user.getScreenName());
		userData.put("Name",user.getName());
		userData.put("Description",user.getDescription());
		userData.put("Profile Image URL",user.getProfileImageURL());
		userData.put("URL",user.getURL());
		userData.put("Protected",user.isProtected());
		userData.put("Followers",user.getFollowersCount());
		userData.put("Created At",user.getCreatedAt().toString());
		userData.put("Favorites",user.getFavouritesCount());
		userData.put("Friends",user.getFriendsCount());
		userData.put("Location",user.getLocation());
		userData.put("Statuses",user.getStatusesCount());
		userData.put("Profile Background Color",user.getProfileBackgroundColor());
		userData.put("Profile Background Image URL",user.getProfileBackgroundImageUrl());
		userData.put("Profile Sidebar Border Color",user.getProfileSidebarBorderColor());
		userData.put("Profile Sidebar Fill Color",user.getProfileSidebarFillColor());
		userData.put("Profile Text Color",user.getProfileTextColor());
		userData.put("Time Zone",user.getTimeZone());
		result.put("User data", userData);
		return result;
	}

}
