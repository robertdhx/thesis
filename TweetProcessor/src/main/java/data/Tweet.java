package data;

import java.util.Set;


public class Tweet {
	private long id;

	private String text;

	private GeoLocation geolocation;

	private Set<Hashtag> hashtagSet;


	public Tweet(long id, String text, GeoLocation geolocation, Set<Hashtag> hashtagSet) {
		this.id = id;
		this.text = text;
		this.geolocation = geolocation;
		this.hashtagSet = hashtagSet;
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


	public GeoLocation getGeolocation() {
		return geolocation;
	}


	public void setGeolocation(GeoLocation geolocation) {
		this.geolocation = geolocation;
	}


	public Set<Hashtag> getHashtagSet() {
		return hashtagSet;
	}


	public void setHashtagSet(Set<Hashtag> hashtagSet) {
		this.hashtagSet = hashtagSet;
	}
}
