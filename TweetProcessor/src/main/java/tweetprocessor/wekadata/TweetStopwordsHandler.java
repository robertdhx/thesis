package tweetprocessor.wekadata;

import tweetprocessor.Config;
import weka.core.stopwords.StopwordsHandler;


public class TweetStopwordsHandler implements StopwordsHandler {
	@Override public boolean isStopword(String word) {
		return Config.getInstance().getStopwords().contains(word);
	}
}
