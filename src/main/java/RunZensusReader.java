import data.BoundingBox;
import data.DataSet;
import input.GridShapeFileReader;
import input.PopulationAttributesReader;
import input.PopulationReader;
import org.matsim.core.utils.collections.Tuple;
import output.GridShapeFileWriter;
import output.OutputWriter;

import java.io.FileNotFoundException;


public class RunZensusReader {

    public static void main(String[] args) throws FileNotFoundException {

        //define bounding box in the reference system of the raster cells (
        BoundingBox bb = new BoundingBox(4442677, 2855076, 4507382, 2907901);
        DataSet dataSet = new DataSet(bb);

        String baseDirectory = "c:/projects/radLast/data/zensus/";
        String ppFile = baseDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        PopulationReader populationReader = new PopulationReader(dataSet);
        populationReader.read(ppFile, ";");

        String ppAttributesFile = baseDirectory + "Bevoelkerung100M.csv";
        PopulationAttributesReader populationAttributesReader = new PopulationAttributesReader(dataSet);
        populationAttributesReader.read(ppAttributesFile, ";");

        String shpFile;
        GridShapeFileReader gridShapeFileReader;

        shpFile = baseDirectory + "shp_28_44/100kmN28E44_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id" );

        shpFile = baseDirectory + "shp_28_45/100kmN28E45_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id" );

        shpFile = baseDirectory + "shp_29_44/100kmN29E44_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id" );

        shpFile = baseDirectory + "shp_29_45/100kmN29E45_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id" );

        String outShpFile = baseDirectory + "out/zones_with_population.shp";
        GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
        gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, baseDirectory + "zonalDataSummary.csv", baseDirectory + "zonalConfidenceSummary.csv");


    }


}
