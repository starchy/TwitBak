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

import twitter4j.DirectMessage;
import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Backs up a user's direct messages.
 * 
 * @author David @starchy Grant
 *
 */
class DirectMessageBak implements MessageBak{
	
	private final Twitter twitter;
	private final User user;
	private List<DirectMessage> statuses;
	private JSONArray statusArray;
	private JSONObject statusObject;
	
	DirectMessageBak (Twitter twitter, User user) {
		this.twitter = twitter;
		this.user = user;
		this.statusArray = new JSONArray();
		statusObject = new JSONObject();
	}
	
	public JSONArray statusArray () { return statusArray; }
	
	public JSONObject statusObject() { return statusObject; }
	
	
	/**
	 * Retrieves page n of user's direct messages. Hard coded to max retrieval value of 200
	 * to help mitigate rate limiting issues.
	 */
	public List<DirectMessage> getStatusPage(int n) throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter.getRateLimitStatus();
		if (rls.getRemainingHits() < 10) {
			System.out.println("Pausing to let Twitter's rate limiting catch up...");
//			BackupWorker.setStatus("Pausing to let Twitter's rate limiting catch up...");
			Thread.sleep(30000);
		}
		System.out.println("Retrieving a block of up to 200 direct messages...");
		List<DirectMessage> dmPage = twitter.getDirectMessages(new Paging(n,200));
		return dmPage;
	}
	
	/**
	 * Retrieves all of user's direct messages.
	 */
	public void getStatuses() throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter.getRateLimitStatus();
//		BackupWorker.setStatus("API calls remaining: " + rls.getRemainingHits());
		int pageNum = 1;
		statuses = this.getStatusPage(pageNum);
		if (!statuses.isEmpty()) {
			List<DirectMessage> nextPage = statuses;
			while (!nextPage.isEmpty()) {
				pageNum++;
				nextPage = this.getStatusPage(pageNum);
				statuses.addAll(nextPage);
			}
		}
	}
	
	/**
	 * Adds a DirectMessage to statusArray as a JSONObject.
	 * 
	 * @param dm
	 * @throws TwitterException
	 * @throws JSONException
	 */
	private void dmToJson(DirectMessage dm) throws TwitterException, JSONException {
		JSONObject result = new JSONObject();
		result.put("Created At",dm.getCreatedAt().toString());
		result.put("ID",dm.getId());
		result.put("Text",dm.getText());
		result.put("Sender ID",dm.getSenderId());
		result.put("Sender",dm.getSender().getScreenName());
		result.put("Sender Name",dm.getSender().getName());
		statusArray.put(result);
	}
	
	/**
	 * Converts all of user's direct messages to JSONObjects.
	 * 
	 * @throws TwitterException
	 * @throws JSONException
	 */
	void dmsToJson() throws TwitterException, JSONException {
		for (DirectMessage dm : statuses) {
			dmToJson(dm);
		}
		statusObject.put("Direct Messages",statusArray);
	}

	public Twitter twitter() {
		return twitter;
	}

}
