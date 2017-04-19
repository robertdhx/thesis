import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data.Profile;
import data.Tweet;
import util.StringUtil;

import java.io.*;
import java.util.*;


class FileProcessor {
	private File file;


	FileProcessor(File file) {
		this.file = file;
	}


	Map<Profile, List<Tweet>> getProfilesAndTweets() throws IOException {
		Map<Profile, List<Tweet>> profilesAndTweets = new HashMap<>();

		try (Reader reader = new FileReader(file)) {
			BufferedReader br = new BufferedReader(reader);
			String line;
			while ((line = br.readLine()) != null) {
				if (!containsLocation(line) || containsRetweet(line)) {
					continue;
				}
				JsonObject fullTweet = new JsonParser().parse(line).getAsJsonObject();
				JsonObject user = fullTweet.getAsJsonObject("user");

				if (isRealPerson(user.get("name").getAsString())) {
					Profile profile = new Profile(user);
					Tweet tweet = new Tweet(fullTweet);
					if (profilesAndTweets.containsKey(profile)) {
						List<Tweet> tweetList = profilesAndTweets.get(profile);
						tweetList.add(tweet);
					} else {
						List<Tweet> tweetList = new ArrayList<>();
						tweetList.add(tweet);
						profilesAndTweets.put(profile, tweetList);
					}
				}
			}
		}
		return profilesAndTweets;
	}


	private boolean containsLocation(String line) {
		return !line.contains("location\":null");
	}


	private boolean containsRetweet(String line) {
		return line.contains("retweeted_status\":");
	}


	private boolean isRealPerson(String name) {
		String firstName = StringUtil.getFirstName(name);
		return Config.getInstance().getFirstNamesSet().contains(firstName.toLowerCase());
	}
}
