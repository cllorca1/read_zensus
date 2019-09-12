package input;

import data.DataSet;
import data.RasterCell;
import jdk.nashorn.internal.runtime.logging.DebugLogger;
import org.apache.log4j.Logger;

public class DictionaryReader extends GenericCSVReader{
    private final static Logger logger = Logger.getLogger(DictionaryReader.class);

    public DictionaryReader(DataSet data) {
        super(data);
    }

    private int attrIndex = 0;

    protected void processHeader(String[] header) {

    }

    protected void processRecord(String[] record) {
        RasterCell.registerAttribute(record[attrIndex]);
        logger.info("Registered " + record[attrIndex]);
    }

    public void read(String filename, String delimiter){
        super.read(filename, delimiter);

        //register the totals "manually since their attribute name is the same


    }
}
