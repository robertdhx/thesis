package tweetprocessor;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

import java.io.File;
import java.util.List;
import java.util.Map;


public class DatasetProcessor implements Processor {
	private Map<Profile, List<Tweet>> profilesAndTweets;

	private File file;


	public DatasetProcessor(File file) {
		this.file = file;
		doProcessing();
	}


	public Map<Profile, List<Tweet>> getProfilesAndTweets() {
		throw new NotImplementedException();
	}


	public void doProcessing() {
		throw new NotImplementedException();
	}
}
