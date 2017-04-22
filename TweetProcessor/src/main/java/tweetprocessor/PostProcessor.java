package tweetprocessor;

import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

import java.util.List;
import java.util.Map;


public class PostProcessor implements Processor {
	private Map<Profile, List<Tweet>> profilesAndTweets;


	public PostProcessor(Map<Profile, List<Tweet>> profilesAndTweets) {
		this.profilesAndTweets = profilesAndTweets;
		doProcessing();
	}


	public Map<Profile, List<Tweet>> getProfilesAndTweets() {
		return profilesAndTweets;
	}


	public void doProcessing() {
		int minimumTweets = Config.getInstance().getMinimumTweets();
		System.out.println("Removing profiles with fewer than " + minimumTweets + " tweets...");
		profilesAndTweets.entrySet().removeIf(e -> (e.getValue().size() < minimumTweets));
		System.out.println("Complete!");
	}
}
