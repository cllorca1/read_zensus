import data.RasterCell;
import de.tum.bgu.msm.util.MitoUtil;
import org.apache.log4j.Logger;
import org.locationtech.jts.geom.Coordinate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class merges the information of raster cells as csv format (the output of the class ReadGridAndAssignTAZ) and
 * land uses per raster cell (the output of a process made in R that converts the osm land use files into rasters of 10x10 m
 * resolution.
 */
public class AssignLUToGrid {

    private static Logger logger = Logger.getLogger(AssignLUToGrid.class);
    private static Map<String, RasterCell> landUseByRaster = new HashMap<>();


    public static void main(String[] args) throws IOException {


        String gridDirectory = "F:/zensus2011/";
        String shapeDirectory = gridDirectory + "grid/Lambert/DE_Grid_ETRS89-LAEA_100m.shape/";
        int counter = 0;


        String workingDirectory = "C:/projects/bast_entlastung/data/lu/processed";

        RasterCell.registerAttribute("TAZ_id");

        for (int latitude = 27; latitude < 33; latitude++) {
            for (int longitude = 41; longitude < 45; longitude++) {
                processSquare(counter, shapeDirectory, latitude, longitude);
            }
        }

        //other squares that are outside the main rectangle
        int[] latitudes = {29, 30, 31, 32, 27, 28, 29, 30, 31, 32, 33, 34, 30, 31, 32, 33, 34, 34, 34, 35, 35, 26, 28, 34, 33, 33, 33, 33, 34, 28, 35};
        int[] longitudes = {40, 40, 40, 40, 45, 45, 45, 45, 45, 45, 45, 45, 46, 46, 46, 46, 42, 43, 44, 42, 43, 43, 46, 46, 41, 42, 43, 44, 41, 40, 45};

        int j = 0;
        for (int latitude : latitudes) {

            int longitude = longitudes[j];
            processSquare(counter, shapeDirectory, latitude, longitude);
            j++;
        }

        logger.info("Finished reading rasters of 100 m x 100 m");


        String[] landUses = new String[]{"commercial", "industrial", "residential", "retail", "allotments",
                "cemetery", /*"farm", "forest", */"grass", "heat", "meadow", "military", "natural_reserve", "orchard",
                /*"park",*/ "quarry", "recreation_ground", "scrub", "vineyard"};


        for (String landUse : landUses) {
            RasterCell.registerAttribute(landUse);
            String luCsvFile = workingDirectory + "/" + landUse + "_raster_100.csv";


            BufferedReader br = new BufferedReader(new FileReader(luCsvFile));

            String[] header = br.readLine().split(",");
            int posN = MitoUtil.findPositionInArray("n", header);
            int posId = MitoUtil.findPositionInArray("cell_id", header);

            String line;
            while ((line = br.readLine()) != null) {
                String id = line.split(",")[posId];
                int n = Integer.parseInt(line.split(",")[posN]);
                RasterCell cell = landUseByRaster.get(id);
                if (cell != null){
                    cell.addAttribute(landUse, n);
                } else {
                    logger.warn("Cell not found");
                }
            }
            logger.info("Finished reading LU: " + landUse);
        }

        PrintWriter pw = new PrintWriter(workingDirectory + "/all_raster_100.csv");

        pw.print("id,TAZ_id");
        for (String landUse : landUses) {
            pw.print(",");
            pw.print(landUse);
        }
        pw.println();

        for (RasterCell rasterCell : landUseByRaster.values()){
            pw.print(rasterCell.getId());
            pw.print(",");
            Map<String, Integer> attributes = rasterCell.getAttributes();
            pw.print(attributes.get("TAZ_id"));
            for (String landUse : landUses) {
                pw.print(",");
                if (attributes.containsKey(landUse)){
                    pw.print(attributes.get(landUse));
                } else {
                    pw.print(0);
                }
            }
            pw.println();
        }

        pw.close();



    }

    private static void processSquare(int counter, String shapeDirectory, int latitude, int longitude) throws IOException {
        String code = "N" + latitude + "E" + longitude;
        String csvFile;
        csvFile = shapeDirectory + "100km" + code + "_DE_Grid_ETRS89-LAEA_100m.csv";

        BufferedReader br = new BufferedReader(new FileReader(csvFile));

        int idIndex = 0;
        int xIndex = 1;
        int yIndex = 2;
        int tazIndex = 3;

        br.readLine();

        String line;
        while ((line = br.readLine()) != null) {
            String row[] = line.split(",");
            String id = row[idIndex];
            RasterCell cell = new RasterCell(id, new Coordinate(Double.parseDouble(row[xIndex]), Double.parseDouble(row[yIndex])), 0);

            cell.addAttribute("TAZ_id", Integer.parseInt(row[tazIndex]));
            landUseByRaster.put(id, cell);
        }
        counter++;
        logger.info("Finished reading raster of 100 m x 100 m: " + csvFile);
    }


}
