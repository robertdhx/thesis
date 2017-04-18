package util;

public class StringUtil {
	public static String getFirstName(String name) {
		if (name.indexOf(' ') > -1) {
			return name.substring(0, name.indexOf(' '));
		}
		return name;
	}


	public static String getFirstPartOfLocation(String location) {
		if (location.indexOf(',') > -1) {
			return location.substring(0, location.indexOf(','));
		}
		if (location.indexOf('/') > -1) {
			return location.substring(0, location.indexOf('/'));
		}
		return location;
	}
}
