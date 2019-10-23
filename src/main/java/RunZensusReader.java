import data.BoundingBox;
import data.DataSet;
import data.RasterCell;
import input.DictionaryReader;
import input.GridShapeFileReader;
import input.PopulationAttributesReader;
import input.PopulationReader;
import org.apache.log4j.Logger;
import output.GridShapeFileWriter;
import output.OutputWriter;

import javax.annotation.Resources;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;


public class RunZensusReader {

    public static Logger logger = Logger.getLogger(RunZensusReader.class);

    public static void main(String[] args) throws FileNotFoundException {

        //define bounding box in the reference system of the raster cells (
        BoundingBox bb = new BoundingBox(4357150, 2721533, 4511356, 2886574);
        DataSet dataSet = new DataSet(bb);



        String baseDirectory = "c:/data/zensus2011/";
        String shapeDirectory = baseDirectory + "out/shapes/";
        //String shapeDirectory = baseDirectory + "grid/Lambert/DE_Grid_ETRS89-LAEA_100m.shape/";
        String dataDirectory = baseDirectory + "data/";
        String outputDirectory = baseDirectory + "out/";
        String ppFile = dataDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        ClassLoader classLoader = RunZensusReader.class.getClassLoader();
        String fileName = new File(classLoader.getResource("dictionary.csv").getFile()).getAbsolutePath();
        new DictionaryReader(dataSet).read(fileName, ",");

        PopulationReader populationReader = new PopulationReader(dataSet);
        populationReader.read(ppFile, ";");

        String ppAttributesFile;
        PopulationAttributesReader populationAttributesReader;

        String[] attributeFiles = new String[]{"Bevoelkerung100M.csv",
                "Familie100m.csv",
                //"Geb100m.csv",
                "Haushalte100m.csv",
                "Wohnungen100m.csv"
        };

        //the different files have different delimiters (specified in the same order as the file names)
        String[] delimiters = new String[]{";",
                ",",
                //",",
                ",",
                ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" //this splits by comma if comma is not between quotes
        };

        String[] unitTypes = new String[]{"PP",
                "FF",
                //"GG",
                "HH",
                "DD"
        };

        for (String unitType : unitTypes){
            RasterCell.registerAttribute("INSGESAMT_0_" + unitType);
        }

        for (int i=0; i < attributeFiles.length; i++) {
            logger.warn("Reading " + attributeFiles[i]);
            ppAttributesFile = dataDirectory + attributeFiles[i];
            populationAttributesReader = new PopulationAttributesReader(dataSet, unitTypes[i]);
            populationAttributesReader.read(ppAttributesFile, delimiters[i]);
        }

        String shpFile;
        GridShapeFileReader gridShapeFileReader;

/*        shpFile = shapeDirectory + "muc100by100TAZ.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");*/

        shpFile = shapeDirectory + "n27e43.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = shapeDirectory + "n27e44_.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = shapeDirectory + "n27e45.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = shapeDirectory + "n28e43.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = shapeDirectory + "n28e44_.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = shapeDirectory + "n28e45_.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        String outShpFile = outputDirectory + "zones_with_populationMUC.shp";
        GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
        gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, outputDirectory + "zonalDataSummaryMUC.csv",
                outputDirectory + "zonalConfidenceSummaryMUC.csv");


    }


}
