package hakd.other.enumerations;

public enum Prefs { // this holds what each line in the prefs mean
	WIDTH(0), HEIGHT(1), FULLSCREEN(2), VSYNC(3); // these start at 0 but the total starts at 1

	public int			line;
	public static int	total	= 4;

	private Prefs(int line) {
		this.line = line;
	}
}
