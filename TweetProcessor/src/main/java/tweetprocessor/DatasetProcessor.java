package tweetprocessor;

import tweetprocessor.data.Profile;
import tweetprocessor.util.JsonUtil;

import java.io.File;
import java.util.Map;


public class DatasetProcessor implements Processor {
	private Map<Long, Profile> profiles;

	private File file;


	public DatasetProcessor(File file) {
		this.file = file;
		doProcessing();
	}


	public Map<Long, Profile> getProfiles() {
		return profiles;
	}


	public void doProcessing() {
		profiles = JsonUtil.readJsonOutput(file);
		System.out.println("Normalizing number of tweets...");

		/*Random random = new Random();

		for (List<Tweet> tweetList : profiles.values()) {
			while (tweetList.size() > 70) {
				tweetList.remove(random.nextInt(tweetList.size()));
			}
		}

		for (Map.Entry<Profile, List<Tweet>> entry : profilesAndTweets.entrySet()) {
			entry.getKey().setTweetList(entry.getValue());
			entry.setValue(null);
		}


		Map<String, List<Profile>> groupedByProvince = profilesAndTweets.keySet().stream()
				.collect(Collectors.groupingBy(p -> p.getPredictedLocation().getProvince()));

		Random secondRandom = new Random();

		for (List<Profile> profileList : groupedByProvince.values()) {
			while (profileList.size() > 350) {
				profileList.remove(secondRandom.nextInt(profileList.size()));
			}
		}

		System.out.println("check!");
*/


		// System.out.println("Number of profiles: " + profilesAndTweets.keySet().size());

	}


	private void showStatistics() {

	}
}
