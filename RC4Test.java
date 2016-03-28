
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jinmiao
 */
public class RC4Test {
    
    public final static String CIPHER_RC4 = "RC4";
    public final static String KEY_SPEC_RC4 = "ARCFOUR";

    private final Random rand = new Random();
    private SecretKeySpec skeySpec;
    
    public byte[] genInput(int fileSize) throws Exception
    {
        byte[] content = new byte[fileSize];
        rand.nextBytes(content);
        //return new ByteArrayInputStream(content);
        return content;
    }
    
    public SecretKeySpec createKey() {
        try {
            /*
	     * The class KeyGenerator provided the functionality of
	     * symmetric-key keygenerator
             */
            KeyGenerator kgen = KeyGenerator.getInstance("RC4");

            /*
	     * Initialize the key generator with the keysize. The key size is an
	     * algorithm specific metric specified in number of bits
             */
            kgen.init(128); //16 bits

            /* Generate the SecretKey */
            SecretKey skey = kgen.generateKey();

            /* Get the key in the encoded format */
            byte[] raw = skey.getEncoded();

            /* Construct an algorithm specific secret key from byte array */
            skeySpec = new SecretKeySpec(raw, KEY_SPEC_RC4);
        } catch (NoSuchAlgorithmException nsae) {
            System.out.println(nsae);
        }
        return skeySpec;
    }
    
    public byte[] enc(byte[] data) throws Exception
    {
        Cipher cipher = Cipher.getInstance(CIPHER_RC4);
        //SecretKey secretKey = new SecretKeySpec(key, KEY_SPEC_RC4);
        if (skeySpec == null)
            createKey();
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encryptedData = cipher.doFinal(data);
        return encryptedData; 
    }
    
    public static void main(String[] args) throws Exception
    {
        int numIt = 10;
        int fileSize = 1024 * 1024;
        if (args.length > 0) {
            numIt = Integer.parseInt(args[0]);
            if (args.length > 1)
            {
                fileSize = Integer.parseInt(args[1]);
            }
        }
        
        RC4Test test = new RC4Test();
        byte[] data = test.genInput(fileSize);
        long x = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            try {
                byte[] encData = test.enc(data);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw ex;
            }
        }
	System.out.println(System.nanoTime() - x);
	System.out.println("Finish warmup");
    
        for (int i = 0; i < numIt; i++) {
            x = System.nanoTime();
            try {
                byte[] encData = test.enc(data);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw ex;
            }
            System.out.println(System.nanoTime() - x);
       }
    }
}
