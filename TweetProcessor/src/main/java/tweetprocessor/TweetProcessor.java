package tweetprocessor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

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

		Map<Profile, List<Tweet>> profilesAndTweets = new HashMap<>();

		for (File inputFile : fileList) {
			System.out.println("Processing file " + inputFile.getName() + "...");
			Processor fileProcessor = new FileProcessor(new HashMap<>(), inputFile);
			fileProcessor.getProfilesAndTweets().forEach((k, v) -> profilesAndTweets.merge(k, v, (s1, s2) -> {
				List<Tweet> tweetList = new ArrayList<>(s1);
				tweetList.addAll(s2);
				return tweetList;
			}));
		}
		Processor postProcessor = new PostProcessor(profilesAndTweets);
		writeJsonOutput(postProcessor.getProfilesAndTweets());
	}


	private static void writeJsonOutput(Map<Profile, List<Tweet>> profilesAndTweets) {
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
