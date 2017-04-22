import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import tweetprocessor.*;
import tweetprocessor.data.*;
import tweetprocessor.util.JsonUtil;
import tweetprocessor.util.LocationUtil;

import java.io.*;
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

		PredictedLocation predictedLocation = LocationUtil.guessLocation("Mechelen");
		PredictedLocation bestMatch = new PredictedLocation("Mechelen", "LI", "NL");
		assertEquals(predictedLocation, bestMatch);
	}


	@Test
	public void testDeserialization() throws Exception {
		Type typeOf = new TypeToken<HashMap<Profile, Collection<Tweet>>>() {
		}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Map<Profile, Collection<Tweet>> test = gson.fromJson(new FileReader("20170419-output-75.json"), typeOf);

		System.out.println("Number of profiles: " + test.keySet().size());

		File file = new File("foo.txt");
		try (FileWriter writer = new FileWriter(file)) {
			System.out.print("Writing raw... ");
			for (Map.Entry<Profile, Collection<Tweet>> entry : test.entrySet()) {
				Profile profile = entry.getKey();
				writer.write(profile.getLocation() + ", predicted: " + profile.getPredictedLocation() + "\n");
			}
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
			profile.setPredictedLocation(LocationUtil.guessLocation(profile.getLocation()));
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

		for (File inputFile : fileList) {
			System.out.println("Processing file " + inputFile.getName() + "...");
			Processor fileProcessor = new FileProcessor(new HashMap<>(), inputFile);
			JsonUtil.writeJsonOutput(fileProcessor.getProfilesAndTweets(), inputFile.getName());
		}

		Map<Profile, List<Tweet>> profilesAndTweets = new HashMap<>();
		System.out.println("Merging results...");

		for (File inputFile : fileList) {
			File outputFile = new File("output_" + inputFile.getName());
			JsonUtil.readJsonOutput(outputFile).forEach((k, v) -> profilesAndTweets.merge(k, v, (s1, s2) -> {
				List<Tweet> tweetList = new ArrayList<>(s1);
				tweetList.addAll(s2);
				return tweetList;
			}));
		}

		assertEquals(1, profilesAndTweets.size());

		for (List<Tweet> tweetList : profilesAndTweets.values()) {
			assertEquals(2, tweetList.size());
		}
	}


	@Test
	public void checkShortPredictedLocations() throws Exception {
		Config config = Config.getInstance();
		config.buildPredictedLocationSet();

		for (PredictedLocation predictedLocation : config.getPredictedLocationSet()) {
			if (predictedLocation.getPlace().length() <= 2 && predictedLocation.getCountry().equals("NL")) {
				System.out.println(predictedLocation);
			}
		}
	}
}