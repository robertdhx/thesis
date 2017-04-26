package datasettools.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import datasettools.data.Profile;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;


public class JsonUtil {
	public static void writeJsonOutput(Map<String, Profile> profiles, String filename) {
		File file = new File("output_" + filename);
		try (Writer writer = new FileWriter(file)) {
			Gson gson = new GsonBuilder().create();
			gson.toJson(profiles, writer);
			System.out.println("JSON file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}


	public static Map<String, Profile> readJsonOutput(File file) {
		Type typeOf = new TypeToken<HashMap<String, Profile>>() {}.getType();
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
