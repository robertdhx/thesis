package datasettools;

import com.twitter.Extractor;
import datasettools.data.*;
import datasettools.util.ArffUtil;
import datasettools.util.JsonUtil;
import org.unbescape.html.HtmlEscape;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class ProfileProcessor implements Processor {
	private Map<String, Profile> profiles;

	private List<ArffInstance> arffInstanceList;

	private File file;


	public ProfileProcessor(File file) {
		this.file = file;
		doProcessing();
	}


	public Map<String, Profile> getProfiles() {
		return profiles;
	}


	public void doProcessing() {
		profiles = JsonUtil.readJsonOutput(file);
		for (Profile profile : profiles.values()) {
			setNumberOfTweets(profile, 70);
			for (Tweet tweet : profile.getTweetList()) {
				tweet.setText(doCleanup(tweet.getText()));
			}
			profile.setDescription(doCleanup(profile.getDescription()));
		}
		createArffInstanceList();
		ArffUtil.prepareArffOutput(arffInstanceList);
	}


	private String doCleanup(String text) {
		if (text != null) {
			text = removeMentions(text);
			text = removeUrls(text);
			text = removeHashtags(text);
			text = convertHtmlEntities(text);
			text = removeNewlines(text);
			text = removeUnicodeSymbols(text);
			text = removeEmoticons(text);
			text = removePunctuation(text);
			text = removeDoubleSpaces(text);
			return removeStopwords(text);
		}
		return "";
	}


	private void setNumberOfTweets(Profile profile, int number) {
		Random random = new Random();
		List<Tweet> tweetList = profile.getTweetList();
		while (tweetList.size() > number) {
			tweetList.remove(random.nextInt(tweetList.size()));
		}
	}


	private String removeMentions(String text) {
		Extractor extractor = new Extractor();
		List<String> list = extractor.extractMentionedScreennames(text);
		for (String mention : list) {
			text = text.replace("@" + mention, "");
		}
		return text;
	}


	private String removeHashtags(String text) {
		Extractor extractor = new Extractor();
		List<String> list = extractor.extractHashtags(text);
		for (String hashtag : list) {
			text = text.replace("#" + hashtag, "");
		}
		return text;
	}


	private String removeUrls(String text) {
		Extractor extractor = new Extractor();
		List<String> list = extractor.extractURLs(text);
		for (String url : list) {
			text = text.replace(url, "");
		}
		return text;
	}


	private String convertHtmlEntities(String text) {
		return HtmlEscape.unescapeHtml(text);
	}


	private String removeNewlines(String text) {
		return text.replaceAll("\\r|\\n", "");
	}


	private String removeDoubleSpaces(String text) {
		return text.trim().replaceAll(" +", " ");
	}


	private String removeUnicodeSymbols(String text) {
		return text.replaceAll("\\p{So}+", "");
	}


	private String removeEmoticons(String text) {
		String[] emoticons = {":)", ":(", ":P", "<3", ":-)", ":-(", ":-P"};
		for (String emoticon : emoticons) {
			text = text.replace(emoticon, "");
		}
		return text;
	}


	private String removePunctuation(String text) {
		return text.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}^\\s]", " ");
	}


	private String removeStopwords(String text) {
		String[] splitted = text.split("\\s+");
		List<String> allowedWords = new ArrayList<>();
		for (String word : splitted) {
			if (!Config.getInstance().getStopwords().contains(word.trim().toLowerCase())) {
				allowedWords.add(word);
			}
		}
		return String.join(" ", allowedWords);
	}


	private void createArffInstanceList() {
		arffInstanceList = new ArrayList<>();
		for (Profile profile : profiles.values()) {
			ArffInstance arffInstance = new ArffInstance();
			List<String> hashtagList = new ArrayList<>();
			for (Tweet tweet : profile.getTweetList()) {
				if (tweet.getHashtagList() != null) {
					hashtagList.addAll(tweet.getHashtagList());
				}
			}
			arffInstance.setDescription(profile.getDescription().toLowerCase());
			arffInstance.setTweetText(profile.getTweetList().stream().map(Tweet::toString)
					.collect(Collectors.joining(" ")).toLowerCase());
			arffInstance.setHashtags(String.join(" ", hashtagList).toLowerCase());
			arffInstance.setProvince(profile.getPredictedLocation().getProvince());
			arffInstanceList.add(arffInstance);
		}
	}
}
