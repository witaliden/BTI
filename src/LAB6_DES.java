import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class LAB6_DES {
    private static final String ALGORITHM = "DES";
    private static final int BLOCK_SIZE = 8;

    public static void main(String[] args) throws InvalidKeyException, InvalidKeySpecException,
            IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, Exception {
        Scanner sc = new Scanner(System.in);
        String plainText = sc.nextLine().trim();
        int numBlocks = (plainText.length() + BLOCK_SIZE - 1) / BLOCK_SIZE;
        System.out.println("");
        byte[] key = new byte[8];
        new SecureRandom().nextBytes(key);

        // ECB
        SecretKey desKey = SecretKeyFactory.getInstance(ALGORITHM)
                .generateSecret(new DESKeySpec(key));
        Cipher desCipher = Cipher.getInstance(ALGORITHM + "/ECB/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey);
        byte[] cipherText = desCipher.doFinal(plainText.getBytes());
        System.out.println("Tekst zaszyfrowany w ECB: {} " + new String(cipherText));

        desCipher.init(Cipher.DECRYPT_MODE, desKey);
        byte[] decryptedText = desCipher.doFinal(cipherText);
        System.out.println("Tekst odszyfrowany w ECB: " + new String(decryptedText));

/*        for (int i = 0; i < numBlocks; i++) {
            int start = i * BLOCK_SIZE;
            int end = Math.min(start + BLOCK_SIZE, plainText.length());
            byte[] block = plainText.substring(start, end).getBytes();
            byte[] encryptedBlock = desCipher.doFinal(block);
            System.arraycopy(encryptedBlock, 0, cipherText, start, encryptedBlock.length);
        }*/

        // CBC
        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        desCipher = Cipher.getInstance(ALGORITHM + "/CBC/PKCS5Padding");
        desCipher.init(Cipher.ENCRYPT_MODE, desKey, ivSpec);
        cipherText = desCipher.doFinal(plainText.getBytes());
        System.out.println("Tekst zaszyfrowany w CBC: " + new String(cipherText));

        desCipher.init(Cipher.DECRYPT_MODE, desKey, ivSpec);
        decryptedText = desCipher.doFinal(cipherText);
        System.out.println("Tekst odszyfrowany w CBC: " + new String(decryptedText));
    }
}
