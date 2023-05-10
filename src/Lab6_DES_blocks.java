import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

public class Lab6_DES_blocks {
    private static final String ALGORITHM = "DES";
    private static final int BLOCK_SIZE = 8;

    public static void main(String[] args) throws Exception {
        String plainText = "The quick brown fox jumps over the lazy dog";
        int numBlocks = (plainText.length() + BLOCK_SIZE - 1) / BLOCK_SIZE;
        byte[] key = new byte[8];
        new SecureRandom().nextBytes(key);

        SecretKey desKey = SecretKeyFactory.getInstance(ALGORITHM)
                .generateSecret(new DESKeySpec(key));
        Cipher desCipher = Cipher.getInstance(ALGORITHM + "/ECB/NoPadding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);

        byte[] cipherText = new byte[plainText.length()];


        System.out.println("ECB mode cipher text: " + new String(cipherText));
    }
}
