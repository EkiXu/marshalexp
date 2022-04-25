package xyz.eki.marshalexp.utils;

import java.lang.reflect.Field;

public class ReflectUtils {
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
}
