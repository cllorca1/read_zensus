import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AggregateFrom10To100Raster {

    public static void main(String[] args) throws IOException {


        Map<String, Integer> landUseByRaster = new HashMap<>();

        BufferedReader reader = new BufferedReader(new FileReader("F:/oms_lu/raster/park_raster.csv"));

        reader.readLine();

        int counter = 0;

        String line;
        while((line = reader.readLine())!= null){
            double x = Double.parseDouble((line.split(",")[0]));
            double y = Double.parseDouble((line.split(",")[1]));

            int x_100 = (int) Math.floor(x/100.);
            int y_100 = (int) Math.floor(y/100.);

            String code  = "100m" + "N" + y_100 + "E" + x_100;

            landUseByRaster.putIfAbsent(code, 0);
            landUseByRaster.put(code, landUseByRaster.get(code) + 1);

            if (counter % 1e6 == 0){
                System.out.println("Done " + counter + " lines.");
            }


            counter++;

        }

        PrintWriter pw = new PrintWriter("C:/projects/bast_entlastung/data/lu/processed/park_raster_100.csv");
        pw.println("cell_id,n");
        for (String code : landUseByRaster.keySet()){
            pw.println(code + "," + landUseByRaster.get(code));
        }

        pw.close();


    }

}
