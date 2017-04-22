package tweetprocessor.data;

import java.util.function.Predicate;


public class ProfilePredicates {
	public static Predicate<Profile> hasBelgianLocation() {
		return p -> p.getPredictedLocation().getCountry().equals("BE") ||
				p.getLocation().toLowerCase().contains("belgium") ||
				p.getLocation().toLowerCase().contains("belgië") ||
				p.getLocation().toLowerCase().contains("belgie");
	}


	public static Predicate<Profile> hasEdgeCaseLocation() {
		return p -> p.getLocation().toLowerCase().equals("nederland") ||
				p.getLocation().toLowerCase().equals("hier") ||
				p.getLocation().toLowerCase().equals("aarde") ||
				p.getLocation().toLowerCase().equals("nl") ||
				p.getLocation().toLowerCase().equals("holland");
	}
}
