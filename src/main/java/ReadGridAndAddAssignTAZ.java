import org.apache.log4j.Logger;
import org.locationtech.jts.geom.*;
import org.matsim.core.utils.gis.ShapeFileReader;
import org.opengis.feature.simple.SimpleFeature;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class reads the raster cell 100x100 m files and writes it as a csv adding a column of the TAZ where the raster cell
 * is located.
 */

public class ReadGridAndAddAssignTAZ {

    static Logger logger = Logger.getLogger(ReadGridAndAddAssignTAZ.class);

    public static void main(String[] args) throws FileNotFoundException {

        AtomicInteger counter = new AtomicInteger(0);

        String baseDirectory = "F:/zensus2011/";
        String shapeDirectory = baseDirectory + "grid/Lambert/DE_Grid_ETRS89-LAEA_100m.shape/";
        String zoneFileName = "C:/models/transit_germany/input/zones/de_zones_attributes_3035.shp";
        String dataDirectory = "C:/projects/bast_entlastung/data/lu/processed";

        String outputDirectory = baseDirectory + "out/Germany/";
        File destFolder = new File(outputDirectory);
        if (!destFolder.exists()) {
            boolean directoryCreated = destFolder.mkdir();
            if (!directoryCreated) logger.warn("Could not create directory for copying: ");
        }


        final Map<String, SimpleFeature> zones = new HashMap<String, SimpleFeature>();
        ShapeFileReader.getAllFeatures(zoneFileName).stream().forEach(f -> {
            zones.put(f.getAttribute("TAZ_id").toString(), f);
        });

        //squares that are on the main rectangle of Germany
        for (int latitude = 27; latitude < 33; latitude++) {

            for (int longitude = 41; longitude < 45; longitude++) {

                processSquare(counter, shapeDirectory, zones, latitude, longitude);
            }


        }


        //other squares that are outside the main rectangle
        int[] latitudes = {29, 30, 31, 32, 27, 28, 29, 30, 31, 32, 33, 34, 30, 31, 32, 33, 34, 34, 34, 35, 35, 26, 28, 34, 33, 33, 33, 33, 34, 28, 35};
        int[] longitudes = {40, 40, 40, 40, 45, 45, 45, 45, 45, 45, 45, 45, 46, 46, 46, 46, 42, 43, 44, 42, 43, 43, 46, 46, 41, 42, 43, 44, 41, 40, 45};

        int j = 0;
        for (int latitude : latitudes) {

            int longitude = longitudes[j];
            processSquare(counter, shapeDirectory, zones, latitude, longitude);
            j++;
        }
    }

    private static void processSquare(AtomicInteger counter, String shapeDirectory, Map<String, SimpleFeature> zones, int latitude, int longitude) throws FileNotFoundException {
        String code = "N" + latitude + "E" + longitude;
        String shpFile;
        shpFile = shapeDirectory + "100km" + code + "_DE_Grid_ETRS89-LAEA_100m.shp";

        PrintWriter pw = new PrintWriter(shapeDirectory + "100km" + code + "_DE_Grid_ETRS89-LAEA_100m.csv");
        pw.println("id,x_mp,y_mp,TAZ_id");

        ShapeFileReader.getAllFeatures(shpFile).parallelStream().forEach(feature -> {
            String id = feature.getAttribute("id").toString();
            double x = Integer.parseInt(feature.getAttribute("x_mp").toString());
            double y = Integer.parseInt(feature.getAttribute("y_mp").toString());
            GeometryFactory factory = new GeometryFactory();
            Point point = factory.createPoint(new Coordinate(x, y));
            for (String zId : zones.keySet()) {
                SimpleFeature z = zones.get(zId);
                if (((MultiPolygon) z.getDefaultGeometry()).contains(point)) {
                    pw.println(id +"," +  x +  "," + y + "," + zId);
                    break;
                }
            }
            counter.getAndIncrement();
            if (counter.get() % 10000 == 0){
                logger.info("Completed " + counter);
            }
        });
        pw.close();
        logger.info("Raster cells :  " + counter);
    }

}
