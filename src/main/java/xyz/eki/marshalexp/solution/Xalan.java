package xyz.eki.marshalexp.solution;

import javax.xml.XMLConstants;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;

/*
XSL(可扩展样式表语言)是一种用于转换XML文档的语言，XSLT表示的就是XSL转换，
而XSL转换指的就是XML文档本身。
转换后得到的一般都是不同的XML文档或其他类型文档，例如HTML文档、CSV文件以及明文文本文件等等。
 */
public class Xalan {
    public static void case1() throws TransformerException {
        String xsltTemplate = "xslt/rce1.xslt";

//        javax.xml.transform.TransformerFactory factory = TransformerFactory.newInstance();
//        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        javax.xml.transform.Transformer transformer = factory.newTransformer();

        TransformerFactory factory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        javax.xml.transform.Transformer transformer = factory.newTransformer(new StreamSource(xsltTemplate));

        // 使用 Transformer 对 XML 文件进行转换
        transformer.transform(new StreamSource("xslt/victim.xml"), new StreamResult("xslt/output.xml"));


        //org.apache.xalan.xslt.Process.main(new String[] { "-XSLTC", "-IN", "xslt/victim.xml", "-XSL", xsltTemplate, "-XX", "-XT" });
    }

    public static void case2() throws TransformerException {
        String xsltTemplate = "xslt/rce2.xslt";

//        javax.xml.transform.TransformerFactory factory = TransformerFactory.newInstance();
//        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//        javax.xml.transform.Transformer transformer = factory.newTransformer();

        TransformerFactory factory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        javax.xml.transform.Transformer transformer = factory.newTransformer(new StreamSource(xsltTemplate));

        // 使用 Transformer 对 XML 文件进行转换
        transformer.transform(new StreamSource("xslt/victim.xml"), new StreamResult("xslt/output.xml"));
    }

    //https://blog.noah.360.net/xalan-j-integer-truncation-reproduce-cve-2022-34169/
    public static void case3(){
        String xsltTemplate = "xslt/ovflow.xslt";
        org.apache.xalan.xslt.Process.main(new String[] { "-XSLTC", "-IN", "xslt/victim.xml", "-XSL", xsltTemplate, "-SECURE", "-XX", "-XT" });
    }

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().exec("rm -rf xslt/*.class");
//        String xsltTemplate = "xslt/ovflow.xslt";
        case2();

    }
}
