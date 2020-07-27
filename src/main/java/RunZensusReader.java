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
        if (!destFolder.exists()) {
            boolean directoryCreated = destFolder.mkdir();
            if (!directoryCreated) logger.warn("Could not create directory for copying: ");
        }
        String ppFile = dataDirectory + "Zensus_Bevoelkerung_100m-Gitter.csv";

        int squareCount = 1;

        //squares that are on the main rectangle of Germany
        for (int latitude = 27; latitude < 33; latitude++) {

            for (int longitude = 41; longitude < 45; longitude++) {
                int j = 0;
                //for (int latitude : latitudes){

                //    int longitude = longitudes[j];
                String code = "N" + latitude + "E" + longitude;

                Square square = new Square(squareCount, code);
                square.setBoundingBox(new BoundingBox(longitude * 100000, latitude * 100000, (longitude + 1) * 100000, (latitude + 1) * 100000));
                square.addShape(1, "100km" + square.getCode() + "_DE_Grid_ETRS89-LAEA_100m.shp");

                DataSet dataSet = new DataSet(square.getBoundingBox());

                ClassLoader classLoader = RunZensusReader.class.getClassLoader();
                String fileName = new File(classLoader.getResource("dictionary.csv").getFile()).getAbsolutePath();
                new DictionaryReader(dataSet).read(fileName, ",");

                PopulationReader populationReader = new PopulationReader(dataSet);
                populationReader.read(ppFile, ";");

                if (dataSet.getRasterCells() != null) {

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

                    for (String unitType : unitTypes) {
                        RasterCell.registerAttribute("INSGESAMT_0_" + unitType);
                    }

                    for (int i = 0; i < attributeFiles.length; i++) {
                        logger.warn("Reading " + attributeFiles[i]);
                        ppAttributesFile = dataDirectory + attributeFiles[i];
                        populationAttributesReader = new PopulationAttributesReader(dataSet, unitTypes[i]);
                        populationAttributesReader.read(ppAttributesFile, delimiters[i]);
                    }

                    String shpFile;
                    GridShapeFileReader gridShapeFileReader;
                    for (Map.Entry<Integer, String> entry : square.getShapes().entrySet()) {
                        shpFile = shapeDirectory + entry.getValue();
                        gridShapeFileReader = new GridShapeFileReader(dataSet);
                        gridShapeFileReader.readFeatures(shpFile, "id");
                    }

                    String outShpFile = outputDirectory + "zones_with_population_" + square.getCode() + ".shp";
                    GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
                    gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

                    OutputWriter.printOutRasterCellsWithPopulation(dataSet, outputDirectory + "zonalDataSummary_" + square.getCode() + ".csv",
                            outputDirectory + "zonalConfidenceSummary_" + square.getCode() + ".csv");

                }
                squareCount++;
                j++;
            }
        }

        //other squares that are outside the main rectangle
        int[] latitudes = {29,30,31,32,27,28,29,30,31,32,33,34,30,31,32,33,34,34,34,35,35,26,28,34,33,33,33,33,34,28,35};
        int[] longitudes = {40,40,40,40,45,45,45,45,45,45,45,45,46,46,46,46,42,43,44,42,43,43,46,46,41,42,43,44,41,40,45};

        int j = 0;
        for (int latitude : latitudes){

            int longitude = longitudes[j];
            String code = "N" + latitude + "E" + longitude;

            Square square = new Square(squareCount, code);
            square.setBoundingBox(new BoundingBox(longitude * 100000, latitude * 100000, (longitude + 1) * 100000, (latitude + 1) * 100000));
            square.addShape(1, "100km" + square.getCode() + "_DE_Grid_ETRS89-LAEA_100m.shp");

            DataSet dataSet = new DataSet(square.getBoundingBox());

            ClassLoader classLoader = RunZensusReader.class.getClassLoader();
            String fileName = new File(classLoader.getResource("dictionary.csv").getFile()).getAbsolutePath();
            new DictionaryReader(dataSet).read(fileName, ",");

            PopulationReader populationReader = new PopulationReader(dataSet);
            populationReader.read(ppFile, ";");

            if (dataSet.getRasterCells() != null) {

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

                for (String unitType : unitTypes) {
                    RasterCell.registerAttribute("INSGESAMT_0_" + unitType);
                }

                for (int i = 0; i < attributeFiles.length; i++) {
                    logger.warn("Reading " + attributeFiles[i]);
                    ppAttributesFile = dataDirectory + attributeFiles[i];
                    populationAttributesReader = new PopulationAttributesReader(dataSet, unitTypes[i]);
                    populationAttributesReader.read(ppAttributesFile, delimiters[i]);
                }

                String shpFile;
                GridShapeFileReader gridShapeFileReader;
                for (Map.Entry<Integer, String> entry : square.getShapes().entrySet()) {
                    shpFile = shapeDirectory + entry.getValue();
                    gridShapeFileReader = new GridShapeFileReader(dataSet);
                    gridShapeFileReader.readFeatures(shpFile, "id");
                }

                String outShpFile = outputDirectory + "zones_with_population_" + square.getCode() + ".shp";
                GridShapeFileWriter gridShapeFileWriter = new GridShapeFileWriter(dataSet);
                gridShapeFileWriter.writeShapefileOfZonesWithPopulation(outShpFile);

                OutputWriter.printOutRasterCellsWithPopulation(dataSet, outputDirectory + "zonalDataSummary_" + square.getCode() + ".csv",
                        outputDirectory + "zonalConfidenceSummary_" + square.getCode() + ".csv");

            }
            squareCount++;
            j++;
        }

    }

}
