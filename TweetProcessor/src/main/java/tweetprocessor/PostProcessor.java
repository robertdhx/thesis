package tweetprocessor;

import tweetprocessor.data.Profile;

import java.util.Map;


public class PostProcessor implements Processor {
	private Map<Long, Profile> profiles;


	public PostProcessor(Map<Long, Profile> profiles) {
		this.profiles = profiles;
		doProcessing();
	}


	public Map<Long, Profile> getProfiles() {
		return profiles;
	}


	public void doProcessing() {
		int minimumTweets = Config.getInstance().getMinimumTweets();
		System.out.println("Removing profiles with fewer than " + minimumTweets + " tweets...");
		profiles.values().removeIf(e -> (e.getTweetList().size() < minimumTweets));
		System.out.println("Complete!");
	}
}
