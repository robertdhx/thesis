import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import data.*;
import org.apache.commons.lang3.StringUtils;
import twitter4j.TwitterException;
import util.StringUtil;

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
			try {
				FileProcessor fileProcessor = new FileProcessor(inputFile);
				profilesAndTweets.putAll(fileProcessor.getProfilesAndTweets());
			} catch (IOException | TwitterException e) {
				System.out.println("Error during processing of '" + inputFile + ". Message: " + e.getMessage());
			}
		}

		System.out.println("\nUnique profiles: " + profilesAndTweets.keySet().size());
		System.out.println("Unique tweets: " + profilesAndTweets.size());

		System.out.println("\nApplying rule: ignore profiles with fewer than " + config.getMinimumTweets() + " tweets...");
		profilesAndTweets.asMap().entrySet().removeIf(e -> (e.getValue().size() < config.getMinimumTweets()));

		System.out.println("\nUnique profiles: " + profilesAndTweets.keySet().size());
		System.out.println("Unique tweets: " + profilesAndTweets.size());

		System.out.println("\nApplying rule: ignore profiles without predicted location...");

		profilesAndTweets.asMap().forEach((k, v) -> k.setPredictedLocation(guessLocation(k.getLocation())));
		profilesAndTweets.asMap().entrySet().removeIf(e -> (e.getKey().getPredictedLocation() == null));

		System.out.println("\nUnique profiles: " + profilesAndTweets.keySet().size());
		System.out.println("Unique tweets: " + profilesAndTweets.size());

		writeJsonOutput(profilesAndTweets.asMap());
	}


	static PredictedLocation guessLocation(String location) {
		String firstPartOfLocation = StringUtil.getFirstPartOfLocation(location);
		List<PredictedLocation> matchingLocations = getMatchingLocations(firstPartOfLocation);
		if (!matchingLocations.isEmpty()) {
			Map<PredictedLocation, Integer> results = calculateLevenshteinDistance(matchingLocations, firstPartOfLocation);
			Comparator<? super Map.Entry<PredictedLocation, Integer>> valueComparator = (entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue());
			Map.Entry<PredictedLocation, Integer> lowestMapEntry = results.entrySet()
					.stream().min(valueComparator).get();
			return lowestMapEntry.getKey();
		}
		return null;
	}


	private static List<PredictedLocation> getMatchingLocations(String location) {
		List<PredictedLocation> matchingLocations = new ArrayList<>();
		String firstPartOfLocation = StringUtil.getFirstPartOfLocation(location);

		for (PredictedLocation predictedLocation : Config.getInstance().getPredictedLocationList()) {
			String normalizedPlace = predictedLocation.getPlace().toLowerCase();
			String normalizedLocation = firstPartOfLocation.toLowerCase();
			if (normalizedPlace.contains(normalizedLocation)) {
				matchingLocations.add(predictedLocation);
			}
		}
		return matchingLocations;
	}


	private static Map<PredictedLocation, Integer> calculateLevenshteinDistance(List<PredictedLocation> matchingLocations, String firstPartOfLocation) {
		if (!matchingLocations.isEmpty()) {
			Map<PredictedLocation, Integer> results = new HashMap<>();
			for (PredictedLocation predictedLocation : matchingLocations) {
				int levenshteinDistance = StringUtils.getLevenshteinDistance(predictedLocation.getPlace(), firstPartOfLocation);
				results.put(predictedLocation, levenshteinDistance);
			}
			return results;
		}
		return Collections.emptyMap();
	}


	private static void writeJsonOutput(Map<Profile, Collection<Tweet>> profileTweetMap) {
		File file = new File("output.json");
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
			gson.toJson(profileTweetMap, writer);
			System.out.println("\nJSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Error writing output file: " + e.getMessage());
		}
	}
}
