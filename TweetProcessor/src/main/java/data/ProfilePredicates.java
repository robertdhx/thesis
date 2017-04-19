package data;

import java.util.function.Predicate;


public class ProfilePredicates {
	public static Predicate<Profile> hasBelgianLocation() {
		return p -> p.getPredictedLocation().getCountry().equals("BE");
	}


	public static Predicate<Profile> hasEdgeCaseLocation() {
		return p -> p.getPredictedLocation().getPlace().equals("Nederland") ||
				p.getPredictedLocation().getPlace().equals("Holland");
	}
}
