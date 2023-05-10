import java.awt.*;
import java.util.Scanner;

public class Lab4_playfair {
    private static char[][] charTable;
    private static Point[] positions;

    public static void main(String[] args) {
        System.out.println("Szyfrowanie metodą Playfair");

        Scanner sc = new Scanner(System.in);
        String encryptKey = userInteraction("Podaj klucz (min 4 litery): ", sc, 4);
        String message = userInteraction("Podaj wiadomość do zaszyfrowania: ", sc, 1);
        String replaceJtoI = userInteraction("Zamienić J na I? t/n: ", sc, 1);

        boolean jToIstate = replaceJtoI.equalsIgnoreCase("t");

        createTable(encryptKey, jToIstate);

        String encodedText = encode(cleanText(message, jToIstate));

        System.out.printf("%nZaszyfrowana wiadomość: %n%s%n", encodedText);
        System.out.printf("%nDezszyfrowana wiadomość: %n%s%n", decode(encodedText));
    }

    private static String userInteraction(String promptText, Scanner sc, int minLength) {
        String inputResult;
        do {
            System.out.print(promptText);
            inputResult = sc.nextLine().trim();
        } while (inputResult.length() < minLength);
        return inputResult;
    }

    private static String cleanText(String s, boolean changeJtoI) {
        s = s.toUpperCase().replaceAll("[^A-Z]", "");
        return changeJtoI ? s.replace("J", "I") : s.replace("Q", "");
    }

    private static void createTable(String key, boolean changeJtoI) {
        charTable = new char[5][5];
        positions = new Point[26];

        String s = cleanText(key + "ABCDEFGHIJKLMNOPQRSTUVWXYZ", changeJtoI);

        int len = s.length();
        for (int i = 0, j = 0; i < len; i++) {
            j = getJ(s, i, j, positions, charTable);
        }
    }

    static int getJ(String s, int i, int j, Point[] positions, char[][] charTable) {
        char c = s.charAt(i);
        if (positions[c - 'A'] == null) {
            charTable[j / 5][j % 5] = c;
            positions[c - 'A'] = new Point(j % 5, j / 5);
            j++;
        }
        return j;
    }

    private static String encode(String s) {
        StringBuilder sb = new StringBuilder(s);

        for (int i = 0; i < sb.length(); i += 2) {
            if (i == sb.length() - 1)
                sb.append(sb.length() % 2 == 1 ? 'X' : "");

            else if (sb.charAt(i) == sb.charAt(i + 1))
                sb.insert(i + 1, 'X');
        }
        return runChiffering(sb, 1);
    }

    private static String decode(String s) {
        return runChiffering(new StringBuilder(s), 4);
    }

    private static String runChiffering(StringBuilder text, int direction) {
        int len = text.length();
        for (int i = 0; i < len; i += 2) {
            char a = text.charAt(i);
            char b = text.charAt(i + 1);

            int row1 = positions[a - 'A'].y;
            int row2 = positions[b - 'A'].y;
            int col1 = positions[a - 'A'].x;
            int col2 = positions[b - 'A'].x;

            if (row1 == row2) {
                col1 = (col1 + direction) % 5;
                col2 = (col2 + direction) % 5;

            } else if (col1 == col2) {
                row1 = (row1 + direction) % 5;
                row2 = (row2 + direction) % 5;

            } else {
                int tmp = col1;
                col1 = col2;
                col2 = tmp;
            }

            text.setCharAt(i, charTable[row1][col1]);
            text.setCharAt(i + 1, charTable[row2][col2]);
        }
        return text.toString();
    }
}
