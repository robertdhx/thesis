package data;

import twitter4j.HashtagEntity;
import twitter4j.Status;

import java.util.*;


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


	public Tweet(Status status) {
		this.id = status.getId();
		this.text = status.getText();
		this.geoLocation = createGeoLocation(status.getGeoLocation());
		this.hashtagList = createHashtags(status.getHashtagEntities());
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


	private static GeoLocation createGeoLocation(twitter4j.GeoLocation geoLocation) {
		if (geoLocation != null) {
			return new GeoLocation(geoLocation.getLatitude(), geoLocation.getLongitude());
		} else {
			return null;
		}
	}


	private static List<Hashtag> createHashtags(HashtagEntity[] hashtagEntities) {
		if (hashtagEntities != null) {
			List<Hashtag> hashtagList = new ArrayList<>();
			for (HashtagEntity hashtagEntity : hashtagEntities) {
				hashtagList.add(new Hashtag(hashtagEntity.getText()));
			}
			return hashtagList;
		} else {
			return null;
		}
	}
}
