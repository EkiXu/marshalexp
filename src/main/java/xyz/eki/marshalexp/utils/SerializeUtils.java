package xyz.eki.marshalexp.utils;

import com.caucho.hessian.io.*;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.*;

public class SerializeUtils {
    public static class NoWriteReplaceSerializerFactory extends SerializerFactory {

        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getObjectSerializer(java.lang.Class)
         */
        @Override
        public Serializer getObjectSerializer ( Class<?> cl ) throws HessianProtocolException {
            return super.getObjectSerializer(cl);
        }


        /**
         * {@inheritDoc}
         *
         * @see com.caucho.hessian.io.SerializerFactory#getSerializer(java.lang.Class)
         */
        @Override
        public Serializer getSerializer ( Class cl ) throws HessianProtocolException {
            Serializer serializer = super.getSerializer(cl);

            if ( serializer instanceof WriteReplaceSerializer ) {
                return UnsafeSerializer.create(cl);
            }
            return serializer;
        }

    }

    public static byte[] serialize(Object o) throws Exception{
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(o);
        oos.close();
        bos.close();
        return bos.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bis);
        bis.close();
        ois.close();
        return ois.readObject();
    }

    public static byte[] hessian2Serialize(Object o) throws Exception{
//        ByteArrayOutputStream os = new ByteArrayOutputStream();
//        AbstractHessianOutput out = new Hessian2Output(os);
//        out.writeObject(o);
//        return os.toByteArray();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        AbstractHessianOutput out = new Hessian2Output(bos);
        NoWriteReplaceSerializerFactory sf = new NoWriteReplaceSerializerFactory();
        sf.setAllowNonSerializable(true);
        out.setSerializerFactory(sf);
        out.writeObject(o);
        out.close();
        return bos.toByteArray();
    }

    public static Object hessian2Deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        AbstractHessianInput ahi = new Hessian2Input(bis);

        return ahi.readObject();
    }

    public static byte[] kryoSerialize(Object o) throws Exception{
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Output output = new Output(bos);
        kryo.writeClassAndObject(output,o);
        output.flush();
        output.close();
        return bos.toByteArray();
    }

    public static Object kryoDeserialize(byte[] bytes) throws Exception{
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        return kryo.readClassAndObject(input);
    }

    public static Object kryoAltDeserialize(byte[] bytes) throws Exception{
        Kryo kryo=new Kryo();
        kryo.setReferences(true);
        kryo.setRegistrationRequired(false);
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        Input input = new Input(bis);
        return kryo.readClassAndObject(input);
    }


}
