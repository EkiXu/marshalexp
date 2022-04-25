package xyz.eki.marshalexp.poc;

public interface POC {
   default Object getPocObject(String cmd) throws Exception{
       throw new Exception("Not Implemented");
   }

    default Object getPocObject(byte[] clzBytes) throws Exception{
        throw new Exception("Not Implemented");
    }
}
