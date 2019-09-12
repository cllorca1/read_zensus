package input;

import data.Codes;
import data.DataSet;
import data.RasterCell;
import de.tum.bgu.msm.util.MitoUtil;

public class PopulationAttributesReader extends GenericCSVReader {

    private final String thisUnitType;

    public PopulationAttributesReader(DataSet data, String thisUnitType) {
        super(data);
        this.thisUnitType = thisUnitType;
    }

    protected void processHeader(String[] header) {
        for (String code : Codes.CODES_POPULATION_ATTRIBUTES){
            indexes.put(code, MitoUtil.findPositionInArray(code, header));
        }

    }

    protected void processRecord(String[] record) {
        String id = record[indexes.get(Codes.ID)];

        if (data.getRasterCells().keySet().contains(id)){

            String attributeName = record[indexes.get(Codes.MERKMAL_CODE)];
            attributeName = attributeName.replace("\"", "").trim();
            int attributeCategory = Integer.parseInt(record[indexes.get(Codes.MERKMAL_LEVEL)]);
            int number = Integer.parseInt(record[indexes.get(Codes.MERKMAL_N)]);
            int numberConfindence = Integer.parseInt(record[indexes.get(Codes.MERKMAL_N_CONFIDENCE)]);

            RasterCell rasterCell = data.getRasterCells().get(id);
            if (attributeName.equals("INSGESAMT")){
                rasterCell.addAttribute(attributeName + "_" + attributeCategory + "_" + thisUnitType, number);
                rasterCell.addAttributesConfidence(attributeName + "_" + attributeCategory, numberConfindence);
            } else {
                rasterCell.addAttribute(attributeName + "_" + attributeCategory, number);
                rasterCell.addAttributesConfidence(attributeName + "_" + attributeCategory, numberConfindence);
            }


        }


    }
}
