import data.BoundingBox;
import data.DataSet;
import input.GridShapeFileReader;
import input.PopulationAttributesReader;
import input.PopulationReader;
import output.GridShapeFileWriter;
import output.OutputWriter;

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

        String shpFile = baseDirectory + "shp_28_44/100kmN28E44_DE_Grid_ETRS89-LAEA_100m.shp";
        GridShapeFileReader gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id" );

        String outShpFile = baseDirectory + "shp_28_44/zones_with_population.shp";
        GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
        gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, baseDirectory + "zonalDataSummary.csv");


    }


}
