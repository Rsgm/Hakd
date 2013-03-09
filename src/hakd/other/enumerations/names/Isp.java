package hakd.other.enumerations.names;

public enum Isp {
	// these are just some names I came up with, they are in no way referencing real companies, infinity LTD.
	// for now I will use the greek alphabeta until I think of company names
	ALPHA("Alpha"), BETA("Beta"), GAMMA("Gamma"), DELTA("Delta"), EPSILON("Epsilon"), ZETA("Zeta"), ETA("Eta"), THETA("Theta"), IOTA("Iota"), KAPPA(
			"Kappa"), LAMBDA("Lambda"), MU("Mu"), NU("Nu"), XI("Xi"), OMNICRON("Omnicron"), PI("Pi"), RHO("Rho"), SIGMA("Sigma"), TAU("Tau"),
	UPSILON("Upsilon"), PHI("Phi"), CHI("Chi"), PSI("Psi"), OMEGA("Omega");
	String	company;

	private Isp(String company) {
		this.company = company;
	}
}
