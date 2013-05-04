package hakd.other.enumerations;

public enum NetworkType { // these don't define networks or who owns them, their stats do, these just define how to generate a network
	PLAYER(), COMPANY(), TEST(), ISP(), NPC();// more to come
	private NetworkType() {
	}
}