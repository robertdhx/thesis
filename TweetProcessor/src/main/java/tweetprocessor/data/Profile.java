package tweetprocessor.data;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.List;


public class Profile {
	private long id;

	private String username;

	private String name;

	private String location;

	private PredictedLocation predictedLocation;

	private List<Tweet> tweetList;


	public Profile(long id, String username, String name, String location) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.location = location;
	}


	public Profile(JsonObject user) {
		this.id = user.get("id").getAsLong();
		this.username = user.get("screen_name").getAsString();
		this.name = user.get("name").getAsString();
		this.location = user.get("location").getAsString();
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
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


	@Override public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(id)
				.append(username)
				.toHashCode();
	}


	@Override public boolean equals(Object o) {
		if (o == this) { return true; }
		if (!(o instanceof Profile)) {
			return false;
		}

		Profile profile = (Profile) o;

		return new EqualsBuilder()
				.append(id, profile.id)
				.append(username, profile.username)
				.isEquals();
	}
}
