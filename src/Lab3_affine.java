import java.util.Scanner;

public class Lab3_affine {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nPodaj wiadomość do zaszyfrowania: ");
        String message = sc.nextLine().toUpperCase();

        System.out.println("Podaj klucze do szyfrowania. \nPierwszy: ");
        int keyA = sc.nextInt();
        System.out.println("Drugi: ");
        int keyB = sc.nextInt();

        // Szyfrowanie
        String cipherText = encryptMessage(message.toCharArray(), keyA, keyB);
        System.out.println("Zaszyfrowana wiadomość: " + cipherText);

        // Deszyfrowanie
        System.out.println("Deszyfrowana wiadomość: " + decryptCipher(cipherText, keyA, keyB));
    }

    static String encryptMessage(char[] message, int a, int b) {
        StringBuilder cipher = new StringBuilder();
        for (char c : message) {
            // Unikamy konwertowania spacji
            if (c != ' ') {
                cipher.append((char) ((((a * (c - 'A')) + b) % 26) + 'A'));
            } else {
                cipher.append(c);
            }
        }
        return cipher.toString();
    }

    static String decryptCipher(String cipher, int a, int b) {
        StringBuilder message = new StringBuilder();
        int a_inwers = 0;
        int flag = 0;

        for (int i = 0; i < 26; i++) {
            flag = (a * i) % 26;

            // Sprawdzamy czy (a * i) % 26 == 1
            if (flag == 1) {
                a_inwers = i;
            }
        }
        for (int i = 0; i < cipher.length(); i++) {
            // Unikamy konwertowania spacji
            if (cipher.charAt(i) != ' ') {
                message.append((char) (((a_inwers *
                        ((cipher.charAt(i) + 'A' - b)) % 26)) + 'A'));
            } else {
                message.append(cipher.charAt(i));
            }
        }
        return message.toString();
    }

}