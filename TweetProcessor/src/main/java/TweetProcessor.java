import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.Profile;
import data.Tweet;
import twitter4j.TwitterException;

import java.io.*;
import java.util.*;


public class TweetProcessor {
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new RuntimeException("Argument input files are missing.");
		}

		Config config = Config.getInstance();
		config.buildFirstNamesSet();
		config.buildPredictedLocationSet();

		List<File> fileList = new ArrayList<>();
		for (String arg : args) {
			fileList.add(new File(arg));
		}

		Map<Profile, Set<Tweet>> profilesAndTweets = new HashMap<>();

		for (File inputFile : fileList) {
			System.out.println("Processing file " + inputFile.getName() + "...");
			try {
				FileProcessor fileProcessor = new FileProcessor(inputFile);
				fileProcessor.getProfilesAndTweets().forEach((k, v) -> profilesAndTweets.merge(k, v, (s1, s2) -> {
					Set<Tweet> tweetSet = new HashSet<>(s1);
					tweetSet.addAll(s2);
					return tweetSet;
				}));
			} catch (IOException | TwitterException e) {
				System.err.println("Error during processing of '" + inputFile + ". Message: " + e.getMessage());
			}
		}

		PostProcessor postProcessor = new PostProcessor(profilesAndTweets);
		writeJsonOutput(postProcessor.getProfilesAndTweets());
	}


	private static void writeJsonOutput(Map<Profile, Set<Tweet>> profilesAndTweets) {
		File file = new File("output.json");
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
			gson.toJson(profilesAndTweets, writer);
			System.out.println("JSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}
}
