package lwv.data;

public class Probability {
	public enum Province {
		NB, FL, OV, NH, ZE, GR, FR, LI, DR, GE, ZH, UT
	}


	private Province province;
	private double value;


	public Probability(Province province, double value) {
		this.province = province;
		this.value = value;
	}


	public Province getProvince() {
		return province;
	}


	public void setProvince(Province province) {
		this.province = province;
	}


	public double getValue() {
		return value;
	}


	public void setValue(double value) {
		this.value = value;
	}
}
