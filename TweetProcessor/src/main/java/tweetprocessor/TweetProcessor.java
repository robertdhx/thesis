package tweetprocessor;

import org.apache.commons.cli.*;
import tweetprocessor.data.Profile;
import tweetprocessor.data.Tweet;
import tweetprocessor.util.JsonUtil;

import java.io.File;
import java.util.*;


public class TweetProcessor {
	private static final int SCREEN_WIDTH_COLUMNS = 79;


	public static void main(String[] args) {
		Options options = setCliOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("tweets")) {
				String[] arguments = cmd.getOptionValues("tweets");
				processTwitterData(arguments);
			}
			if (cmd.hasOption("dataset")) {
				processDataset();
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			showHelp(options);
		}
	}


	private static Options setCliOptions() {
		Options options = new Options();
		Option tweets = Option.builder("tweets")
				.desc("process tweets")
				.hasArgs()
				.valueSeparator(' ')
				.argName("files")
				.build();
		options.addOption(tweets);
		options.addOption("dataset", "process dataset of profiles and tweets");
		options.addOption("help", "print this message");
		return options;
	}


	private static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(SCREEN_WIDTH_COLUMNS);
		formatter.printHelp("TweetProcessor [option] [argument]", options);
	}


	private static void processTwitterData(String[] arguments) {
		Config config = Config.getInstance();
		config.buildFirstNamesSet();
		config.buildPredictedLocationSet();

		List<File> fileList = new ArrayList<>();

		for (String argument : arguments) {
			fileList.add(new File(argument));
		}

		for (File inputFile : fileList) {
			Processor fileProcessor = new FileProcessor(new HashMap<>(), inputFile);
			JsonUtil.writeJsonOutput(fileProcessor.getProfilesAndTweets(), inputFile.getName());
		}

		Map<Profile, List<Tweet>> profilesAndTweets = new HashMap<>();
		System.out.println("Merging results...");

		for (File inputFile : fileList) {
			File outputFile = new File("output_" + inputFile.getName());
			JsonUtil.readJsonOutput(outputFile).forEach((k, v) -> profilesAndTweets.merge(k, v, (s1, s2) -> {
				List<Tweet> tweetList = new ArrayList<>(s1);
				tweetList.addAll(s2);
				return tweetList;
			}));
		}

		Processor postProcessor = new PostProcessor(profilesAndTweets);
		JsonUtil.writeJsonOutput(postProcessor.getProfilesAndTweets(), "merged.json");
	}


	private static void processDataset() {
		Map<Profile, List<Tweet>> profilesAndTweets = new HashMap<>();
		File datasetFile = new File("20170421-output-70.json");
		Processor datasetProcessor = new DatasetProcessor(datasetFile);
	}
}
