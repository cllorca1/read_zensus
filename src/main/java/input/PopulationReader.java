package input;

import data.Codes;
import data.DataSet;
import data.RasterCell;
import de.tum.bgu.msm.util.MitoUtil;
import org.locationtech.jts.geom.Coordinate;


public class PopulationReader extends GenericCSVReader {

    public PopulationReader(DataSet data) {
        super(data);
    }

    protected void processHeader(String[] header) {
        for (String code : Codes.CODES_POPULATION){
            indexes.put(code, MitoUtil.findPositionInArray(code, header));
        }
    }

    protected void processRecord(String[] record) {
        String id = record[indexes.get(Codes.ID)];
        int x = Integer.parseInt(record[indexes.get(Codes.X)]);
        int y = Integer.parseInt(record[indexes.get(Codes.Y)]);
        int population = Integer.parseInt(record[indexes.get(Codes.POPULATION)]);
        Coordinate coordinate = new Coordinate(x,y);
        if (data.getBoundingBox().isInBoundingBox(coordinate)) {
            data.getRasterCells().put(id, new RasterCell(id, coordinate, population));
        }

    }
}
