import data.Profile;
import data.Tweet;

import java.util.List;
import java.util.Map;


public interface Processor {
	void doProcessing();

	Map<Profile, List<Tweet>> getProfilesAndTweets();
}
