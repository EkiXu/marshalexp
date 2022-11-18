package xyz.eki.marshalexp.gadget.jdk;

import xyz.eki.marshalexp.utils.ReflectUtils;

import java.lang.reflect.InvocationHandler;
import java.util.Map;

public class GAnnotationInvocationHandler {
    private static String ANN_INV_HANDLER_CLASS = "sun.reflect.annotation.AnnotationInvocationHandler";
    public static InvocationHandler createMemoizedInvocationHandler(final Map<String, Object> map) throws Exception {
        return (InvocationHandler) ReflectUtils.getFirstCtor(ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
    }
}
