package data;

import org.locationtech.jts.geom.Coordinate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RasterCell {

    private String id;
    private Coordinate coordinate;
    private int population;
    private final Map<String, Integer> attributes = new HashMap<String, Integer>();
    private final Map<String, Integer> attributesConfidence = new HashMap<String, Integer>();

    private static List<String> listOfAttributes = new ArrayList<String>();

    public void addAttribute(String key, int value) {
        attributes.put(key, value);
        if (!listOfAttributes.contains(key)){
            listOfAttributes.add(key);
        }
    }

    public void addAttributesConfidence(String key, int value) {
         attributesConfidence.put(key, value);
    }

    public int getPopulation() {
        return population;
    }

    public RasterCell(String id, Coordinate coordinate, int population) {
        this.id = id;
        this.coordinate = coordinate;
        this.population = population;
    }

    public static String getHeader(){
        StringBuilder builder = new StringBuilder();
        builder.append("id").append(",").append("x").append(",").append("y").append(",").append("population");

        for (String key : listOfAttributes){
            builder.append(",");
            builder.append(key);
        }

        return builder.toString();

    }

    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append(id).append(",").append(coordinate.x).append(",").append(coordinate.y).append(",").append(population);

        for (String key : listOfAttributes){
            builder.append(",");
            builder.append(attributes.get(key));
        }

        return builder.toString();

    }


}
