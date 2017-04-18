import data.PredictedLocation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class TweetProcessorTest {
	@Test
	public void testGuessLocation() throws Exception {
		PredictedLocation predictedLocation = TweetProcessor.guessLocation("Amsterdam, The Netherlands");
		PredictedLocation bestMatch = new PredictedLocation("Amsterdam", "Noord-Holland");
		assertEquals(predictedLocation, bestMatch);
	}
}
