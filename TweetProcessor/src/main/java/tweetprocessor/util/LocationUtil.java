package tweetprocessor.util;

import org.apache.commons.lang3.StringUtils;
import tweetprocessor.Config;
import tweetprocessor.data.PredictedLocation;

import java.util.*;


public class LocationUtil {
	public static PredictedLocation guessLocation(String location) {
		String firstPartOfLocation = StringUtil.getFirstPartOfLocation(location);
		if (firstPartOfLocation.length() <= 2) {
			return null;
		}
		List<PredictedLocation> matchingLocations = getMatchingLocations(firstPartOfLocation);

		if (!matchingLocations.isEmpty()) {
			Map<PredictedLocation, Integer> results = calculateBestMatch(matchingLocations, firstPartOfLocation);
			Comparator<? super Map.Entry<PredictedLocation, Integer>> valueComparator = Comparator.comparing(Map.Entry::getValue);
			Map.Entry<PredictedLocation, Integer> lowestMapEntry = results.entrySet()
					.stream().min(valueComparator).get();
			if (lowestMapEntry.getValue() < 5) {
				return lowestMapEntry.getKey();
			}
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
