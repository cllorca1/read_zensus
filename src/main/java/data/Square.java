package data;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class Square {

   private final int id;
   private BoundingBox boundingBox;
   private Map<Integer, String> shapes;
   private final String code;

   public Square(int id, String code){
       this.id = id;
       this.code = code;
       boundingBox = new BoundingBox(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
       shapes = new LinkedHashMap<Integer, String>(10);
   }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public Map<Integer, String> getShapes() {
        return Collections.unmodifiableMap(shapes);
    }

    public void addShape(int shapeOrder, String shape){
       if (shape !=null){
           this.shapes.put(shapeOrder,shape);
       }
    }

}
