package output;

import data.DataSet;
import data.RasterCell;
import org.matsim.core.utils.gis.ShapeFileWriter;
import org.opengis.feature.simple.SimpleFeature;

import java.util.ArrayList;
import java.util.List;

public class GridShapeFileWriter {


    private final DataSet dataSet;

    public GridShapeFileWriter(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void writeShapefileOfZonesWithPopulation(String fileName){

        ShapeFileWriter shapeFileWriter = new ShapeFileWriter();

        List<SimpleFeature> features = new ArrayList<SimpleFeature>();

        for (RasterCell rasterCell : dataSet.getRasterCells().values()){
            if (rasterCell.getPopulation() > 0){
                SimpleFeature feature = rasterCell.getFeature();
                //feature.setAttribute("population", rasterCell.getPopulation());
                if (feature != null){
                    features.add(feature);
                }

            }
        }

        shapeFileWriter.writeGeometries(features, fileName);


    };


}
