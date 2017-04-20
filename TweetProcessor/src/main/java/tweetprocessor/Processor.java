package tweetprocessor;

import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

import java.util.List;
import java.util.Map;


public interface Processor {
	void doProcessing();

	Map<Profile, List<Tweet>> getProfilesAndTweets();
}
