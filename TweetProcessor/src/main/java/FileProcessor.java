import data.Profile;
import data.Tweet;
import twitter4j.*;
import util.StringUtil;

import java.io.*;
import java.util.*;


class FileProcessor {
	private File file;


	FileProcessor(File file) {
		this.file = file;
	}


	Map<Profile, Set<Tweet>> getProfilesAndTweets() throws IOException, TwitterException {
		Map<Profile, Set<Tweet>> profilesAndTweets = new HashMap<>();
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
					if (profilesAndTweets.containsKey(profile)) {
						Set<Tweet> tweetSet = profilesAndTweets.get(profile);
						tweetSet.add(tweet);
					} else {
						Set<Tweet> tweetSet = new HashSet<>();
						tweetSet.add(tweet);
						profilesAndTweets.put(profile, tweetSet);
					}
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
		return Config.getInstance().getFirstNamesSet().contains(firstName.toLowerCase());
	}
}
