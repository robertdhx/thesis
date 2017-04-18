import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.PredictedLocation;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


class Config {
	private static Config instance;

	private int minimumTweets = 75;

	private Set<String> nameSet;

	private List<PredictedLocation> predictedLocationList;


	static Config getInstance() {
		if (instance == null) {
			instance = new Config();
		}
		return instance;
	}


	Set<String> getNameSet() {
		return nameSet;
	}


	List<PredictedLocation> getPredictedLocationList() {
		return predictedLocationList;
	}


	void buildFirstNamesHashSet() {
		try (InputStream in = this.getClass().getResourceAsStream("firstnames.txt")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Set<String> firstNames = new HashSet<>();
			String line;
			while ((line = br.readLine()) != null) {
				firstNames.add(line);
			}
			this.nameSet = firstNames;
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with first names.", e);
		}
	}


	void buildPredictedLocationList() {
		try (InputStream in = this.getClass().getResourceAsStream("places.json")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Type type = new TypeToken<List<PredictedLocation>>() {
			}.getType();
			this.predictedLocationList = new Gson().fromJson(br, type);
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with places.", e);
		}
	}


	int getMinimumTweets() {
		return minimumTweets;
	}
}
