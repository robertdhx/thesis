package datasettools;

import com.twitter.Extractor;
import datasettools.data.Profile;
import datasettools.data.Tweet;
import datasettools.util.ArffUtil;
import datasettools.util.JsonUtil;
import datasettools.data.ProfileTweets;
import org.unbescape.html.HtmlEscape;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;


public class ProfileProcessor implements Processor {
	private Map<String, Profile> profiles;

	private List<ProfileTweets> profileTweetsList;

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
		setNumberOfTweets(70);
		// groupAndReduceProfiles();
		// JsonUtil.writeJsonOutput(profiles, "updated.json");

		removeMentions();
		removeUrls();
		removeHashtags();
		convertHtmlEntities();
		removeNewlines();
		removeUnicodeSymbols();
		removeEmoticons();
		removePunctuation();
		removeDoubleSpaces();
		removeStopwords();

		createUserTweetDocument();
		ArffUtil.writeArffOutput(profileTweetsList, "test.arff");
	}


	private void setNumberOfTweets(int number) {
		System.out.println("Setting number of tweets...");
		Random random = new Random();
		for (Profile profile : profiles.values()) {
			List<Tweet> tweetList = profile.getTweetList();
			while (tweetList.size() > number) {
				tweetList.remove(random.nextInt(tweetList.size()));
			}
		}
	}


	private void groupAndReduceProfiles() {
		Map<String, List<Profile>> groupedByProvince = profiles.values().stream()
				.collect(Collectors.groupingBy(p -> p.getPredictedLocation().getProvince()));
		Map<String, Profile> updatedMap = new HashMap<>();
		Random random = new Random();

		for (List<Profile> profileList : groupedByProvince.values()) {
			while (profileList.size() > 358) {
				profileList.remove(random.nextInt(profileList.size()));
			}
			for (Profile profile : profileList) {
				updatedMap.put(profile.getId(), profile);
			}
		}
		profiles = updatedMap;
	}


	private void removeMentions() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				Extractor extractor = new Extractor();
				List<String> list = extractor.extractMentionedScreennames(tweetText);
				for (String mention : list) {
					tweetText = tweetText.replace("@" + mention, "");
					tweet.setText(tweetText);
				}
			}
		}
	}


	private void removeHashtags() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				Extractor extractor = new Extractor();
				List<String> list = extractor.extractHashtags(tweetText);
				for (String hashtag : list) {
					tweetText = tweetText.replace("#" + hashtag, "");
					tweet.setText(tweetText);
				}
			}
		}
	}


	private void removeUrls() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				Extractor extractor = new Extractor();
				List<String> list = extractor.extractURLs(tweetText);
				for (String url : list) {
					tweetText = tweetText.replace(url, "");
					tweet.setText(tweetText);
				}
			}
		}
	}


	private void convertHtmlEntities() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				tweet.setText(HtmlEscape.unescapeHtml(tweetText));
			}
		}
	}


	private void removeNewlines() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				tweet.setText(tweetText.replaceAll("\\r|\\n", ""));
			}
		}
	}


	private void removeDoubleSpaces() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				tweet.setText(tweetText.trim().replaceAll(" +", " "));
			}
		}
	}


	private void removeUnicodeSymbols() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				tweet.setText(tweetText.replaceAll("\\p{So}+", ""));
			}
		}
	}


	private void removeEmoticons() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				String[] emoticons = {":)", ":(", ":P", "<3", ":-)", ":-(", ":-P"};
				for (String emoticon : emoticons) {
					tweetText = tweetText.replace(emoticon, "");
					tweet.setText(tweetText);
				}
			}
		}
	}


	private void removePunctuation() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				tweet.setText(tweetText.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}^\\s]", " "));
			}
		}
	}


	private void removeStopwords() {
		for (Profile profile : profiles.values()) {
			for (Tweet tweet : profile.getTweetList()) {
				String tweetText = tweet.getText();
				String[] splitted = tweetText.split("\\s+");
				List<String> allowedWords = new ArrayList<>();
				for (String word : splitted) {
					if (!Config.getInstance().getStopwords().contains(word.trim().toLowerCase())) {
						allowedWords.add(word);
					}
				}
				tweet.setText(String.join(" ", allowedWords));
			}
		}
	}


	private void createUserTweetDocument() {
		profileTweetsList = new ArrayList<>();
		for (Profile profile : profiles.values()) {
			ProfileTweets profileTweets = new ProfileTweets();
			profileTweets.setId(profile.getId());
			profileTweets.setProvince(profile.getPredictedLocation().getProvince());
			profileTweets.setTweetText(profile.getTweetList().stream().map(Tweet::toString)
					.collect(Collectors.joining(" ")).toLowerCase());
			profileTweetsList.add(profileTweets);
		}
	}
}
