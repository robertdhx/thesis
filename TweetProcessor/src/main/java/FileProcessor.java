import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data.Profile;
import data.Tweet;
import util.StringUtil;

import java.io.*;
import java.util.*;


class FileProcessor implements Processor {
	private Map<Profile, List<Tweet>> profilesAndTweets;

	private File file;


	FileProcessor(Map<Profile, List<Tweet>> profilesAndTweets, File file) {
		this.profilesAndTweets = profilesAndTweets;
		this.file = file;
		doProcessing();
	}

	public Map<Profile, List<Tweet>> getProfilesAndTweets() {
		return profilesAndTweets;
	}


	public void doProcessing() {
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
		} catch (IOException e) {
			System.out.println("IO error: " + e.getMessage());
		}
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
