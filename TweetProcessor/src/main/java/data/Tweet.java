package data;

import twitter4j.*;

import java.util.Date;


public class Tweet {
	private long id;

	private String text;

	private Date date;

	private GeoLocation geolocation;

	private Place place;

	private HashtagEntity[] hashtagEntities;


	public Tweet(long id, String text, Date date, GeoLocation geolocation, Place place, HashtagEntity[] hashtagEntities) {
		this.id = id;
		this.text = text;
		this.date = date;
		this.geolocation = geolocation;
		this.place = place;
		this.hashtagEntities = hashtagEntities;
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


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public GeoLocation getGeolocation() {
		return geolocation;
	}


	public void setGeolocation(GeoLocation geolocation) {
		this.geolocation = geolocation;
	}


	public Place getPlace() {
		return place;
	}


	public void setPlace(Place place) {
		this.place = place;
	}


	public HashtagEntity[] getHashtagEntities() {
		return hashtagEntities;
	}


	public void setHashtagEntities(HashtagEntity[] hashtagEntities) {
		this.hashtagEntities = hashtagEntities;
	}
}
