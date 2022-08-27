import java.util.ArrayList;
import java.util.Arrays;

public class Utils {
    public static String binArrayToHexString(byte[] binArr) {
        /**
         * Convert binary array to hexadecimal string
         * 
         * @param binArr: binary array of 9 bits (ex: {0, 1, 0, 0, 1, 0, 1, 0, 1})
         * 
         * @return hexadecimal string of 2 chars equivalent to the binArr (ex: "95")
         */

        // If the array's length < 8, pad it with 0s on the left until it reaches 8:
        if (binArr.length < 8) {
            byte[] newB = new byte[8];
            int diff = 8 - binArr.length;
            for (int i = 0; i < diff; i++) {
                newB[i] = 0;
            }
            for (int i = diff; i < 8; i++) {
                newB[i] = binArr[i - diff];
            }
            binArr = newB;
        }
        // If the array's length > 8, truncate it, take the rightmost 8 bits:
        else if (binArr.length > 8) {
            byte[] truncated = Arrays.copyOfRange(binArr, binArr.length - 8, binArr.length);
            binArr = truncated;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < binArr.length; i++) {
            sb.append(binArr[i]);
        }
        // "010010101"
        String binString = sb.toString();
        // "010010101"

        int deci = Integer.parseInt(binString, 2); // Binary --> Decimal
        String hexString = Integer.toHexString(deci);
        // Make the length of the hex string to be 2 digits:
        while (hexString.length() < 2) {
            hexString = "0" + hexString;
        }
        return hexString.toUpperCase();
    } // end method

    public static byte[] hexStringToBinArray(String strHex) {
        int deci = Integer.parseInt(strHex, 16); // Hexa --> Decimal
        String binString = Integer.toBinaryString(deci);
        // Make the length of the binary string to be 9 bits:
        while (binString.length() < 9) {
            binString = "0" + binString;
        }

        String[] binArrStr = binString.split("");

        byte[] binArr = new byte[binArrStr.length];

        for (int i = 0; i < binArrStr.length; i++) {
            binArr[i] = (byte) Integer.parseInt(binArrStr[i]);
        }

        return binArr;
    } // end method

    public static void printMatrix(String[][] state) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                System.out.print(state[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void printMatrix(byte[][] state) {
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                System.out.print(state[row][col] + " ");
            }
            System.out.println();
        }
    }

    public static void printExpandedKey(Word[] words) {
        for (int i = 0; i < words.length; i++) {
            System.out.println(words[i]);
        }
    }

    public static byte[] toNineBitsArray(byte[] binArr) {
        byte[] newB = new byte[9];

        // If the array's length < 9, pad it with 0s on the left until it reaches 9:
        if (binArr.length < 9) {
            int diff = 9 - binArr.length;
            for (int i = 0; i < diff; i++) {
                newB[i] = 0;
            }
            for (int i = diff; i < 9; i++) {
                newB[i] = binArr[i - diff];
            }
        }
        // If the array's length > 8, truncate it, take the rightmost 8 bits:
        else if (binArr.length > 9) {
            newB = Arrays.copyOfRange(binArr, binArr.length - 9, binArr.length);
        }

        return newB;
    }

    public static Block16Byte[] encodeMessage(String message) {
        /**
         * Encode message to 16-byte 4x4 blocks pf plainTexts:
         * Receives a message (String), splits each 16 chars (byte) of it into a
         * separate
         * block (4x4 matrix) of PlainText. Each 4x4 matrix will then be encrypted
         * separately.
         * 
         * @param message: String to be encrypted
         * @return array of 16-byte 4x4 blocks of PlainText
         *
         */

        // Each block of plaintext is 16-Bytes (16 characters)
        int numberOfBlocks = (int) Math.ceil(message.length() / 16.0);
        // ArrayList<Block16Byte> plainTexts = new ArrayList<Block16Byte>();
        Block16Byte[] plainTexts = new Block16Byte[numberOfBlocks];
        for (int i = 0; i < numberOfBlocks; i++) {
            plainTexts[i] = new Block16Byte();
        }

        for (int i = 0; i < message.length(); i++) {
            int deciASCII = (int) message.charAt(i);
            String hexASCII = Integer.toHexString(deciASCII);

            plainTexts[i / 16].append(hexASCII);
        }

        // // ---- Now print the Blocks of PlainTexts -------------
        // for (int i = 0; i < plainTexts.length; i++) {
        // System.out.println("PlainText " + i + ":\n" + plainTexts[i]);
        // }

        return plainTexts; // return array of PlainText objects
    }

    public static String decodePlainText(Block16Byte plainText) {
        /**
         * Encode message to 16-byte 4x4 blocks pf plainTexts:
         * Receives a Block16Byte of plainText, converts each byte that is written in hexadecimal ASCII to its corresponding character, then appends it to a StringBuilder.
         * 
         * @param plainText: Block16Byte t(4x4 matrix)
         * @return decoded string.
         *
         */
        String[][] m = plainText.getHexStringMatrix();

        StringBuilder decodedMessage = new StringBuilder();
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                // HEX --> DEC --> Char
                int deciASCII = Integer.parseInt(m[col][row], 16); // HEX --> DEC
                char c = (char) deciASCII; // DEC --> Char
                decodedMessage.append(c);
            }
        }

        // return decodedMessage.toString().trim();
        return decodedMessage.toString();
    }

    // // ---- This is Extended Euclidean for numbers, not polynomial ----
    // public static Integer[] extendedEuclidean(int m, int b) {
    // // returns gcd(m,b) and x,y such that gcd(m,b) = xm + yb
    // int[] A = { 1, 0, m };
    // int[] B = { 0, 1, b };

    // Integer[] result = new Integer[2]; // [gcd(m,b), b^-1 mod m]

    // int A1, A2, A3;
    // A1 = 1;
    // A2 = 0;
    // A3 = m;

    // int B1, B2, B3;
    // B1 = 0;
    // B2 = 1;
    // B3 = b;

    // while (true) {
    // if (B3 == 0) {
    // result[0] = A3; // A3 is the gcd(m,b);
    // result[1] = null; // no inverse mod m, because gcd(m,b) is not 1 (m and b are
    // not relatively
    // // prime)
    // return result;
    // }
    // if (B3 == 1) {
    // result[0] = 1; // B3 is the gcd(m,b);
    // result[1] = B2; // B2 is the inverse: b^-1 mod m
    // return result;
    // }

    // int q = A3 / B3; // quotient
    // // System.out.println("Q = " + q);
    // int[] newB = { A1 - q * B1, A2 - q * B2, A3 - q * B3 };

    // A = B;
    // B = newB;
    // A1 = A[0];
    // A2 = A[1];
    // A3 = A[2];
    // B1 = B[0];
    // B2 = B[1];
    // B3 = B[2];

    // // System.out.print(Arrays.toString(A));
    // // System.out.print("\t");
    // // System.out.print(Arrays.toString(newB));
    // // System.out.println("\n");
    // // return result;
    // }
    // } // end method

}// end class
