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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Logic for running a backup with all options enabled
 * @author David @starchy Grant
 *
 */
public class FullBackup {
	
	private final String username, password, filename;
	
	public FullBackup(String username, String password, String filename) throws TwitterException, 
	IOException, InterruptedException, JSONException { //FIXME - needs to except filename, should not be public
		this.username = username;
		this.password = password;
		this.filename = filename;
		this.backup();
	}
	
	private void backup() throws TwitterException, IOException, InterruptedException, JSONException {
		
		//Open the Twitter connection and backup user data.
		TwitBak twitbak = new TwitBak(username,password,filename);
		JSONObject userJSON = TwitBak.userToJson(twitbak.user); 
        
        JSONArray fullJSON = new JSONArray(); 
        fullJSON.put(userJSON);
		
        //Backup statuses
        StatusBak statusbak = new StatusBak(twitbak.twitter, twitbak.user);
		statusbak.getStatuses();
		statusbak.statusesToJson();
		fullJSON.put(statusbak.statusObject());
        
		//Backup Direct Messages
		DirectMessageBak dmbak = new DirectMessageBak(twitbak.twitter, twitbak.user);
		dmbak.getStatuses();
		dmbak.dmsToJson();
		fullJSON.put(dmbak.statusObject());
		
		//Backup Mentions
		MentionBak mentionbak = new MentionBak(twitbak.twitter,twitbak.user);
		mentionbak.getStatuses();
		mentionbak.statusesToJson();
		fullJSON.put(mentionbak.statusObject());
		
		//Backup favorites
		FavoriteBak favoritebak = new FavoriteBak(twitbak.twitter,twitbak.user);
		favoritebak.getStatuses();
		favoritebak.statusesToJson();
		fullJSON.put(favoritebak.statusObject());
		
		FileWriter fstream = new FileWriter(filename);
		System.out.println("Opened FileWriter " + filename);
        BufferedWriter out = new BufferedWriter(fstream);
        System.out.println("Opened BufferedWriter fstream");
        out.write(fullJSON.toString(4));
		out.close();
	    System.out.println("Done!");
		
	}
}