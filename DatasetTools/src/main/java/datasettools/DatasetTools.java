package datasettools;

import org.apache.commons.cli.*;
import datasettools.data.Profile;
import datasettools.util.JsonUtil;

import java.io.File;
import java.util.*;


public class DatasetTools {
	private static final int SCREEN_WIDTH_COLUMNS = 79;


	public static void main(String[] args) {
		Options options = setCliOptions();
		CommandLineParser parser = new DefaultParser();

		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption("dataset")) {
				String[] arguments = cmd.getOptionValues("dataset");
				createDataset(arguments);
			}
			if (cmd.hasOption("arff")) {
				String argument = cmd.getOptionValue("arff");
				createArffFiles(argument);
			}
		} catch (ParseException e) {
			System.out.println(e.getMessage());
			showHelp(options);
		}
	}


	private static Options setCliOptions() {
		Options options = new Options();

		Option profiles = Option.builder("dataset")
				.desc("create dataset from raw Twitter data")
				.hasArgs()
				.valueSeparator(' ')
				.argName("files")
				.build();

		Option dataset = Option.builder("arff")
				.desc("create arff files from dataset")
				.numberOfArgs(1)
				.argName("file")
				.build();

		options.addOption(profiles);
		options.addOption(dataset);
		options.addOption("help", "print this message");
		return options;
	}


	private static void showHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.setWidth(SCREEN_WIDTH_COLUMNS);
		formatter.printHelp("DatasetTools [option] [argument]", options);
	}


	private static void createDataset(String[] arguments) {
		List<File> fileList = new ArrayList<>();

		for (String argument : arguments) {
			fileList.add(new File(argument));
		}

		for (File inputFile : fileList) {
			Processor fileProcessor = new FileProcessor(new HashMap<>(), inputFile);
			JsonUtil.writeJsonOutput(fileProcessor.getProfiles(), inputFile.getName());
		}

		Map<String, Profile> profiles = new HashMap<>();
		System.out.println("Merging results...");

		for (File inputFile : fileList) {
			File outputFile = new File("output_" + inputFile.getName());
			JsonUtil.readJsonOutput(outputFile).forEach((k, v) -> profiles.merge(k, v, (s1, s2) -> {
				s1.getTweetList().addAll(s2.getTweetList());
				return s1;
			}));
		}

		Processor postProcessor = new PostProcessor(profiles);
		JsonUtil.writeJsonOutput(postProcessor.getProfiles(), "merged.json");
	}


	private static void createArffFiles(String argument) {
		File inputFile = new File(argument);
		Processor profileProcessor = new ProfileProcessor(inputFile);
	}
}
