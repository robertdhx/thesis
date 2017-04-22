package tweetprocessor;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import tweetprocessor.data.*;
import tweetprocessor.util.LocationUtil;
import tweetprocessor.util.StringUtil;

import java.io.*;
import java.util.*;


public class FileProcessor implements Processor {
	private Map<Profile, List<Tweet>> profilesAndTweets;

	private File file;


	public FileProcessor(Map<Profile, List<Tweet>> profilesAndTweets, File file) {
		this.profilesAndTweets = profilesAndTweets;
		this.file = file;
		doProcessing();
	}


	public Map<Profile, List<Tweet>> getProfilesAndTweets() {
		return profilesAndTweets;
	}


	public void doProcessing() {
		System.out.println("Processing file " + file.getName() + "...");
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
			System.out.println("Attempting to set predicted location for each profile...");
			profilesAndTweets.forEach((k, v) -> k.setPredictedLocation(LocationUtil.guessLocation(k.getLocation())));

			System.out.println("Performing clean-up...");
			profilesAndTweets.keySet().removeIf(p -> p.getPredictedLocation() == null);
			profilesAndTweets.keySet().removeIf(ProfilePredicates.hasBelgianLocation());
			profilesAndTweets.keySet().removeIf(ProfilePredicates.hasEdgeCaseLocation());
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
