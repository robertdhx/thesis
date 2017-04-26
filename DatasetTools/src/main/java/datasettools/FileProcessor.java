package datasettools;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import datasettools.data.*;
import datasettools.util.LocationUtil;
import datasettools.util.StringUtil;

import java.io.*;
import java.util.*;


public class FileProcessor implements Processor {
	private Map<String, Profile> profiles;

	private File file;


	public FileProcessor(Map<String, Profile> profiles, File file) {
		this.profiles = profiles;
		this.file = file;
		doProcessing();
	}


	public Map<String, Profile> getProfiles() {
		return profiles;
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
					Tweet tweet = new Tweet(fullTweet);
					String userId = user.get("id_str").getAsString();
					if (profiles.containsKey(userId)) {
						Profile profile = profiles.get(userId);
						profile.getTweetList().add(tweet);
					} else {
						Profile profile = new Profile(user);
						profile.getTweetList().add(tweet);
						profiles.put(profile.getId(), profile);
					}
				}
			}
			System.out.println("Attempting to set predicted location for each profile...");
			profiles.values().forEach(p -> p.setPredictedLocation(LocationUtil.guessLocation(p.getLocation())));

			System.out.println("Performing clean-up...");
			profiles.values().removeIf(p -> p.getPredictedLocation() == null);
			profiles.values().removeIf(ProfilePredicates.hasBelgianLocation());
			profiles.values().removeIf(ProfilePredicates.hasEdgeCaseLocation());
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
