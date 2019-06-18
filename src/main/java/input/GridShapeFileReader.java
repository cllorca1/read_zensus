package input;

import data.DataSet;
import de.tum.bgu.msm.resources.Properties;
import de.tum.bgu.msm.resources.Resources;
import org.apache.log4j.Logger;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

public class GridShapeFileReader {

    private final DataSet dataSet;
    private final static Logger logger = Logger.getLogger(GridShapeFileReader.class);

    public GridShapeFileReader(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void readFeatures(String fileName, String idCode){
        int counter = 0;
        for (SimpleFeature feature: ShapeFileReader.getAllFeatures(fileName)) {
            String id = feature.getAttribute(idCode).toString();
            if (dataSet.getRasterCells().keySet().contains(id)){
                dataSet.getRasterCells().get(id).setFeature(feature);
                counter++;
            }
        }
        logger.info("Assigned " + counter + " features to raster cell objects.");
    }


}
