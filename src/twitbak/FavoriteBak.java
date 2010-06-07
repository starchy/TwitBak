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

import twitter4j.RateLimitStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.internal.org.json.JSONException;

/**
 * Backs up a user's favorited messages.
 * 
 * @author David @starchy Grant
 *
 */
class FavoriteBak extends StatusBak {

	FavoriteBak(Twitter twitter, User user) {
		super(twitter, user);
	}
	
	/**
	 * Retrieves page n of user's Status messages. The API only lets us grab 20 at a time.
	 */
	public List<Status> getStatusPage(int n) throws TwitterException, InterruptedException {
		RateLimitStatus rls = twitter().getRateLimitStatus();
		if (rls.getRemainingHits() < 10) {
			System.out.println("Pausing to let Twitter's rate limiting catch up...");
			Thread.sleep(30000);
		}
		System.out.println("Retrieving a block of up to 20 favorites...");
		List<Status> statusPage = twitter().getFavorites(n);
		return statusPage;
	}
	
	@Override
	public void statusesToJson() throws TwitterException, JSONException {
		for (Status status : statuses) {
			statusToJson(status);
		}
		statusObject.put("Favorites",statusArray);
	}
	
}
