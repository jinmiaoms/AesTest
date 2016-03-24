import javax.crypto.Cipher;
import java.security.AlgorithmParameters;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class AesTest
{
   private byte[] ivBytes;
   
   public AesTest() {
   }

   public byte[] enc(byte data[], byte key[]) throws Exception
   {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
      SecretKeySpec k =
         new SecretKeySpec(key, "AES");
      c.init(Cipher.ENCRYPT_MODE, k);
      AlgorithmParameters params = c.getParameters();
      ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
      byte[] encryptedData = c.doFinal(data);
      return encryptedData;  
   }
   
   public byte[] dec(byte encData[], byte key[]) throws Exception
   {
       Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
       SecretKeySpec k =
         new SecretKeySpec(key, "AES");
       c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(ivBytes));
       byte[] data = c.doFinal(encData);
       return data;  
   }
   
    public static void main(String[] arges) throws Exception {
        int numIt = 100;
        if (arges.length > 0) {
            numIt = Integer.parseInt(arges[0]);
        }
        byte [] key="1111111111111111".getBytes("UTF-8");
        byte[] data = "this is a test".getBytes("UTF-8");
        AesTest at = new AesTest();

	long x = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            try {
                byte[] encData = at.enc(data, key);
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
                at.dec(encData, key);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw ex;
            }
            System.out.println(System.nanoTime() - x);
       }
    }
}
