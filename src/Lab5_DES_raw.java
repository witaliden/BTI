import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

public class Lab5_DES_raw {
    private static final char[] DIGITS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    public static void main(String[] args) {
        String c1 = "8C46A7C86E865292" + "9279D6386885D034";
        String c2 = "CED60BECE94F7F3D" + "25BCC84A3B8538B9";
        String c3 = "1F52329BF1131525" + "9123DA989BCD7037";

        // Getting ZMK key from three clear components
        String zmk = bin2hex(xor(xor(hex2bin(c1), hex2bin(c2)), hex2bin(c3)));
        assert "5dc29ebf76da388a26e6c4eac8cd98ba".equalsIgnoreCase(zmk);
        System.out.println("ZMK key: " + zmk);

        String checkValue = des3(zmk, "0000000000000000" + "0000000000000000", true);
        assert checkValue.toLowerCase().startsWith("81ecea");
        System.out.println("Check value: " + checkValue);

        String clearMac = des3(zmk, "2C8ED7A334299BD8" + "5AD1C1AC9B30EB35", false);
        assert "0123456789abcdefabcdef9876543210".equalsIgnoreCase(clearMac);
        System.out.println("Clear MAC key: " + clearMac);
    }

    private static String des3(String hexKey, String hexData, boolean encrypt) {
        byte[] key1 = hex2bin(hexKey.substring(0, 16)); // First eight bytes of key
        byte[] key2 = hex2bin(hexKey.substring(16, 32)); // Last eight bytes of key
        byte[] result = desRound(key1, hex2bin(hexData), encrypt);
        result = desRound(key2, result, !encrypt);
        result = desRound(key1, result, encrypt);
        return bin2hex(result);
    }

    private static byte[] desRound(byte[] key, byte[] data, boolean encrypt) {
        try {
            SecretKey keySpec = new SecretKeySpec(key, "DES");
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec);
            return cipher.doFinal(data);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private static byte[] xor(byte[] a1, byte[] a2) {
        byte[] result = new byte[a1.length];
        for (int i = 0; i < a1.length; i++) {
            result[i] = (byte) (a1[i] ^ a2[i]);
        }
        return result;
    }

    private static byte[] hex2bin(String hex) {
        char[] data = hex.toCharArray();
        final int len = data.length;

        if ((len & 0x01) != 0)
            throw new IllegalArgumentException("Odd number of characters");

        final byte[] out = new byte[len >> 1];

        // Two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    private static String bin2hex(byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        // Two characters form the hex value
        for (int i = 0, j = 0; i < l; i++) {
            out[j++] = DIGITS[(0xF0 & data[i]) >>> 4];
            out[j++] = DIGITS[0x0F & data[i]];
        }
        return new String(out);
    }

    private static int toDigit(final char ch, final int index) {
        final int digit = Character.digit(ch, 16);
        if (digit == -1)
            throw new IllegalArgumentException("Illegal hexadecimal character " + ch + " at index " + index);
        return digit;
    }
}
