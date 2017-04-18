package data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


public class PredictedLocation {
	private String place;

	private String province;


	public PredictedLocation(String place, String province) {
		this.place = place;
		this.province = province;
	}


	public String getPlace() {
		return place;
	}


	public void setPlace(String place) {
		this.place = place;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	@Override public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(place)
				.append(province)
				.toHashCode();
	}


	@Override public boolean equals(Object o) {
		if (o == this) { return true; }
		if (!(o instanceof PredictedLocation)) {
			return false;
		}

		PredictedLocation predictedLocation = (PredictedLocation) o;

		return new EqualsBuilder()
				.append(place, predictedLocation.place)
				.append(province, predictedLocation.province)
				.isEquals();
	}


	@Override public String toString() {
		return this.getPlace() + " - " + this.getProvince();
	}
}
