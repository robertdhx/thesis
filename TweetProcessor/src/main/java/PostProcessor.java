import com.google.common.collect.Multimap;
import data.*;
import org.apache.commons.lang3.StringUtils;
import util.StringUtil;

import java.util.*;


class PostProcessor {
	private Multimap<Profile, Tweet> profilesAndTweets;


	PostProcessor(Multimap<Profile, Tweet> profilesAndTweets) {
		this.profilesAndTweets = profilesAndTweets;
		doPostProcessing();
	}


	Multimap<Profile, Tweet> getProfilesAndTweets() {
		return profilesAndTweets;
	}


	private void doPostProcessing() {
		int minimumTweets = Config.getInstance().getMinimumTweets();
		System.out.println("Removing profiles with fewer than " + minimumTweets + " tweets...");
		profilesAndTweets.asMap().entrySet().removeIf(e -> (e.getValue().size() < minimumTweets));

		System.out.println("Attempting to set predicted location for each profile...");
		profilesAndTweets.asMap().forEach((k, v) -> k.setPredictedLocation(guessLocation(k.getLocation())));

		System.out.println("Performing final clean-up...");
		profilesAndTweets.asMap().keySet().removeIf(p -> p.getPredictedLocation() == null);
		profilesAndTweets.asMap().keySet().removeIf(ProfilePredicates.hasBelgianLocation());
		profilesAndTweets.asMap().keySet().removeIf(ProfilePredicates.hasEdgeCaseLocation());

		System.out.println("Complete!");
	}


	public static PredictedLocation guessLocation(String location) {
		String firstPartOfLocation = StringUtil.getFirstPartOfLocation(location);
		List<PredictedLocation> matchingLocations = getMatchingLocations(firstPartOfLocation);

		if (!matchingLocations.isEmpty()) {
			Map<PredictedLocation, Integer> results = calculateBestMatch(matchingLocations, firstPartOfLocation);
			Comparator<? super Map.Entry<PredictedLocation, Integer>> valueComparator = (entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue());
			Map.Entry<PredictedLocation, Integer> lowestMapEntry = results.entrySet()
					.stream().min(valueComparator).get();
			return lowestMapEntry.getKey();
		}
		return null;
	}


	private static List<PredictedLocation> getMatchingLocations(String firstPartOfLocation) {
		List<PredictedLocation> matchingLocations = new ArrayList<>();

		for (PredictedLocation predictedLocation : Config.getInstance().getPredictedLocationSet()) {
			String normalizedPlace = predictedLocation.getPlace().toLowerCase();
			String normalizedLocation = firstPartOfLocation.toLowerCase();
			if (normalizedPlace.contains(normalizedLocation)) {
				matchingLocations.add(predictedLocation);
			}
		}
		return matchingLocations;
	}


	private static Map<PredictedLocation, Integer> calculateBestMatch(List<PredictedLocation> matchingLocations, String firstPartOfLocation) {
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
}
