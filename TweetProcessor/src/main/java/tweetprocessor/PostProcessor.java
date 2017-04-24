package tweetprocessor;

import tweetprocessor.data.Profile;

import java.util.Map;


public class PostProcessor implements Processor {
	private Map<String, Profile> profiles;


	public PostProcessor(Map<String, Profile> profiles) {
		this.profiles = profiles;
		doProcessing();
	}


	public Map<String, Profile> getProfiles() {
		return profiles;
	}

	public void doProcessing() {
		int minimumTweets = Config.getInstance().getMinimumTweets();
		System.out.println("Removing profiles with fewer than " + minimumTweets + " tweets...");
		profiles.values().removeIf(p -> (p.getTweetList().size() < minimumTweets));
		System.out.println("Complete!");
	}
}
