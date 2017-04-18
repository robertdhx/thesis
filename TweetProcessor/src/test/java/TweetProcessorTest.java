import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.*;
import org.junit.Test;

import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class TweetProcessorTest {
	@Test
	public void testGuessLocation() throws Exception {
		PredictedLocation predictedLocation = TweetProcessor.guessLocation("Amsterdam, The Netherlands");
		PredictedLocation bestMatch = new PredictedLocation("Amsterdam", "Noord-Holland");
		assertEquals(predictedLocation, bestMatch);
	}


	@Test
	public void testJsonInput() throws Exception {
		Type typeOf = new TypeToken<Map<Profile, Collection<Tweet>>>() {
		}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		Map<Profile, Collection<Tweet>> test = gson.fromJson(new FileReader("output_test.json"), typeOf);

		for (Profile profile : test.keySet()) {
			System.out.println(profile.getPredictedLocation());
		}
	}
}