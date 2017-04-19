import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.PredictedLocation;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


class Config {
	private static Config instance;

	private int minimumTweets = 75;

	private Set<String> firstNamesSet;

	private Set<PredictedLocation> predictedLocationSet;


	static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}


	Set<String> getFirstNamesSet() {
		return firstNamesSet;
	}


	Set<PredictedLocation> getPredictedLocationSet() {
		return predictedLocationSet;
	}


	void buildFirstNamesSet() {
		try (InputStream in = this.getClass().getResourceAsStream("firstnames.txt")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Set<String> firstNames = new HashSet<>();
			String line;
			while ((line = br.readLine()) != null) {
				firstNames.add(line);
			}
			this.firstNamesSet = firstNames;
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with first names.", e);
		}
	}


	void buildPredictedLocationSet() {
		try (InputStream in = this.getClass().getResourceAsStream("places.json")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Type type = new TypeToken<HashSet<PredictedLocation>>() {
			}.getType();
			this.predictedLocationSet = new Gson().fromJson(br, type);
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with places.", e);
		}
	}


	int getMinimumTweets() {
		return minimumTweets;
	}


	void setMinimumTweets(int minimumTweets) {
		this.minimumTweets = minimumTweets;
	}
}
