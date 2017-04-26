package datasettools;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datasettools.data.PredictedLocation;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class Config {
	private static Config config;

	private int minimumTweets = 70;

	private Set<String> firstNamesSet;

	private Set<PredictedLocation> predictedLocationSet;

	private Set<String> stopwords;


	public static Config getInstance() {
		if (config == null) {
			config = new Config();
			config.buildFirstNamesSet();
			config.buildPredictedLocationSet();
			config.buildStopwordList();
		}
		return config;
	}


	Set<String> getFirstNamesSet() {
		return firstNamesSet;
	}


	public Set<PredictedLocation> getPredictedLocationSet() {
		return predictedLocationSet;
	}


	public Set<String> getStopwords() { return stopwords; }


	public void buildFirstNamesSet() {
		try (InputStream in = this.getClass().getResourceAsStream("/firstnames.txt")) {
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


	public void buildPredictedLocationSet() {
		try (InputStream in = this.getClass().getResourceAsStream("/places.json")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Type type = new TypeToken<HashSet<PredictedLocation>>() {
			}.getType();
			this.predictedLocationSet = new Gson().fromJson(br, type);
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with places.", e);
		}
	}


	public void buildStopwordList() {
		try (InputStream in = this.getClass().getResourceAsStream("/stopwords.txt")) {
			BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
			Set<String> stopwords = new HashSet<>();
			String line;
			while ((line = br.readLine()) != null) {
				stopwords.add(line);
			}
			this.stopwords = stopwords;
		} catch (IOException e) {
			throw new RuntimeException("Could not load file with stopwords.", e);
		}
	}


	int getMinimumTweets() {
		return minimumTweets;
	}


	public void setMinimumTweets(int minimumTweets) {
		this.minimumTweets = minimumTweets;
	}
}
