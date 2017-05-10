package datasettools.util;

import datasettools.data.ProfileTweets;

import java.io.*;
import java.util.List;


public class ArffUtil {
	public static void writeArffOutput(List<ProfileTweets> profileTweetsList, String filename) {
		File file = new File(filename);
		try (Writer wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"))) {
			wr.write("@relation profile\n\n");
			wr.write("@attribute tweettext string\n");
			wr.write("@attribute class {NB,FL,OV,NH,ZE,GR,FR,LI,DR,GE,ZH,UT}\n\n");
			wr.write("@data\n");
			for (ProfileTweets profileTweets : profileTweetsList) {
				wr.write("\"" + profileTweets.getTweetText() + "\"," + profileTweets.getProvince() + "\n");
			}
			System.out.println("ARFF file saved in: " + file.getAbsolutePath());
		} catch (IOException e) {
			System.err.println("Error writing output file '" + file.getName() + "'. Message: " + e.getMessage());
		}
	}
}
