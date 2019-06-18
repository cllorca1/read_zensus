package data;

import org.locationtech.jts.geom.Coordinate;

public class BoundingBox {

    private final double xMin;
    private final double yMin;
    private final double xMax;
    private final double yMax;

    public BoundingBox(double xMin, double yMin, double xMax, double yMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public boolean isInBoundingBox(Coordinate coordinate){
        if (coordinate.x > xMin &&
                coordinate.y > yMin &&
                coordinate.x < xMax &&
                coordinate.y < yMax){
            return true;
        } else {
            return false;
        }


    };

}
