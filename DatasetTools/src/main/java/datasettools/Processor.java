package datasettools;

import datasettools.data.Profile;

import java.util.Map;


public interface Processor {
	void doProcessing();

	Map<String, Profile> getProfiles();
}
