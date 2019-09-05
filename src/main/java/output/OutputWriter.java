package output;

import data.DataSet;
import data.RasterCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OutputWriter {

    public static void printOutRasterCellsWithPopulation(DataSet dataSet, String fileNameAttributes, String fileNameConfidences) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(fileNameAttributes));
        pw.println(RasterCell.getHeader());
        for (RasterCell rasterCell : dataSet.getRasterCells().values()){
            if (rasterCell.getPopulation() > 0){
                pw.println(rasterCell.printOutAttributesInLine().replace("null", "0"));
            }
        }
        pw.close();

        PrintWriter pw2 = new PrintWriter(new File(fileNameConfidences));
        pw2.println(RasterCell.getHeader());
        for (RasterCell rasterCell : dataSet.getRasterCells().values()){
            if (rasterCell.getPopulation() > 0){
                pw2.println(rasterCell.printOutAttributeConfidenceInLine().replace("null", "0"));
            }
        }
        pw2.close();
    }

}
