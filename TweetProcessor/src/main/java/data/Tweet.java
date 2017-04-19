package data;

import com.google.gson.*;

import java.util.ArrayList;
import java.util.List;


public class Tweet {
	private long id;

	private String text;

	private GeoLocation geoLocation;

	private List<Hashtag> hashtagList;


	public Tweet(long id, String text, GeoLocation geoLocation, List<Hashtag> hashtagList) {
		this.id = id;
		this.text = text;
		this.geoLocation = geoLocation;
		this.hashtagList = hashtagList;
	}


	public Tweet(JsonObject jsonObject) {
		this.id = jsonObject.get("id").getAsLong();
		this.text = jsonObject.get("text").getAsString();
		this.geoLocation = createGeoLocation(jsonObject);
		this.hashtagList = createHashtags(jsonObject);
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


	public void setGeoLocation(GeoLocation geolocation) {
		this.geoLocation = geoLocation;
	}


	public List<Hashtag> getHashtagList() {
		return hashtagList;
	}


	public void setHashtagList(List<Hashtag> hashtagList) {
		this.hashtagList = hashtagList;
	}


	private static GeoLocation createGeoLocation(JsonObject jsonObject) {
		if (!jsonObject.get("geo").isJsonNull()) {
			JsonObject geo = jsonObject.get("geo").getAsJsonObject();
			JsonArray coordinates = geo.get("coordinates").getAsJsonArray();
			return new GeoLocation(coordinates.get(0).getAsDouble(), coordinates.get(1).getAsDouble());
		}
		return null;
	}


	private static List<Hashtag> createHashtags(JsonObject jsonObject) {
		JsonObject entities = jsonObject.getAsJsonObject("entities");
		JsonArray hashtagEntities = entities.getAsJsonArray("hashtags");

		if (hashtagEntities.size() == 0) {
			return null;
		}

		List<Hashtag> hashtagList = new ArrayList<>();
		for (JsonElement hashtag : hashtagEntities) {
			hashtagList.add(new Hashtag(hashtag.getAsJsonObject().get("text").getAsString()));
		}
		return hashtagList;
	}
}
