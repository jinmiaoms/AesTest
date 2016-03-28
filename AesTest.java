import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.crypto.Cipher;
import java.security.AlgorithmParameters;
import java.util.Random;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AesTest
{
   private byte[] ivBytes;
   private final String AES_TYPE = "AES/CTR/NoPadding";
   //private final String AES_TYPE = "AES/CBC/PKCS5Padding";
   private final Random rand = new Random();
   
   public AesTest() {
   }

   public byte[] enc(byte[] data, byte key[]) throws Exception
   {
      Cipher c = Cipher.getInstance(AES_TYPE);
      SecretKeySpec k =
         new SecretKeySpec(key, "AES");
      c.init(Cipher.ENCRYPT_MODE, k);
      AlgorithmParameters params = c.getParameters();
      ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
      //return new CipherInputStream(input, c);
      byte[] encryptedData = c.doFinal(data);
      return encryptedData;  
   }
   
   public byte[] dec(byte encData[], byte key[]) throws Exception
   {
       Cipher c = Cipher.getInstance(AES_TYPE);
       SecretKeySpec k =
         new SecretKeySpec(key, "AES");
       c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(ivBytes));
       byte[] data = c.doFinal(encData);
       return data;  
   }
   
   public byte[] genInput(int fileSize) throws Exception
   {
       byte[] content = new byte[fileSize];
       rand.nextBytes(content);
       //return new ByteArrayInputStream(content);
       return content;
   }
   
    public static void main(String[] arges) throws Exception {
        int numIt = 10;
        int fileSize = 1024 * 1024;
        if (arges.length > 0) {
            numIt = Integer.parseInt(arges[0]);
            if (arges.length > 1)
            {
                fileSize = Integer.parseInt(arges[1]);
            }
        }
        
        AesTest at = new AesTest();
        //byte [] key="1111111111111111".getBytes("UTF-8");
        byte [] key="C0BAE23DF8B51807".getBytes("UTF-8");
        //byte [] key="C0BAE23DF8B51807B3E17D21925FADF2".getBytes("UTF-8");
        //byte[] data = "this is a test".getBytes("UTF-8");
        //InputStream input = at.genInput(fileSize);       
        byte[] data = at.genInput(fileSize);
        System.out.println("Max key length allowed: " + Cipher.getMaxAllowedKeyLength("AES"));
	long x = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            try {
                byte[] encData = at.enc(data, key);
                //at.enc(input, key);
                at.dec(encData, key);
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
                byte[] encData = at.enc(data, key);
                //at.enc(data, key);
                at.dec(encData, key);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw ex;
            }
            System.out.println(System.nanoTime() - x);
       }
    }
}
