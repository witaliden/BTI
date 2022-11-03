import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

public class Lab4_app {
    private static char[][] charTable;
    private static Point[] positions;

    public static void main(String[] args) {
        System.out.println("Szyfrowanie metodą Playfair");

        Scanner sc = new Scanner(System.in);
        System.out.println("\nPodaj wiadomość do zaszyfrowania: ");
        String message = sc.nextLine().toUpperCase();

        System.out.println("Podaj klucz: ");
        String encryptKey = sc.nextLine().toUpperCase();
        createCipherTable(encryptKey);

        System.out.println("Key: " + encryptKey);
        System.out.println("PlainText: " + message);

        //Lab4_Playfair pf_1 = new Lab4_Playfair(encryptKey, message);
        //pf_1.cleanPlayFairKey();
        //pf_1.generateCipherKey();

        String encodedMessage = encryptMessage(encryptKey);
        System.out.println("\nZaszyfrowana wiadomość:");
        System.out.println(encodedMessage);

        System.out.println("\nDeszyfrowana wiadomość:");
        //System.out.println(decodeMessage(encodedMessage, encryptKey));
    }


    // remove repeated characters from the cipher key and replace J with I
    public static String prepareKey(String encryptKey) {
        return encryptKey.replaceAll("j", "i").replaceAll("[^A-Z]", "").chars().distinct().mapToObj(c -> (char) c).toString();
    }


    // function to generate playfair cipher key table
    public static void createCipherTable(String encryptKey) {
        charTable = new char[5][5];
        positions = new Point[26];

        String cleanKey = prepareKey(encryptKey + "ABCDEFGHIJKLMNOPQRSTUVWXYZ");

        // create cipher key table
        for (int i = 0, idx = 0; i < encryptKey.length(); i++) {
            char c = cleanKey.charAt(i);
            if(positions[c - 'A'] == null){
                charTable[idx / 5][idx % 5] = c;
                positions[c - 'A'] = new Point(idx % 5, idx / 5);
                idx++;
            }
            for (int j = 0; j < 5; j++)
                charTable[i][j] = cleanKey.charAt(idx++);
        }
        System.out.println("Playfair Cipher Key Matrix:");

        for (int i = 0; i < 5; i++)
            System.out.println(Arrays.toString(charTable[i]));
    }

    // function to preprocess plaintext
    public static String formatPlainText() {
        StringBuilder message = new StringBuilder();
        int len = message.length();

        for (int i = 0; i < len; i++) {
            // if plaintext contains the character 'j',
            // replace it with 'i'
            if (message.charAt(i) == 'j')
                message.append('i');
            else
                message.append(message.charAt(i));
        }

        // if two consecutive characters are same, then
        // insert character 'x' in between them
        for (int i = 0; i < message.length(); i += 2) {
            if (message.charAt(i) == message.charAt(i + 1))
                message = new StringBuilder(message.substring(0, i + 1) + 'x'
                        + message.substring(i + 1));
        }

        // make the plaintext of even length
        if (len % 2 == 1)
            message.append('x'); // dummy character

        return message.toString();
    }

    // function to group every two characters
    public static String[] formPairs(String message) {
        int len = message.length();
        String[] pairs = new String[len / 2];

        for (int i = 0, cnt = 0; i < len / 2; i++)
            pairs[i] = message.substring(cnt, cnt += 2);

        return pairs;
    }

    // function to get position of character in key table
    public static int[] getCharPos(char ch) {
        int[] keyPos = new int[2];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {

                if (charTable[i][j] == ch) {
                    keyPos[0] = i;
                    keyPos[1] = j;
                    break;
                }
            }
        }
        return keyPos;
    }

    public static String encryptMessage(String encryptKey) {
        String message = formatPlainText();
        String[] msgPairs = formPairs(message);
        String encText = "";

        for (String msgPair : msgPairs) {
            char ch1 = msgPair.charAt(0);
            char ch2 = msgPair.charAt(1);
            int[] ch1Pos = getCharPos(ch1);
            int[] ch2Pos = getCharPos(ch2);

            // if both the characters are in the same row
            if (ch1Pos[0] == ch2Pos[0]) {
                ch1Pos[1] = (ch1Pos[1] + 1) % 5;
                ch2Pos[1] = (ch2Pos[1] + 1) % 5;
            }

            // if both the characters are in the same column
            else if (ch1Pos[1] == ch2Pos[1]) {
                ch1Pos[0] = (ch1Pos[0] + 1) % 5;
                ch2Pos[0] = (ch2Pos[0] + 1) % 5;
            }

            // if both the characters are in different rows
            // and columns
            else {
                int temp = ch1Pos[1];
                ch1Pos[1] = ch2Pos[1];
                ch2Pos[1] = temp;
            }

            // get the corresponding cipher characters from
            // the key matrix
            encText = encText + charTable[ch1Pos[0]][ch1Pos[1]]
                    + charTable[ch2Pos[0]][ch2Pos[1]];
        }

        return encText;
    }
}
