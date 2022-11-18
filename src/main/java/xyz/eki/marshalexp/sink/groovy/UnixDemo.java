package xyz.eki.marshalexp.sink.groovy;

public class UnixDemo {
    public static void main(String[] args) {
        byte[] bytes  = "ls".getBytes();
        byte[] result = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, result, 0, bytes.length);
        result[result.length - 1] = (byte) 0;
    }
}
