import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import data.Profile;
import data.Tweet;
import twitter4j.*;
import util.StringUtil;

import java.io.*;


class FileProcessor {
	private File file;


	FileProcessor(File file) {
		this.file = file;
	}


	Multimap<Profile, Tweet> getProfilesAndTweets() throws IOException, TwitterException {
		Multimap<Profile, Tweet> profilesAndTweets = HashMultimap.create();
		int lineNumber = 0;

		try (Reader reader = new FileReader(file)) {
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				lineNumber++;
				if (!containsLocation(line)) {
					continue;
				}
				Status status = TwitterObjectFactory.createStatus(line);
				if (satisfiesConditions(status)) {
					Profile profile = new Profile(status.getUser());
					Tweet tweet = new Tweet(status);
					profilesAndTweets.put(profile, tweet);
				}
				if (lineNumber % 100000 == 0) {
					System.out.println("Processed " + lineNumber + " lines...");
				}
			}
		}
		return profilesAndTweets;
	}


	private boolean containsLocation(String line) {
		return !line.contains("location\":null");
	}


	private boolean satisfiesConditions(Status status) {
		return !isRetweet(status) && isIndividual(status.getUser());
	}


	private boolean isRetweet(Status status) {
		return status.isRetweet();
	}


	private boolean isIndividual(User user) {
		String firstName = StringUtil.getFirstName(user.getName());
		return Config.getInstance().getNameSet().contains(firstName.toLowerCase());
	}
}
