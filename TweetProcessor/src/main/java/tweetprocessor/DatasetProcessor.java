package tweetprocessor;

import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;
import tweetprocessor.util.JsonUtil;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class DatasetProcessor implements Processor {
	private Map<Profile, List<Tweet>> profilesAndTweets;

	private File file;


	public DatasetProcessor(File file) {
		this.file = file;
		doProcessing();
	}


	public Map<Profile, List<Tweet>> getProfilesAndTweets() {
		return profilesAndTweets;
	}


	public void doProcessing() {
		profilesAndTweets = JsonUtil.readJsonOutput(file);
		showStatistics();

		/*System.out.println("Number of profiles: " + profilesAndTweets.keySet().size());

		File file = new File("foo.txt");
		try (FileWriter writer = new FileWriter(file)) {
			System.out.print("Writing to file... ");
			for (Map.Entry<Profile, List<Tweet>> entry : profilesAndTweets.entrySet()) {
				Profile profile = entry.getKey();
				writer.write(profile.getLocation() + ", predicted: " + profile.getPredictedLocation() + "\n");
			}
		} catch (IOException e) {

		}*/
	}


	private void showStatistics() {
		Map<String, List<Profile>> groupedByProvince = profilesAndTweets.keySet().stream()
				.collect(Collectors.groupingBy(p -> p.getPredictedLocation().getProvince()));
	}
}
