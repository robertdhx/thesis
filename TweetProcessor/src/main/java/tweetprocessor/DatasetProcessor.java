package tweetprocessor;

import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;
import tweetprocessor.util.JsonUtil;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class DatasetProcessor implements Processor {
	private Map<String, Profile> profiles;

	private File file;


	public DatasetProcessor(File file) {
		this.file = file;
		doProcessing();
	}


	public Map<String, Profile> getProfiles() {
		return profiles;
	}


	public void doProcessing() {
		profiles = JsonUtil.readJsonOutput(file);
		// setNumberOfTweets(70);
		// groupAndReduceProfiles();
		// JsonUtil.writeJsonOutput(profiles, "updated");
	}


	private void setNumberOfTweets(int number) {
		System.out.println("Setting number of tweets...");
		Random random = new Random();
		for (Profile profile : profiles.values()) {
			List<Tweet> tweetList = profile.getTweetList();
			while (tweetList.size() > number) {
				tweetList.remove(random.nextInt(tweetList.size()));
			}
		}
	}


	private void groupAndReduceProfiles() {
		Map<String, List<Profile>> groupedByProvince = profiles.values().stream()
				.collect(Collectors.groupingBy(p -> p.getPredictedLocation().getProvince()));
		Map<String, Profile> updatedMap = new HashMap<>();
		Random random = new Random();

		for (List<Profile> profileList : groupedByProvince.values()) {
			while (profileList.size() > 358) {
				profileList.remove(random.nextInt(profileList.size()));
			}
			for (Profile profile : profileList) {
				updatedMap.put(profile.getId(), profile);
			}
		}
		profiles = updatedMap;
	}
}
