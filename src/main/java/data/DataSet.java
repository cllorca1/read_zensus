package data;


import java.util.HashMap;
import java.util.Map;

public class DataSet {

    private final Map<String, RasterCell> rasterCells = new HashMap<String, RasterCell>();

    private final BoundingBox boundingBox;

    public DataSet(BoundingBox boundingBox) {
        if (boundingBox != null) {
            this.boundingBox = boundingBox;
        } else {
            this.boundingBox = new BoundingBox(Double.NEGATIVE_INFINITY,
                    Double.NEGATIVE_INFINITY,
                    Double.POSITIVE_INFINITY,
                    Double.POSITIVE_INFINITY);
        }
    }

    public Map<String, RasterCell> getRasterCells() {
        return rasterCells;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

}
