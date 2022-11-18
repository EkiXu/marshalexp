package xyz.eki.marshalexp.solution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;

public class FileReadWays {
    public static String loc = "/etc/passwd";
    public static void case1() throws Exception{
        String urlContent = "";
        final URL url = new URL("file://"+loc);
        final BufferedReader in = new BufferedReader(new
                InputStreamReader(url.openStream()));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            urlContent = urlContent + inputLine + "\n";
        }
        in.close();
        System.out.println(urlContent);
    }

    public static void case2() throws Exception{
        String urlContent = "";
        final URI uri = URI.create("file://"+loc);
        final BufferedReader in = new BufferedReader(new
                InputStreamReader(uri.toURL().openStream()));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            urlContent = urlContent + inputLine + "\n";
        }
        in.close();
        System.out.println(urlContent);
    }

    public static void case3() throws Exception{
        String urlContent = "";
        final URL url = new URL("netdoc://"+loc);
        final BufferedReader in = new BufferedReader(new
                InputStreamReader(url.openStream()));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            urlContent = urlContent + inputLine + "\n";
        }
        in.close();
        System.out.println(urlContent);
    }

//    public static void case4() throws Exception{
//        String urlContent = "";
//        final URL url = new URL("classpath:"+loc);
//        final BufferedReader in = new BufferedReader(new
//                InputStreamReader(url.openStream()));
//        String inputLine = "";
//        while ((inputLine = in.readLine()) != null) {
//            urlContent = urlContent + inputLine + "\n";
//        }
//        in.close();
//        System.out.println(urlContent);
//    }

    public static void main(String[] args) throws Exception{
        case3();
    }
}
