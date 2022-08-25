public class Utils {
    public static String binArrayToHexString(byte[] binArr) {
        /**
         * Convert binary array to hexadecimal string
         * 
         * @param binArr: binary array of 9 bits (ex: {0, 1, 0, 0, 1, 0, 1, 0, 1})
         * 
         * @return hexadecimal string of 2 chars equivalent to the binArr (ex: "95")
         */

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

    // // ---- This is Extended Euclidean for numbers, not polynomial ----
    // public static Integer[] extendedEuclidean(int m, int b) {
    //     // returns gcd(m,b) and x,y such that gcd(m,b) = xm + yb
    //     int[] A = { 1, 0, m };
    //     int[] B = { 0, 1, b };

    //     Integer[] result = new Integer[2]; // [gcd(m,b), b^-1 mod m]

    //     int A1, A2, A3;
    //     A1 = 1;
    //     A2 = 0;
    //     A3 = m;

    //     int B1, B2, B3;
    //     B1 = 0;
    //     B2 = 1;
    //     B3 = b;

    //     while (true) {
    //         if (B3 == 0) {
    //             result[0] = A3; // A3 is the gcd(m,b);
    //             result[1] = null; // no inverse mod m, because gcd(m,b) is not 1 (m and b are not relatively
    //                               // prime)
    //             return result;
    //         }
    //         if (B3 == 1) {
    //             result[0] = 1; // B3 is the gcd(m,b);
    //             result[1] = B2; // B2 is the inverse: b^-1 mod m
    //             return result;
    //         }

    //         int q = A3 / B3; // quotient
    //         // System.out.println("Q = " + q);
    //         int[] newB = { A1 - q * B1, A2 - q * B2, A3 - q * B3 };

    //         A = B;
    //         B = newB;
    //         A1 = A[0];
    //         A2 = A[1];
    //         A3 = A[2];
    //         B1 = B[0];
    //         B2 = B[1];
    //         B3 = B[2];

    //         // System.out.print(Arrays.toString(A));
    //         // System.out.print("\t");
    //         // System.out.print(Arrays.toString(newB));
    //         // System.out.println("\n");
    //         // return result;
    //     }
    // } // end method


    public static void main(String[] args) {
        byte[] b = Utils.hexStringToBinArray("95");
        System.out.println();
    }
}// end class
