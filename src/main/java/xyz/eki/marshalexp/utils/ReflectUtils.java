package xyz.eki.marshalexp.utils;

import com.nqzero.permit.Permit;
import sun.misc.Unsafe;
import sun.print.UnixPrintServiceLookup;
import sun.reflect.ReflectionFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReflectUtils {

    public static void setAccessible(AccessibleObject member) {
        String versionStr = System.getProperty("java.version");
        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
        if (javaVersion < 12) {
            // quiet runtime warnings from JDK9+
            Permit.setAccessible(member);
        } else {
            // not possible to quiet runtime warnings anymore...
            // see https://bugs.openjdk.java.net/browse/JDK-8210522
            // to understand impact on Permit (i.e. it does not work
            // anymore with Java >= 12)
            member.setAccessible(true);
        }
    }

    public static Field getField ( final Class<?> clazz, final String fieldName ) throws Exception {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            if ( field != null )
                field.setAccessible(true);
            else if ( clazz.getSuperclass() != null )
                field = getField(clazz.getSuperclass(), fieldName);

            return field;
        }
        catch ( NoSuchFieldException e ) {
            if ( !clazz.getSuperclass().equals(Object.class) ) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            throw e;
        }
    }


    public static void setFieldValue ( final Object obj, final String fieldName, final Object value ) throws Exception {
        final Field field = getField(obj.getClass(), fieldName);
        field.set(obj, value);
    }

    public static Object getFieldValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = null;
        Class<?> clazz = object.getClass();

        //如果是接口中的field直接getField
        try{
            field = clazz.getField(fieldName);
        }catch (NoSuchFieldException e1){
            while (clazz != Object.class) {
                try {
                    field = clazz.getDeclaredField(fieldName);
                    break;
                    //如果NoSuchField继续往父类找
                } catch (NoSuchFieldException e2) {
                    clazz = clazz.getSuperclass();
                }
            }
        }

        if (field == null) {
            throw new NoSuchFieldException(fieldName);
        } else {
            field.setAccessible(true);
            return field.get(object);
        }
    }

    public static Constructor<?> getFirstCtor(final String name) throws Exception {
        final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

    public static Constructor<?> getFirstCtor(Class<?> clz) throws Exception {
        final Constructor<?> ctor = clz.getDeclaredConstructors()[0];
        setAccessible(ctor);
        return ctor;
    }

    public static Object newInstance(String className, Object ... args) throws Exception {
        return getFirstCtor(className).newInstance(args);
    }

    public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
    }


    @SuppressWarnings ( {
            "unchecked"
    } )
    public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes,
                                                Object[] consArgs ) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
        objCons.setAccessible(true);
        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
        sc.setAccessible(true);
        return (T) sc.newInstance(consArgs);
    }

    public static <T> T createProxy(final InvocationHandler ih, final Class<T> iface, final Class<?>... ifaces) {
        final Class<?>[] allIfaces = (Class<?>[]) Array.newInstance(Class.class, ifaces.length + 1);
        allIfaces[0] = iface;
        if (ifaces.length > 0) {
            System.arraycopy(ifaces, 0, allIfaces, 1, ifaces.length);
        }
        return iface.cast(Proxy.newProxyInstance(ReflectUtils.class.getClassLoader(), allIfaces, ih));
    }

    public static void TriggerAllGetter(Object obj) throws Exception{
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        List<PropertyDescriptor> descriptors = Arrays.stream(beanInfo.getPropertyDescriptors()).filter(p -> {
            String name = p.getName();
            //过滤掉不需要修改的属性
            return !"class".equals(name) && !"id".equals(name);
        }).collect(Collectors.toList());

        for (PropertyDescriptor descriptor : descriptors) {
            //Method setterMethod  = descriptor.getWriteMethod();
            Method readMethod = descriptor.getReadMethod();
//            System.out.println(descriptor.getName());
            try {
                Object o = readMethod.invoke(obj);
                System.out.println(o);
            } catch (Exception ignored) {

            }
        }
    }

    public static Object forceNewInstance(Class<?> clazz) throws Exception{
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe.allocateInstance(clazz);
    }

//    public static void setAccessible(AccessibleObject member) {
//        // quiet runtime warnings from JDK9+
//        Permit.setAccessible(member);
//    }
}
