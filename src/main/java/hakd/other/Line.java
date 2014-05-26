package hakd.other;

import com.badlogic.gdx.math.Vector2;

public class Line {
    private final Vector2 pointA;
    private final Vector2 pointB;

    public Line(Vector2 pointA, Vector2 pointB) {
        this.pointA = pointA;
        this.pointB = pointB;
    }

    public Line(float x1, float y1, float x2, float y2) {
        pointA = new Vector2(x1, y1);
        pointB = new Vector2(x2, y2);
    }

    public Vector2 getPointA() {
        return pointA;
    }

    public Vector2 getPointB() {
        return pointB;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof Line) {
            if (pointA.equals(((Line) obj).getPointA()) && pointB.equals(((Line) obj).getPointB())) {
                return true;
            } else if (pointA.equals(((Line) obj).getPointB()) && pointB.equals(((Line) obj).getPointA())) {
                return true;
            }
        }

        return false;
    }
}
