import data.BoundingBox;
import data.DataSet;
import data.RasterCell;
import data.Square;
import input.DictionaryReader;
import input.GridShapeFileReader;
import input.PopulationAttributesReader;
import input.PopulationReader;
import org.apache.log4j.Logger;
import output.GridShapeFileWriter;
import output.OutputWriter;
import sun.util.locale.provider.LocaleNameProviderImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;


public class RunZensusReader {

    public static Logger logger = Logger.getLogger(RunZensusReader.class);

    public static void main(String[] args) throws FileNotFoundException {



        String baseDirectory = "c:/data/zensus2011/";
        String shapeDirectory = baseDirectory + "grid/Lambert/DE_Grid_ETRS89-LAEA_100m.shape/";
        String dataDirectory = baseDirectory + "data/";
        String outputDirectory = baseDirectory + "out/Germany/";
        File destFolder = new File(outputDirectory);
        if(!destFolder.exists()){
            boolean directoryCreated = destFolder.mkdir();
            if (!directoryCreated) logger.warn("Could not create directory for copying: ");
        }
        String ppFile = dataDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        int squareCount = 1;

        for (int latitude = 28; latitude < 29; latitude ++){ //26 - 36

            for (int longitude = 44; longitude < 45; longitude++){ //40 - 47

                String code = "N" + latitude + "E" + longitude;

                Square square = new Square(squareCount, code);
                square.setBoundingBox(new BoundingBox(longitude * 100000, latitude * 100000, (longitude + 1)* 100000, (latitude + 1)* 100000));
                square.addShape(1,"100km" + square.getCode() + "_DE_Grid_ETRS89-LAEA_100m.shp");

                DataSet dataSet = new DataSet(square.getBoundingBox());

                ClassLoader classLoader = RunZensusReader.class.getClassLoader();
                String fileName = new File(classLoader.getResource("dictionary.csv").getFile()).getAbsolutePath();
                new DictionaryReader(dataSet).read(fileName, ",");

                PopulationReader populationReader = new PopulationReader(dataSet);
                populationReader.read(ppFile, ";");

                String ppAttributesFile;
                PopulationAttributesReader populationAttributesReader;

                String[] attributeFiles = new String[]{"Bevoelkerung100M.csv",
                        //"Familie100m.csv",
                        //"Geb100m.csv",
                        //"Haushalte100m.csv",
                        //"Wohnungen100m.csv"
                };

                //the different files have different delimiters (specified in the same order as the file names)
                String[] delimiters = new String[]{";",
                        ",",
                        //",",
                        ",",
                        ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)" //this splits by comma if comma is not between quotes
                };

                String[] unitTypes = new String[]{"PP",
                        //"FF",
                        //"GG",
                        //"HH",
                        //"DD"
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
                for (Map.Entry<Integer, String> entry : square.getShapes().entrySet()){
                    shpFile = shapeDirectory + entry.getValue();
                    gridShapeFileReader = new GridShapeFileReader(dataSet);
                    gridShapeFileReader.readFeatures(shpFile, "id");
                }

                String outShpFile = outputDirectory + "zones_with_population_" + square.getCode() + ".shp";
                GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
                gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

                OutputWriter.printOutRasterCellsWithPopulation(dataSet, outputDirectory + "zonalDataSummary_" + square.getCode() + ".csv",
                        outputDirectory + "zonalConfidenceSummary_" + square.getCode() + ".csv");

                squareCount++;
            }
        }

        /*Square state = new Square(Integer.parseInt(args[0]));
        obtainBoundingBox(state);
        obtainShapes(state);
        DataSet dataSet = new DataSet(state.getBoundingBox());


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
        GridShapeFileReader gridShapeFileReader;*//*        shpFile = shapeDirectory + "muc100by100TAZ.shp";
        gridShapeFileReader = new GridShapeFileReader(dataSet);
        gridShapeFileReader.readFeatures(shpFile, "id");*//*

        for (Map.Entry<Integer, String> entry : state.getShapes().entrySet()){
            shpFile = shapeDirectory + entry.getValue();
            gridShapeFileReader = new GridShapeFileReader(dataSet);
            gridShapeFileReader.readFeatures(shpFile, "id");
        }

        String outShpFile = outputDirectory + "zones_with_population_" + state.getId() + ".shp";
        GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
        gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

        OutputWriter.printOutRasterCellsWithPopulation(dataSet, outputDirectory + "zonalDataSummary_" + state.getId() + ".csv",
                outputDirectory + "zonalConfidenceSummary_" + state.getId() + ".csv");*/

    }

    public static void obtainBoundingBox(Square state){

        BoundingBox bb = null;
        int stateId = state.getId();
        if (stateId == 1) { //Schleswig-Holstein
            bb = new BoundingBox(4200000, 3300000, 4400000, 3550000);
        } else if (stateId == 2) { // Hamburg
            bb = new BoundingBox(4300000, 3300000, 4400000, 3410000);
        } else if (stateId == 3) { // Niedersachsen
            bb = new BoundingBox(4096000, 3128000, 4429500, 3419000);
        } else { //Munich
            bb = new BoundingBox(4357150, 2721533, 4511356, 2886574);
        }

        state.setBoundingBox(bb);

        //BoundingBox bb = new BoundingBox(4357150, 2721533, 4511356, 2886574); FOR MUNICH

    }

    public static void obtainShapes(Square state){

        String[] shapes = null;
        int stateId = state.getId();
        if (stateId == 1) {//Schleswig-Holstein
            shapes = new String[] {"N35E42","N35E43","N34E42","N34E43","N34E44","N33E42","N33E43"};
        } else if (stateId == 2) { // Hamburg (inside state 1 as well)
            shapes = new String[] {"N33E43","N34E43"};
        } else if (stateId == 3) { // Niedersachsen
            shapes = new String[] {"N31E42","N31E43","N32E42","N32E42","N32E43","N33E41","N33E42","N33E43","N33E44"};
        } else if (stateId == 4) { // Hamburg (inside state 3 as well)
            shapes = new String[] {"N31E42","N31E43","N32E42","N32E42","N32E43","N33E41","N33E42","N33E43","N33E44"};
        }
        int i = 1;
        for (String shape : shapes){
            state.addShape(i, "100km" + shape + "_DE_Grid_ETRS89-LAEA_100m.shp");
            i++;
        }
    }


}
