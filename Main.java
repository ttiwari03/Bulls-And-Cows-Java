package bullscows;

import java.util.*;

/*
 *  This program prepare a secret code based on given length and possible symbols.
 *  Player try to guess the right code based on stats.
 *  Cow = Character in code but not guessed at right position
 *  Bull = Character in the code and guessed at right position
 *
 *  @author   - Trapti Tiwari
 *  @email    - traptit1@yahoo.com
 *  @linkedin - https://www.linkedin.com/in/tiwari-trapti/
 */

public class Main {
    private static final Scanner readIp = new Scanner(System.in);
    private static final String symbolSet = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static int cow = 0;
    private static int bull = 0;

    public static void main(String[] args) {
        //  Take length of secret code you want to be prepared,
        //  and check for its validity.
        System.out.println("Input the length of the secret code:");
        String len = readIp.nextLine();
        final int codeSize = checkCodeLength(len);


        if (codeSize != 0) {
            //  Total symbols are 36 (0-9, a-z).
            System.out.println("Input the number of possible symbols in the code:");
            String symbolCount = readIp.nextLine();
            final int charSetLength = checkSymbolCount(symbolCount, codeSize);    //  Check if given code size and symbol numbers are valid for code generation.

            if (charSetLength != 0) {
                String code = generateSecretCode(codeSize, charSetLength);
                char[] codeArray = code.toCharArray();

                //  Print message about generated secret code.
                printMessage(codeSize, charSetLength);

                System.out.println("Okay, let's start a game!");
                int turn = 0;
                boolean isGuessCorrect = false;

                //  Game continue until player guess correct secret code.
                while (!isGuessCorrect) {
                    //  Initialize game parameter
                    cow = 0;
                    bull = 0;
                    turn++;
                    System.out.println("Turn " + turn + ":");

                    //  Take guess for possible secret code
                    char[] guessArray = readIp.nextLine().toCharArray();

                    //  If guessed code have equal length as prepared secret code
                    //  Guess is analyzed and stats are show about right/wrong character.
                    if (checkGuess(guessArray, codeSize)) {
                        isGuessCorrect = analyzeGuess(codeArray, guessArray, codeSize);
                        constructGrade();
                    } else {
                        break;
                    }
                }
                if (isGuessCorrect) {
                    System.out.println("Congratulations! You guessed the secret code.");
                }
            }
        }
    }

    /*
        Check if guessed code is equal length to prepared secret code
        @input  -   guessed code, prepared secret code size
        @output -   guessed code length and prepared code lenth is equal or not
     */
    private static boolean checkGuess(char[] guessArray, int codeSize)
    {
        return guessArray.length == codeSize;
    }

    /*
        Check if length given by user is valid and sufficient to generate secret code
        @input  -   length given by user
        @output -   actual length of which secret code can be generated
     */
    private static int checkCodeLength(String len) {
        int codeLen = 0;
        try {
            codeLen = Integer.parseInt(len);

            if (codeLen > 36 || codeLen == 0) {
                System.out.printf("Error: can't generate a secret number with a length of %d because there aren't enough unique symbols.", codeLen);
                System.out.println();
                codeLen = 0;
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + len + "\" isn't a valid number.");
        }
        return codeLen;
    }

    /*
        Check if the code length given by user and number of symbol wanted in secret code are compatible.
        @input  -   symbol count , secret code size required
        @output -   actual symbol count possible
     */
    private static int checkSymbolCount(String symbolCount, int codeSize) {
        int symbolLen = 0;
        try {
            symbolLen = Integer.parseInt(symbolCount);
            if (symbolLen > symbolSet.length()) {
                System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
                symbolLen = 0;
            }
            if (symbolLen < codeSize) {
                System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", codeSize, symbolLen);
                System.out.println();
                symbolLen = 0;
            }
        } catch (Exception e) {
            System.out.println("Error: \"" + symbolCount + "\" isn't a valid number.");
        }
        return symbolLen;
    }

    /*
        Print message about prepared secret code.
     */
    private static void printMessage(int codeSize, int charset) {
        StringBuilder msg = new StringBuilder("The secret is prepared: ");

        msg.append("*".repeat(Math.max(0, codeSize)));
        msg.append(" (0-9");

        if (charset > 10) {
            msg.append(", ");
            msg.append(String.format("%c-%c", symbolSet.charAt(10), symbolSet.charAt(charset - 1)));
        }
        msg.append(").");
        System.out.println(msg);
    }

    /*
        Generate random secret code based of required length and symbols.
     */
    private static String generateSecretCode(int codeSize, int charset) {
        StringBuilder randomNumbers = new StringBuilder();
        while (true) {
            int randomNum = (int) (Math.random() * charset);
            char temp = symbolSet.charAt(randomNum);
            if (randomNumbers.indexOf(String.valueOf(temp)) == -1) {
                randomNumbers.append(temp);
                if (randomNumbers.length() == codeSize) {
                    return randomNumbers.toString();
                }
            }
        }
    }

    /*
        Return if guessed code is same as prepared code or not
        Bull means guessed character is correct and at right position
        Cow means guessed character is correct but is not guessed at right position
     */
    private static boolean analyzeGuess(char[] codeArray, char[] guessArray, int codeSize) {
        for (int i = 0; i < guessArray.length; i++) {
            for (int j = 0; j < codeArray.length; j++) {
                if (guessArray[i] == codeArray[j] && i == j) {
                    bull++;
                    break;
                } else if (guessArray[i] == codeArray[j]) {
                    cow++;
                    break;
                }
            }
        }
        return bull == codeSize;
    }

    /*
        Generate output about game stats and show to the player after every turn
     */
    private static void constructGrade() {
        StringBuilder grade = new StringBuilder("Grade: ");
        if (cow == 0 && bull == 0) {
            grade.append("None");
        } else {
            if (cow == 1) {
                grade.append(String.format("%d cow", cow));
            }
            if (cow > 1) {
                grade.append(String.format("%d cows", cow));
            }
            if (cow != 0 && bull != 0) {
                grade.append(" and ");
            }
            if (bull == 1) {
                grade.append(String.format("%d bull", bull));
            }
            if (bull > 1) {
                grade.append(String.format("%d bulls", bull));
            }
        }
        grade.append(".");
        System.out.println(grade);
    }
}