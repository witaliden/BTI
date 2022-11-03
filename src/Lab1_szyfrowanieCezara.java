
import java.util.Scanner;

public class Lab1_szyfrowanieCezara {
    private static final char LETTER_A = 'a';
    private static final char LETTER_Z = 'z';
    private static final int ALPHABET_SIZE = LETTER_Z - LETTER_A + 1;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("\nPodaj wiadomość do zaszyfrowania: ");
        String message = sc.nextLine();

        System.out.println("Podaj podstawę do szyfrowania: ");
        int encryptKey = sc.nextInt();
        System.out.println("Podaj drugą podstawę do szyfrowania: ");
        int encryptKey2 = sc.nextInt();

        String encodedMessage = encodeMessage(message, encryptKey);
        System.out.println("\nZaszyfrowana wiadomość:");
        System.out.println(encodedMessage);

        String encodedMessage2 = encodeMessage(encodedMessage, encryptKey2);
        System.out.println("\nZaszyfrowana wiadomość 2:");
        System.out.println(encodedMessage2);

        System.out.println("\nDeszyfrowana wiadomość:");
        String decodedMessage = decodeMessage(encodedMessage2, encryptKey2);
        System.out.println(decodedMessage);
        System.out.println("\nDeszyfrowana wiadomość:");
        System.out.println(decodeMessage(decodedMessage, encryptKey));
    }

    public static String encodeMessage(String message, int key) {
        StringBuilder result = new StringBuilder();
        for (char c : message.toCharArray()) {
            if (!Character.isAlphabetic(c)) result.append(c);
            else {
                int originalAlphabetPosition = c - LETTER_A;
                int newAlphabetPosition = (originalAlphabetPosition + key) % ALPHABET_SIZE;
                result.append((char) (LETTER_A + newAlphabetPosition));
            }
        }
        return result.toString();
    }

    public static String decodeMessage(String message, int key) {
        return encodeMessage(message, ALPHABET_SIZE - (key % ALPHABET_SIZE));
    }
}
