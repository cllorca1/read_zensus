import data.BoundingBox;
import data.DataSet;
import data.RasterCell;
import input.PopulationAttributesReader;
import input.PopulationReader;

import java.io.FileNotFoundException;


public class RunZensusReader {

    public static void main(String[] args) throws FileNotFoundException {

        BoundingBox bb = new BoundingBox(4461831, 2869351, 4489630, 2891972);
        DataSet dataSet = new DataSet(bb);

        String baseDirectory = "c:/projects/radLast/data/zensus/";
        String ppFile = baseDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        PopulationReader populationReader = new PopulationReader(dataSet);
        populationReader.read(ppFile, ";");

        String ppAttributesFile = baseDirectory + "Bevoelkerung100M.csv";
        PopulationAttributesReader populationAttributesReader = new PopulationAttributesReader(dataSet);
        populationAttributesReader.read(ppAttributesFile, ";");

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, baseDirectory + "zonalDataSummary.csv");


    }


}
