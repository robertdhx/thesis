package data;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.HashSet;
import java.util.Set;


public class Tweet {
	private long id;

	private String text;

	private GeoLocation geoLocation;

	private Set<Hashtag> hashtagSet;


	public Tweet(long id, String text, GeoLocation geoLocation, Set<Hashtag> hashtagSet) {
		this.id = id;
		this.text = text;
		this.geoLocation = geoLocation;
		this.hashtagSet = hashtagSet;
	}


	public Tweet(Status status) {
		this.id = status.getId();
		this.text = status.getText();
		this.geoLocation = createGeoLocation(status.getGeoLocation());
		this.hashtagSet = createHashtags(status.getHashtagEntities());
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


	public Set<Hashtag> getHashtagSet() {
		return hashtagSet;
	}


	public void setHashtagSet(Set<Hashtag> hashtagSet) {
		this.hashtagSet = hashtagSet;
	}


	private static GeoLocation createGeoLocation(twitter4j.GeoLocation geoLocation) {
		if (geoLocation != null) {
			return new GeoLocation(geoLocation.getLatitude(), geoLocation.getLongitude());
		} else {
			return null;
		}
	}


	private static Set<Hashtag> createHashtags(HashtagEntity[] hashtagEntities) {
		if (hashtagEntities != null) {
			Set<Hashtag> hashtagSet = new HashSet<>();
			for (HashtagEntity hashtagEntity : hashtagEntities) {
				hashtagSet.add(new Hashtag(hashtagEntity.getText()));
			}
			return hashtagSet;
		} else {
			return null;
		}
	}
}
