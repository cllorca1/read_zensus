import org.cts.CRSFactory;
import org.cts.IllegalCoordinateException;
import org.cts.crs.CRSException;
import org.cts.crs.GeodeticCRS;
import org.cts.op.CoordinateOperation;
import org.cts.op.CoordinateOperationFactory;
import org.cts.registry.EPSGRegistry;
import org.cts.registry.RegistryManager;
import org.matsim.api.core.v01.Coord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by carlloga on 15/12/16.
 */
public class TransformCoordinates {


    private  List<CoordinateOperation> coortzdOps;

    public TransformCoordinates() throws CRSException {


        CRSFactory cRSFactory = new CRSFactory();
        RegistryManager registryManager = cRSFactory.getRegistryManager();
        registryManager.addRegistry(new EPSGRegistry());


        try {
            GeodeticCRS sourceGCRS = (GeodeticCRS) cRSFactory.getCRS("EPSG:4326");
            GeodeticCRS targetGCRS = (GeodeticCRS) cRSFactory.getCRS("EPSG:31468");
            //System.out.println( sourceGCRS.getName());
            //System.out.println( targetGCRS.getName());

            coortzdOps = new ArrayList<CoordinateOperation>(
                    CoordinateOperationFactory.createCoordinateOperations(sourceGCRS, targetGCRS));


        } catch (CRSException e) {
            e.printStackTrace();

        }

    }

    public Coord transformCoordinates(Coord coordinate) {


        try {

            double[] coord = new double[]{coordinate.getX(), coordinate.getY()};
            //SevenParameterTransformation op = createBursaWolfTransformation(612.4, 77, 440.2, -0.054, 0.057, -2.797, 2.55);

            for (int i = 0; i < coortzdOps.size(); i++) {
                CoordinateOperation op = coortzdOps.get(i);

                coord = op.transform(coord);
            }

            return new Coord(coord[0], coord[1]);


        } catch (IllegalCoordinateException e) {
            e.printStackTrace();

            return null;
        }
    }

}
