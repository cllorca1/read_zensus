package input;

import data.Codes;
import data.DataSet;
import data.RasterCell;
import de.tum.bgu.msm.util.MitoUtil;

public class PopulationAttributesReader extends GenericCSVReader {

    public PopulationAttributesReader(DataSet data) {
        super(data);
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
            int attributeCategory = Integer.parseInt(record[indexes.get(Codes.MERKMAL_LEVEL)]);
            int number = Integer.parseInt(record[indexes.get(Codes.MERKMAL_N)]);
            int numberConfindence = Integer.parseInt(record[indexes.get(Codes.MERKMAL_N_CONFIDENCE)]);

            RasterCell rasterCell = data.getRasterCells().get(id);
            rasterCell.addAttribute(attributeName + "_" + attributeCategory, number);
            rasterCell.addAttributesConfidence(attributeName + "_" + attributeCategory, numberConfindence);




        }


    }
}
