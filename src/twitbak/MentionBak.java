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
import twitter4j.internal.org.json.JSONException;
import twitter4j.internal.org.json.JSONObject;

/**
 * Backs up messages that mention a user (aka @-replies).
 * 
 * @author David @starchy Grant
 *
 */
class MentionBak extends StatusBak {

	MentionBak(Twitter twitter, User user) {
		super(twitter, user);
	}
	
	public List<Status> getStatusPage(int n) throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter().getRateLimitStatus();
		if (rls.getRemainingHits() < 10) {
			System.out.println("Pausing to let Twitter's rate limiting catch up...");
			Thread.sleep(30000);
		}
		System.out.println("Retrieving a block of up to 200 mentions...");
		List<Status> statusPage = twitter().getMentions(new Paging(n,200));
		return statusPage;
	}
	
	public void statusToJson(Status status) throws TwitterException, JSONException {
		JSONObject result = new JSONObject();
		User poster = status.getUser();
		result.put("Poster ID",poster.getId());
		result.put("Poster",poster.getScreenName());
		result.put("Poster Name",poster.getName());
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
		statusArray().put(result);
	}
	
	@Override
	public void statusesToJson() throws TwitterException, JSONException {
		for (Status status : statuses) {
			statusToJson(status);
		}
		statusObject.put("Mentions",statusArray);
	}
}
