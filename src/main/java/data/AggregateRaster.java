package data;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AggregateRaster {


    public static void main(String[] args) throws IOException {



        BufferedReader reader = new BufferedReader(new FileReader("F:/oms_lu/raster/park_raster.csv"));
        Map<String,Integer> rasters = new HashMap<>();
        reader.readLine();
        String line;
        int counter = 0;

        while ((line = reader.readLine())!= null ){
            double x = Double.parseDouble(line.split(",")[0]);
            double y = Double.parseDouble(line.split(",")[1]);

            int x_100 = (int) Math.floor(x / 100.);
            int y_100 = (int) Math.floor(y / 100.);

            String code = "100mN" +  y_100 +  "E" + x_100;

            rasters.putIfAbsent(code, 0);
            rasters.put(code, rasters.get(code) + 1);

            if (counter % 1e6 == 0){
                System.out.println("Counter " + counter);
            }
            counter++;

        }
        System.out.println("Read complete");
        PrintWriter pw = new PrintWriter("C:/projects/bast_entlastung/data/lu/processed/park_raster_100.csv");
        pw.println("n,cell_id");
        for (String raster : rasters.keySet()){
            pw.println(rasters.get(raster)+ "," +  raster);
        }

        pw.close();
        System.out.println("Write complete");

    }
}
