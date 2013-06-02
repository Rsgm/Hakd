package hakd.other;

import com.google.gag.annotation.remark.Hack;

public class Util {

	@Hack
	public static int[] orthoToIso(float x, float y, int roomWidth) {// converts float x/y orthogonal screen coordinates into int x/y isometric map
// coordinates
		float firstA = -0.5f * x + y; // checks every frame, so use float not double
		float secondA = 0.5f * x + y;

		// I have a page full of math written out for this algorithm, front and back
		float firstIntersectX;
		float firstIntersectY;

		float secondIntersectX;
		float secondIntersectY; // TODO this is a little off, but it works for now

		firstIntersectY = -y + firstA + 0.25f; // I could explain all of these numbers, or you could just trust they work
		firstIntersectX = (firstIntersectY - firstA) * 2;

		secondIntersectY = secondA - y + 0.25f;
		secondIntersectX = (secondA - secondIntersectY) * 2;

		int[] returnInt = new int[2];
		returnInt[0] = (int) (0.9f * Math.sqrt((firstIntersectX - x) * (firstIntersectX - x) + (firstIntersectY - y) * (firstIntersectY - y)));
		returnInt[1] =
				(int) (roomWidth - 0.9f * Math
						.sqrt((secondIntersectX - x) * (secondIntersectX - x) + (secondIntersectY - y) * (secondIntersectY - y)));

		return returnInt;
	}

// public static isoToOrtho{}
}