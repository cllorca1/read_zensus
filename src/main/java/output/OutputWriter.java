package output;

import data.DataSet;
import data.RasterCell;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class OutputWriter {

    public static void printOutRasterCellsWithPopulation(DataSet dataSet, String fileName) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new File(fileName));
        pw.println(RasterCell.getHeader());
        for (RasterCell rasterCell : dataSet.getRasterCells().values()){
            if (rasterCell.getPopulation() > 0){
                pw.println(rasterCell.toString().replace("null", "0"));
            }
        }
        pw.close();
    }

}
