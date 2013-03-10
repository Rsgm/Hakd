package hakd.other.enumerations.names;

public enum Owner {
	COMPANY("Company"), TEST("Test"); // these will be replaced with better ones

	public String	company;

	private Owner(String company) {
		this.company = company;
	}
}
