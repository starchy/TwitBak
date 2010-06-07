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

import twitter4j.Paging;
import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.internal.org.json.JSONArray;
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Backs up a users status messages.
 * 
 * @author David @starchy Grant
 *
 */
class StatusBak implements MessageBak {
	
	private final Twitter twitter;
	private final User user;
	protected List<Status> statuses;
	protected JSONArray statusArray;
	protected JSONObject statusObject;
	
	public StatusBak (Twitter twitter, User user) {
		this.twitter = twitter;
		this.user = user;
		statusArray = new JSONArray();
		statusObject = new JSONObject();
	}
	
	public JSONArray statusArray() { return statusArray; }
	
	public JSONObject statusObject() { return statusObject; }
	
	public Twitter twitter() { return twitter; }
	
	/**
	 * Retrieves page n of user's Status messages. Hard coded to max retrieval value of 200
	 * to help mitigate rate limiting issues.
	 */
	public List<Status> getStatusPage(int n) throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter.getRateLimitStatus();
		if (rls.getRemainingHits() < 10) {
			System.out.println("Pausing to let Twitter's rate limiting catch up...");
			Thread.sleep(30000);
		}
		System.out.println("Retrieving status updates starting from number " + n * 200);
		List<Status> statusPage = twitter.getUserTimeline(new Paging(n,200));
		System.out.println("Retrieved status updates starting from number " + n * 200);
		return statusPage;
	}
	
	/**
	 * Retrieves all of user's Status messages.
	 */
	public void getStatuses() throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter.getRateLimitStatus();
		System.out.println("API calls remaining: " + rls.getRemainingHits());
		int pageNum = 1;
		statuses = this.getStatusPage(pageNum);
		if (!statuses.isEmpty()) {
			List<Status> nextPage = statuses;
			while (!nextPage.isEmpty()) {
				pageNum++;
				System.out.println("Calling getStatusPage for page " + pageNum);
				nextPage = this.getStatusPage(pageNum);
				statuses.addAll(nextPage);
			}
		}
	}
	
	/**
	 * Adds a Status to statusArray as a JSONObject.
	 * 
	 * @param status
	 * @throws TwitterException
	 * @throws JSONException
	 */
	public void statusToJson(Status status) throws TwitterException, JSONException {
		JSONObject result = new JSONObject();
		result.put("Created At",status.getCreatedAt().toString());
		result.put("ID",status.getId());
		result.put("Text",status.getText());
		long inReplyToStatusId = status.getInReplyToStatusId();
		if (inReplyToStatusId != -1) {
			result.put("In Reply To Status ID",status.getInReplyToStatusId());
		}
		long inReplyToUserID = status.getInReplyToUserId();
		result.put("In Reply To User ID",inReplyToUserID);
		if (inReplyToUserID != -1) {
			result.put("In Reply To Screen Name",status.getInReplyToScreenName());
		}
		boolean isFavorited = status.isFavorited();
		if (isFavorited) {
			result.put("Favorited",status.isFavorited());
		}
		statusArray.put(result);
	}
	
	/**
	 * Converts all of user's statuses to JSONObjects.
	 * 
	 * @throws TwitterException
	 * @throws JSONException
	 */
	public void statusesToJson() throws TwitterException, JSONException {
		for (Status status : statuses) {
			statusToJson(status);
		}
		statusObject.put("Statuses",statusArray);
	}
	
}
