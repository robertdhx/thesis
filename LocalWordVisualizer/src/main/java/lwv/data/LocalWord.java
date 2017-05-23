package lwv.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;


public class LocalWord {
	public enum WordType {
		DESCRIPTION, TWEET, HASHTAG;
	}


	private WordType type;
	private String value;
	private List<Probability> probabilities;


	public LocalWord() {
	}


	public LocalWord(String[] lineParts) {
		this.type = WordType.valueOf(lineParts[0].toUpperCase());
		this.value = lineParts[1];
		this.probabilities = new ArrayList<>();
		probabilities.add(new Probability(Probability.Province.NB, Double.parseDouble(lineParts[2])));
		probabilities.add(new Probability(Probability.Province.FL, Double.parseDouble(lineParts[3])));
		probabilities.add(new Probability(Probability.Province.OV, Double.parseDouble(lineParts[4])));
		probabilities.add(new Probability(Probability.Province.NH, Double.parseDouble(lineParts[5])));
		probabilities.add(new Probability(Probability.Province.ZE, Double.parseDouble(lineParts[6])));
		probabilities.add(new Probability(Probability.Province.GR, Double.parseDouble(lineParts[7])));
		probabilities.add(new Probability(Probability.Province.FR, Double.parseDouble(lineParts[8])));
		probabilities.add(new Probability(Probability.Province.LI, Double.parseDouble(lineParts[9])));
		probabilities.add(new Probability(Probability.Province.DR, Double.parseDouble(lineParts[10])));
		probabilities.add(new Probability(Probability.Province.GE, Double.parseDouble(lineParts[11])));
		probabilities.add(new Probability(Probability.Province.ZH, Double.parseDouble(lineParts[12])));
		probabilities.add(new Probability(Probability.Province.UT, Double.parseDouble(lineParts[13])));
		probabilities.sort(Comparator.comparingDouble(Probability::getValue).reversed());
	}


	public WordType getType() {
		return type;
	}


	public void setType(WordType type) {
		this.type = type;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public List<Probability> getProbabilities() {
		return probabilities;
	}


	public void setProbabilities(List<Probability> probabilities) {
		this.probabilities = probabilities;
	}


	@Override public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(type)
				.append(value)
				.toHashCode();
	}


	@Override public boolean equals(Object o) {
		if (o == this) { return true; }
		if (!(o instanceof LocalWord)) {
			return false;
		}

		LocalWord localWord = (LocalWord) o;

		return new EqualsBuilder()
				.append(type, localWord.type)
				.append(value, localWord.value)
				.isEquals();
	}
}
