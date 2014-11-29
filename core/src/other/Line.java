package other;

import com.badlogic.gdx.math.Vector2;
import org.jdelaunay.delaunay.geometries.DEdge;

public class Line {
    private final Vector2 pointA;
    private final Vector2 pointB;

    public Line(Vector2 pointA, Vector2 pointB) {
        this.pointA = new Vector2(pointA);
        this.pointB = new Vector2(pointB);
    }

    public Line(float x1, float y1, float x2, float y2) {
        pointA = new Vector2(x1, y1);
        pointB = new Vector2(x2, y2);
    }

    public Line(DEdge e) {
        pointA = new Vector2((float) e.getStartPoint().getX(), (float) e.getStartPoint().getY());
        pointB = new Vector2((float) e.getEndPoint().getX(), (float) e.getEndPoint().getY());
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
