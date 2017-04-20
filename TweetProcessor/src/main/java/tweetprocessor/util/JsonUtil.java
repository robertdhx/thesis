package tweetprocessor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;

import java.io.*;
import java.util.List;
import java.util.Map;


public class JsonUtil {
	public static void writeJsonOutput(Map<Profile, List<Tweet>> profilesAndTweets) {
		File file = new File("output.json");
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
			gson.toJson(profilesAndTweets, writer);
			System.out.println("JSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}
}
