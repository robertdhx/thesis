package datasettools.util;

import datasettools.data.ArffInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class ArffUtil {
	public static void prepareArffOutput(List<ArffInstance> arffInstanceList) {
		int listSize = arffInstanceList.size();
		int trainingSize = (int) Math.round(listSize * 0.8);

		// Generate ARFF with descriptions
		List<String> dataTypes = new ArrayList<>();
		dataTypes.add("description");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with tweet texts
		dataTypes.clear();
		dataTypes.add("tweetText");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with hashtags
		dataTypes.clear();
		dataTypes.add("hashtags");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with descriptions, tweet texts
		dataTypes.clear();
		dataTypes.add("description");
		dataTypes.add("tweetText");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with descriptions, hashtags
		dataTypes.clear();
		dataTypes.add("description");
		dataTypes.add("hashtags");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with tweet texts, hashtags
		dataTypes.clear();
		dataTypes.add("tweetText");
		dataTypes.add("hashtags");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");

		// Generate ARFF with descriptions, tweet texts, hashtags
		dataTypes.clear();
		dataTypes.add("description");
		dataTypes.add("tweetText");
		dataTypes.add("hashtags");
		writeArffOutput(arffInstanceList.subList(0, trainingSize), dataTypes, "training");
		writeArffOutput(arffInstanceList.subList(trainingSize, listSize), dataTypes, "test");
	}


	private static void writeArffOutput(List<ArffInstance> arffInstanceList, List<String> dataTypes, String set) {
		File file = new File(String.join("_", dataTypes) + "_" + set + ".arff");
		try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"))) {
			wr.write("@relation profile\n\n");
			if (dataTypes.contains("description")) {
				wr.write("@attribute description string\n");
			}
			if (dataTypes.contains("tweetText")) {
				wr.write("@attribute tweetText string\n");
			}
			if (dataTypes.contains("hashtags")) {
				wr.write("@attribute hashtags string\n");
			}
			wr.write("@attribute class {NB,FL,OV,NH,ZE,GR,FR,LI,DR,GE,ZH,UT}\n\n");
			wr.write("@data\n");
			for (ArffInstance arffInstance : arffInstanceList) {
				if (dataTypes.contains("description")) {
					wr.write(convert(arffInstance.getDescription()));
				}
				if (dataTypes.contains("tweetText")) {
					wr.write(convert(arffInstance.getTweetText()));
				}
				if (dataTypes.contains("hashtags")) {
					wr.write(convert(arffInstance.getHashtags()));
				}
				wr.write(arffInstance.getProvince() + "\n");
			}
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}


	private static String convert(String data) {
		return data == null || data.isEmpty() ? "?," : "\"" + data + "\",";
	}
}
