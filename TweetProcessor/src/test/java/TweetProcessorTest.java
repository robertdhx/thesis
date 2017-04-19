import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.*;
import org.junit.Test;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class TweetProcessorTest {
	@Test
	public void testGuessLocation() throws Exception {
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

		Multimap<Profile, Tweet> profilesAndTweets = HashMultimap.create();

		for (File file : fileList) {
			FileProcessor fileProcessor = new FileProcessor(file);
			fileProcessor.getProfilesAndTweets();
			profilesAndTweets.putAll(fileProcessor.getProfilesAndTweets());
		}
		assertEquals(1, profilesAndTweets.asMap().size());
	}
}