import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import data.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import twitter4j.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class TweetProcessor {
	private static final int MINIMUM_TWEETS = 70;

	private static final Set<String> FIRST_NAMES = buildFirstNamesHashSet();

	private static final List<PredictedLocation> PREDICTED_LOCATION_LIST = buildPredictedLocationList();


	public static void main(String[] args) {
		// Check argument
		if (args.length == 0) {
			throw new RuntimeException("Argument input files are missing.");
		}

		List<File> fileList = new ArrayList<>();

		for (String arg : args) {
			fileList.add(new File(arg));
		}

		Multimap<Profile, Tweet> profileTweetMap = HashMultimap.create();
		int lineCounter = 0;
		System.out.println("Processing and applying initial rule set (no retweets, only real persons with locations)...");

		for (File inputFile : fileList) {
			try {
				LineIterator lineIterator = FileUtils.lineIterator(inputFile, "UTF-8");
				try {
					while (lineIterator.hasNext()) {
						lineCounter++;
						if (lineCounter % 100000 == 0) {
							System.out.println("Processed " + lineCounter + " lines.");
						}
						String line = lineIterator.nextLine();
						Status status = TwitterObjectFactory.createStatus(line);
						if (statusFilter(status)) {
							Profile profile = convertUserToProfile(status.getUser());
							Tweet tweet = convertStatusToTweet(status);
							profileTweetMap.put(profile, tweet);
						}
					}
				} finally {
					LineIterator.closeQuietly(lineIterator);
				}
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
		}

		System.out.println("\nUnique profiles: " + profileTweetMap.keySet().size());
		System.out.println("Unique tweets: " + profileTweetMap.size());

		System.out.println("\nApplying rule: ignore profiles with fewer than " + MINIMUM_TWEETS + " tweets...");
		profileTweetMap.asMap().entrySet().removeIf(e -> (e.getValue().size() < MINIMUM_TWEETS));

		System.out.println("\nUnique profiles: " + profileTweetMap.keySet().size());
		System.out.println("Unique tweets: " + profileTweetMap.size());

		System.out.println("\nApplying rule: ignore profiles without predicted location...");

		profileTweetMap.asMap().forEach((k, v) -> k.setPredictedLocation(guessLocation(k.getLocation())));
		profileTweetMap.asMap().entrySet().removeIf(e -> (e.getKey().getPredictedLocation() == null));

		System.out.println("\nUnique profiles: " + profileTweetMap.keySet().size());
		System.out.println("Unique tweets: " + profileTweetMap.size());

		writeJsonOutput(profileTweetMap);
	}


	private static Tweet convertStatusToTweet(Status status) {
		return new Tweet(
				status.getId(),
				status.getText(),
				status.getCreatedAt(),
				status.getGeoLocation(),
				status.getPlace(),
				status.getHashtagEntities()
		);
	}


	private static Profile convertUserToProfile(User user) {
		return new Profile(
				user.getId(),
				user.getScreenName(),
				user.getName(),
				user.getDescription(),
				user.getLocation()
		);
	}


	private static boolean statusFilter(Status status) {
		// Remove retweets
		if (status.isRetweet()) {
			return false;
		}
		User user = status.getUser();
		// Filter users that do not have a location
		if (user.getLocation() == null) {
			return false;
		}
		// Filter first names that do not occur in the HashSet FIRST_NAMES
		String firstName = getFirstName(user.getName());
		if (!FIRST_NAMES.contains(firstName.toLowerCase())) {
			return false;
		}
		// If all filter conditions are satisfied, return true
		return true;
	}


	private static Set<String> buildFirstNamesHashSet() {
		try (InputStream in = TweetProcessor.class.getResourceAsStream("firstnames.txt")) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Set<String> firstNames = new HashSet<>();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				firstNames.add(line);
			}
			return firstNames;
		} catch (Exception e) {
			throw new RuntimeException("Could not load file with first names.");
		}
	}


	private static List<PredictedLocation> buildPredictedLocationList() {
		try (InputStream in = TweetProcessor.class.getResourceAsStream("places.json")) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Type listType = new TypeToken<List<PredictedLocation>>() {
			}.getType();
			return new Gson().fromJson(bufferedReader, listType);
		} catch (Exception e) {
			throw new RuntimeException("Could not load file with places.");
		}
	}


	private static String getFirstName(String name) {
		if (name.indexOf(' ') > -1) {
			return name.substring(0, name.indexOf(' '));
		}
		return name;
	}


	private static String getFirstPartOfLocation(String location) {
		if (location.indexOf(',') > -1) {
			return location.substring(0, location.indexOf(','));
		}
		if (location.indexOf('/') > -1) {
			return location.substring(0, location.indexOf('/'));
		}
		return location;
	}


	public static PredictedLocation guessLocation(String location) {
		String firstPartOfLocation = getFirstPartOfLocation(location);
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
		String firstPartOfLocation = getFirstPartOfLocation(location);

		for (PredictedLocation predictedLocation : PREDICTED_LOCATION_LIST) {
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


	private static void writeJsonOutput(Multimap<Profile, Tweet> profileTweetMap) {
		File file = new File("output.json");
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
			gson.toJson(profileTweetMap.asMap(), writer);
			System.out.println("\nJSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.out.println("Error writing output file: " + e.getMessage());
		}
	}
}
