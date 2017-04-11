import com.google.gson.Gson;
import data.PredictedLocation;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;


public class TweetProcessorTest {
	@Test
	public void testJsonOutput() throws Exception {
		List<PredictedLocation> predictedLocationList = new ArrayList<>();

		PredictedLocation predictedLocation = new PredictedLocation("Schipborg", "Drenthe");
		PredictedLocation anotherPredictedLocation = new PredictedLocation("Groningen", "Groningen");

		predictedLocationList.add(predictedLocation);
		predictedLocationList.add(anotherPredictedLocation);

		Gson gson = new Gson();
		System.out.println(gson.toJson(predictedLocationList));
		assertNotNull(gson);
	}

	@Test
	public void testGetResourceFile() throws Exception {
		try (InputStream in = TweetProcessor.class.getResourceAsStream("/places.json")) {
			String result = IOUtils.toString(in, StandardCharsets.UTF_8);
			System.out.println(result);
		}
	}
}
