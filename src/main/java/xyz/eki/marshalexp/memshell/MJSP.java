package xyz.eki.marshalexp.memshell;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class MJSP {
    static {
        try {
            FileOutputStream outputStream = new FileOutputStream("/tmp/1.txt");
            // 需要将String转换为bytes
            byte[] strToBytes = "123".getBytes();
            outputStream.write(strToBytes);

        } catch (IOException e) {
        }
    }
}
