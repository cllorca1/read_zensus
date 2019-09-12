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

import java.io.FileNotFoundException;


public class RunZensusReader {

    public static Logger logger = Logger.getLogger(RunZensusReader.class);

    public static void main(String[] args) throws FileNotFoundException {

        //define bounding box in the reference system of the raster cells (
        BoundingBox bb = new BoundingBox(4442677, 2855076, 4507382, 2907901);
        DataSet dataSet = new DataSet(bb);



        String baseDirectory = "c:/projects/radLast/data/zensus/";
        String ppFile = baseDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        new DictionaryReader(dataSet).read(baseDirectory + "dictionary.csv", ",");

        PopulationReader populationReader = new PopulationReader(dataSet);
        populationReader.read(ppFile, ";");

        String ppAttributesFile;
        PopulationAttributesReader populationAttributesReader;

        String[] attributeFiles = new String[]{"Bevoelkerung100M.csv",
                "Familie100m.csv",
                "Geb100m.csv",
                "Haushalte100m.csv",
                "Wohnungen100m.csv"
        };

        //the different files have different delimiters (specified in the same order as the file names)
        String[] delimiters = new String[]{";",
                ",",
                ",",
                ",",
                ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" //this splits by comma if comma is not between quotes
        };

        String[] unitTypes = new String[]{"PP",
                "FF",
                "GG",
                "HH",
                "DD"
        };

        for (String unitType : unitTypes){
            RasterCell.registerAttribute("INSGESAMT_0_" + unitType);
        }

        for (int i=0; i < attributeFiles.length; i++) {
            logger.warn("Reading " + attributeFiles[i]);
            ppAttributesFile = baseDirectory + attributeFiles[i];
            populationAttributesReader = new PopulationAttributesReader(dataSet, unitTypes[i]);
            populationAttributesReader.read(ppAttributesFile, delimiters[i]);
        }

        String shpFile;
        GridShapeFileReader gridShapeFileReader;

        shpFile = baseDirectory + "shp_28_44/100kmN28E44_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = baseDirectory + "shp_28_45/100kmN28E45_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = baseDirectory + "shp_29_44/100kmN29E44_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        shpFile = baseDirectory + "shp_29_45/100kmN29E45_DE_Grid_ETRS89-LAEA_100m.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");

        String outShpFile = baseDirectory + "out/zones_with_population.shp";
        GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
        gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, baseDirectory + "zonalDataSummary.csv", baseDirectory + "zonalConfidenceSummary.csv");


    }


}
