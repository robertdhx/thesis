package tweetprocessor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;


public class JsonUtil {
	public static void writeJsonOutput(Map<Profile, List<Tweet>> profilesAndTweets, String filename) {
		File file = new File("output_" + filename);
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
			gson.toJson(profilesAndTweets, writer);
			System.out.println("JSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}


	public static Map<Profile, List<Tweet>> readJsonOutput(File file) {
		Type typeOf = new TypeToken<HashMap<Profile, List<Tweet>>>() {}.getType();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try (Reader reader = new FileReader(file)) {
			return gson.fromJson(reader, typeOf);
		} catch (IOException e) {
			System.err.println("Could not read JSON file '" + file.getName() + "'. Message: " + e.getMessage());
		}
		return null;
	}
}
