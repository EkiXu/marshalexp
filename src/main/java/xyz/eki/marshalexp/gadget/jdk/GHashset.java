package xyz.eki.marshalexp.gadget.jdk;

import xyz.eki.marshalexp.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;

public class GHashset {
    public static HashSet generateHashSet(Object entry) throws Exception{
        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        HashMap hashset_map = (HashMap) ReflectUtils.getFieldValue(hashset,"map");
        Object[] array = (Object[]) ReflectUtils.getFieldValue(hashset_map,"table");

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,entry);
        return hashset;
    }
}
