package hakd.other;

import org.python.util.PythonInterpreter;

public final class Util {

	/**
	 * Converts float x and y orthogonal screen coordinates into int x and y
	 * isometric.
	 */
	public static int[] orthoToIso(float x, float y, int height) {
		// map coordinates
		float a1 = 0.5f * x + y; // checks every frame, so use float not double
		float a2 = -0.5f * x + y;
		float b = (height) / 2 + 0.6f;

		// I have a page full of math written out for this algorithm, front and
		// back, although it is just a system of equations
		float firstIntersectX;
		float firstIntersectY;

		float secondIntersectX;
		float secondIntersectY; // TODO this is a little off, but it works for
		// now

		firstIntersectY = .5f * x + .32f; // I could explain all of these
		// numbers, or you could just trust
		// they work
		firstIntersectX = 2 * (a1 - firstIntersectY);

		secondIntersectY = -y + a2 + b;
		secondIntersectX = 2 * (secondIntersectY - a2);

		int[] iso = new int[2];
		iso[0] = (int) (0.9f * Math.sqrt((firstIntersectX - x) * (firstIntersectX - x) + (firstIntersectY - y) * (firstIntersectY - y)));
		iso[1] = (int) (0.9f * Math.sqrt((secondIntersectX - x) * (secondIntersectX - x) + (secondIntersectY - y) * (secondIntersectY - y)));

		return iso;
	}

	/**
	 * Returns float x and y orthogonal screen coordinates at the bottom middle
	 * of the isometric tile coordinate.
	 */
	public static float[] isoToOrtho(float iX, float iY, float roomHeight) {
		float[] ortho = new float[2];
		ortho[0] = (roomHeight + iX - iY) / 2 - 0.5f;
		ortho[1] = (roomHeight - iX - iY) / 4 - 0.25f;
		return ortho; // adapted from
		// http://www.java-gaming.org/topics/isometric-screen-space/27698/view.html
	}

	public static String GanerateName() {
		PythonInterpreter pi = new PythonInterpreter();
		pi.execfile("src/hakd/other/NameGenerator.py");
		String s = pi.get("name").toString();
		return s;
	}
}
