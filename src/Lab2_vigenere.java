import java.util.Scanner;

public class Lab2_vigenere {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nPodaj wiadomość do zaszyfrowania: ");
        String message = sc.nextLine().toUpperCase();

        System.out.println("Podaj podstawę do szyfrowania: ");
        String encryptKey = sc.nextLine().toUpperCase();

        String encodedMessage = encodeMessage(message, encryptKey);
        System.out.println("\nZaszyfrowana wiadomość:");
        System.out.println(encodedMessage);

        System.out.println("\nDeszyfrowana wiadomość:");
        System.out.println(decodeMessage(encodedMessage, encryptKey));
    }


    private static String decodeMessage(String encodedMessage, String encryptKey) {
        StringBuilder result = new StringBuilder();
        encodedMessage = encodedMessage.toUpperCase();

        for (int i = 0, j = 0; i < encodedMessage.length(); i++) {
            char letter = encodedMessage.charAt(i);
            if (!Character.isAlphabetic(letter)) result.append(letter);
            else {
                result.append((char) ((letter - encryptKey.charAt(j) + 26) % 26 + 65));
                j = ++j % encryptKey.length();
            }
        }
        return result.toString().toLowerCase();
    }

    private static String encodeMessage(String message, String encryptKey) {
        StringBuilder result = new StringBuilder();
        message = message.toUpperCase();
        for (int i = 0, j = 0; i < message.length(); i++) {
            char letter = message.charAt(i);
            if (!Character.isAlphabetic(letter)) result.append(letter);
            else {
                result.append((char) (((letter - 65) + (encryptKey.charAt(j) - 65)) % 26 + 65));
                j = ++j % encryptKey.length();
            }
        }
        return result.toString().toLowerCase();
    }
}
