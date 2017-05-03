package datasettools.data;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;


public class Profile {
	private String id;

	private String username;

	private String name;

	private String description;

	private String location;

	private PredictedLocation predictedLocation;

	private List<Tweet> tweetList;


	public Profile(String id, String username, String name, String description, String location, List<Tweet> tweetList) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.description = description;
		this.location = location;
		this.tweetList = tweetList;
	}


	public Profile(JsonObject user) {
		this.id = user.get("id").getAsString();
		this.username = user.get("screen_name").getAsString();
		this.name = user.get("name").getAsString();
		if (!user.get("description").isJsonNull()) {
			this.description = user.get("description").getAsString();
		}
		this.location = user.get("location").getAsString();
		this.tweetList = new ArrayList<>();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public PredictedLocation getPredictedLocation() {
		return predictedLocation;
	}


	public void setPredictedLocation(PredictedLocation predictedLocation) {
		this.predictedLocation = predictedLocation;
	}


	public List<Tweet> getTweetList() {
		return tweetList;
	}


	public void setTweetList(List<Tweet> tweetList) {
		this.tweetList = tweetList;
	}
}
