package tweetprocessor.data;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;


public class Tweet {
	private long id;

	private String text;

	private GeoLocation geoLocation;

	private List<String> hashtagList;


	public Tweet(long id, String text, GeoLocation geoLocation, List<String> hashtagList) {
		this.id = id;
		this.text = text;
		this.geoLocation = geoLocation;
		this.hashtagList = hashtagList;
	}


	public Tweet(JsonObject fullTweet) {
		this.id = fullTweet.get("id").getAsLong();
		this.text = fullTweet.get("text").getAsString();
		this.geoLocation = createGeoLocation(fullTweet);
		this.hashtagList = createHashtags(fullTweet);
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public GeoLocation getGeoLocation() {
		return geoLocation;
	}


	public void setGeoLocation(GeoLocation geoLocation) {
		this.geoLocation = geoLocation;
	}


	public List<String> getHashtagList() {
		return hashtagList;
	}


	public void setHashtagList(List<String> hashtagList) {
		this.hashtagList = hashtagList;
	}


	private static GeoLocation createGeoLocation(JsonObject fullTweet) {
		if (!fullTweet.get("geo").isJsonNull()) {
			JsonObject geo = fullTweet.get("geo").getAsJsonObject();
			JsonArray coordinates = geo.get("coordinates").getAsJsonArray();
			return new GeoLocation(coordinates.get(0).getAsDouble(), coordinates.get(1).getAsDouble());
		}
		return null;
	}


	private static List<String> createHashtags(JsonObject fullTweet) {
		JsonObject entities = fullTweet.getAsJsonObject("entities");
		JsonArray hashtagEntities = entities.getAsJsonArray("hashtags");

		if (hashtagEntities.size() == 0) {
			return null;
		}

		List<String> hashtagList = new ArrayList<>();
		for (JsonElement hashtag : hashtagEntities) {
			hashtagList.add(hashtag.getAsJsonObject().get("text").getAsString());
		}
		return hashtagList;
	}
}
