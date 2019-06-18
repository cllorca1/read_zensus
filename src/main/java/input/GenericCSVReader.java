package input;

import com.google.common.math.LongMath;
import data.DataSet;
import de.tum.bgu.msm.util.MitoUtil;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class GenericCSVReader {

    protected DataSet data;

    private static final Logger logger = Logger.getLogger(GenericCSVReader.class);

    private BufferedReader reader;

    private int numberOfRecords = 0;

    protected final Map<String, Integer> indexes;

    public GenericCSVReader(DataSet data) {
        this.indexes = new HashMap<String, Integer>();
        this.data = data;
    }

    protected abstract void processHeader(String[] header);

    protected abstract void processRecord(String[] record);

    public void read(String fileName, String delimiter) {
        initializeReader(fileName, delimiter);
        try {
            String record;
            while ((record = reader.readLine()) != null) {
                numberOfRecords++;
                processRecord(record.split(delimiter));
                if (LongMath.isPowerOfTwo(numberOfRecords)){
                    logger.info("Read " + numberOfRecords + " records");
                }
            }
        } catch (IOException e) {
            logger.error("Error parsing record number " + numberOfRecords + ": " + e.getMessage(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(this.getClass().getSimpleName() + ": Read " + numberOfRecords + " records.");
    }

    private void initializeReader(String fileName, String delimiter) {
        try {
            reader = new BufferedReader(new FileReader(fileName.trim()));
            processHeader(reader.readLine().split(delimiter));
        } catch (IOException e) {
            logger.error("Error initializing csv reader: " + e.getMessage(), e);
        }
    }
}