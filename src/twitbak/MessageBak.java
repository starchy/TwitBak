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

import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONObject;

/**
 * Pattern for a class to backup Twitter messages.
 * 
 * @author David @starchy Grant
 *
 */
interface MessageBak {
	
	JSONArray statusArray(); // Messages collected; will be phased out in favor of a SQLJet solution.
	JSONObject statusObject();
	
	Twitter twitter(); // Class representing Twitter connection
	
	List<?> getStatusPage(int n) throws TwitterException, InterruptedException; // get n messages
	
	void getStatuses() throws TwitterException, InterruptedException; // get all messages
	
}
