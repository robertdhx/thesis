import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.*;
import twitter4j.TwitterException;

import java.io.*;
import java.util.*;


public class TweetProcessor {
	public static void main(String[] args) {
		if (args.length == 0) {
			throw new RuntimeException("Argument input files are missing.");
		}

		Config config = Config.getInstance();
		config.buildFirstNamesHashSet();
		config.buildPredictedLocationList();

		List<File> fileList = new ArrayList<>();
		for (String arg : args) {
			fileList.add(new File(arg));
		}

		Multimap<Profile, Tweet> profilesAndTweets = HashMultimap.create();

		for (File inputFile : fileList) {
			System.out.println("Processing file " + inputFile.getName() + "...");
			try {
				FileProcessor fileProcessor = new FileProcessor(inputFile);
				profilesAndTweets.putAll(fileProcessor.getProfilesAndTweets());
			} catch (IOException | TwitterException e) {
				System.err.println("Error during processing of '" + inputFile + ". Message: " + e.getMessage());
			}
		}

		PostProcessor postProcessor = new PostProcessor(profilesAndTweets);
		writeJsonOutput(postProcessor.getProfilesAndTweets());
	}


	private static void writeJsonOutput(Multimap<Profile, Tweet> profilesAndTweets) {
		File file = new File("output.json");
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
			gson.toJson(profilesAndTweets.asMap(), writer);
			System.out.println("JSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}
}
