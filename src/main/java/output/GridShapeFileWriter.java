package output;

import data.DataSet;
import data.RasterCell;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.matsim.api.core.v01.Coord;
import org.matsim.core.utils.gis.PolygonFeatureFactory;
import org.matsim.core.utils.gis.ShapeFileWriter;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.util.ArrayList;
import java.util.List;

public class GridShapeFileWriter {


    private final DataSet dataSet;

    public GridShapeFileWriter(DataSet dataSet) {
        this.dataSet = dataSet;
    }

    public void writeShapefileOfZonesWithPopulation(String fileName) {

        ShapeFileWriter shapeFileWriter = new ShapeFileWriter();

        List<SimpleFeature> features = new ArrayList<SimpleFeature>();
        CoordinateReferenceSystem cr;
        try {
            cr = dataSet.getRasterCells().values().iterator().next().getFeature().getFeatureType().getCoordinateReferenceSystem();
        } catch (Exception e) {
            throw new RuntimeException("The original raster cell shp does not have a reference system!");
        }


        PolygonFeatureFactory polygonFeatureFactory = new PolygonFeatureFactory.Builder()
                .setName("rasterCell")
                .addAttribute("id", String.class)
                .addAttribute("x_mp", Long.class)
                .addAttribute("y_mp", Long.class)
                .addAttribute("ags", Long.class)
                .addAttribute("population", Integer.class)
                .setCrs(cr)
                .create();

        for (RasterCell rasterCell : dataSet.getRasterCells().values()) {
            if (rasterCell.getPopulation() > 0) {
                SimpleFeature feature = rasterCell.getFeature();
                //SimpleFeature newFeature = new polygonFeatureFactory(feature.getAttributes(), feature.getFeatureType(), feature.getIdentifier());
                SimpleFeature newFeature = polygonFeatureFactory.createPolygon(((MultiPolygon) feature.getDefaultGeometry()).getCoordinates());
                newFeature.setAttribute("id", feature.getAttribute("id"));
                newFeature.setAttribute("x_mp", feature.getAttribute("x_mp"));
                newFeature.setAttribute("y_mp", feature.getAttribute("y_mp"));
                newFeature.setAttribute("ags", feature.getAttribute("ags"));
                newFeature.setAttribute("population", rasterCell.getPopulation());

                if (newFeature != null) {
                    features.add(newFeature);
                }

            }
        }

        shapeFileWriter.writeGeometries(features, fileName);


    }

    ;


}
