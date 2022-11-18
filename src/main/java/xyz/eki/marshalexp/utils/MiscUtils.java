package xyz.eki.marshalexp.utils;

import javassist.ClassPool;
import javassist.CtClass;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MiscUtils {

    public static String getLocalTime(){
        Date d = new Date();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }
    public static String randomString(int len){
        Random rnd = ThreadLocalRandom.current();
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < len; i++) {
            boolean isChar = (rnd.nextInt(2) % 2 == 0);// 输出字母还是数字
            if (isChar) { // 字符串
                int choice = rnd.nextInt(2) % 2 == 0 ? 65 : 97; // 取得大写字母还是小写字母
                ret.append((char) (choice + rnd.nextInt(26)));
            } else { // 数字
                ret.append(Integer.toString(rnd.nextInt(10)));
            }
        }
        return ret.toString();
    }
    public static String base64Encode(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }
    public static byte[] base64Decode(String str){
        return Base64.getDecoder().decode(str);
    }
    public static byte[] dumpClass(Class<?> clazz) throws Exception{
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get(clazz.getName());
        byte[] res = cc.toBytecode();
        cc.defrost();
        cc.detach();
        return res;
    }
    public static byte[] dumpCopyClass(Class<?> clazz) throws Exception{
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.getAndRename(clazz.getName(), randomString(6)+"."+clazz.getSimpleName());
        byte[] res = cc.toBytecode();
        cc.defrost();
        cc.detach();
        return res;
    }


    public static String bytes2HexString(byte[] bytes) {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret.append(hex.toUpperCase());
        }
        return ret.toString();
    }
}
