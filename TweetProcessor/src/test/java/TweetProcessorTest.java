import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.*;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TweetProcessorTest {
	@Test
	public void testGuessLocation() throws Exception {
		Config config = Config.getInstance();
		config.buildFirstNamesSet();
		config.buildPredictedLocationSet();

		PredictedLocation predictedLocation = PostProcessor.guessLocation("Amsterdam, The Netherlands");
		PredictedLocation bestMatch = new PredictedLocation("Amsterdam", "NH", "NL");
		assertEquals(predictedLocation, bestMatch);
	}


	@Test
	public void testDeserialization() throws Exception {
		Type typeOf = new TypeToken<HashMap<Profile, Collection<Tweet>>>() { }.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Map<Profile, Collection<Tweet>> test = gson.fromJson(new FileReader("20170419-output.json"), typeOf);

		for (Map.Entry<Profile, Collection<Tweet>> entry : test.entrySet()) {
			Profile profile = entry.getKey();
			System.out.println(profile.getLocation() + ", predicted: " + profile.getPredictedLocation());
		}
		assertNotNull(test);
	}


	@Test
	public void resetPredictedLocations() throws Exception {
		Config config = Config.getInstance();
		config.buildFirstNamesSet();
		config.buildPredictedLocationSet();

		Type typeOf = new TypeToken<HashMap<Profile, Collection<Tweet>>>() { }.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Map<Profile, Collection<Tweet>> test = gson.fromJson(new FileReader("20170419-output.json"), typeOf);

		System.out.println("Old size: " + test.keySet().size());

		for (Map.Entry<Profile, Collection<Tweet>> entry : test.entrySet()) {
			Profile profile = entry.getKey();
			// System.out.println("Old: " + profile.getLocation() + ", predicted: " + profile.getPredictedLocation());
			profile.setPredictedLocation(PostProcessor.guessLocation(profile.getLocation()));
			// System.out.println("New: " + profile.getLocation() + ", predicted: " + profile.getPredictedLocation());
		}
		test.keySet().removeIf(p -> p.getPredictedLocation() == null);

		System.out.println("New size: " + test.keySet().size());

		assertNotNull(test);
	}


	@Test
	public void testDuplicates() throws Exception {
		Config config = Config.getInstance();
		config.buildFirstNamesSet();
		config.buildPredictedLocationSet();
		config.setMinimumTweets(1);

		ClassLoader classLoader = this.getClass().getClassLoader();
		File testFile = new File(classLoader.getResource("sample.json").getFile());
		File anotherTestFile = new File(classLoader.getResource("sample2.json").getFile());

		List<File> fileList = new ArrayList<>();
		fileList.add(testFile);
		fileList.add(anotherTestFile);

		Map<Profile, Set<Tweet>> profilesAndTweets = new HashMap<>();

		for (File file : fileList) {
			FileProcessor fileProcessor = new FileProcessor(file);
			fileProcessor.getProfilesAndTweets().forEach((k, v) -> profilesAndTweets.merge(k, v, (s1, s2) -> {
				Set<Tweet> tweetSet = new HashSet<>(s1);
				tweetSet.addAll(s2);
				return tweetSet;
			}));
		}
		assertEquals(1, profilesAndTweets.size());

		for (Set<Tweet> tweetSet : profilesAndTweets.values()) {
			assertEquals(2, tweetSet.size());
		}
	}
}