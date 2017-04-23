package tweetprocessor;

import tweetprocessor.data.Profile;

import java.util.Map;


public interface Processor {
	void doProcessing();

	Map<Long, Profile> getProfiles();
}
