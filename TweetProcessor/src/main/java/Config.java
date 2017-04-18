import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import data.PredictedLocation;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class Config {
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


	public Set<String> getNameSet() {
		return nameSet;
	}


	public List<PredictedLocation> getPredictedLocationList() {
		return predictedLocationList;
	}


	void buildFirstNamesHashSet() {
		try (InputStream in = this.getClass().getResourceAsStream("firstnames.txt")) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Set<String> firstNames = new HashSet<>();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				firstNames.add(line);
			}
			this.nameSet = firstNames;
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with first names.", e);
		}
	}


	void buildPredictedLocationList() {
		try (InputStream in = this.getClass().getResourceAsStream("places.json")) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Type listType = new TypeToken<List<PredictedLocation>>() {
			}.getType();
			this.predictedLocationList = new Gson().fromJson(bufferedReader, listType);
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with places.", e);
		}
	}


	int getMinimumTweets() {
		return minimumTweets;
	}
}
