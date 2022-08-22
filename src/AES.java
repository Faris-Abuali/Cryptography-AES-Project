import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AES {
    public static final byte[] m = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // irreducible polynomial for AES
    Polynomial_GF2_Utils poly = new Polynomial_GF2_Utils();

    String[][] state = {
            { "87", "F2", "4D", "97" },
            { "6E", "4C", "90", "EC" },
            { "46", "E7", "4A", "C3" },
            { "A6", "8C", "D8", "95" }
    };

    String[][] mixColumnsMatrix = {
            { "02", "03", "01", "01" },
            { "01", "02", "03", "01" },
            { "01", "01", "02", "03" },
            { "03", "01", "01", "02" }
    };

    // ---------------- Stage #1: SubBytes (Substitution Box) ----------------
    public void sBox() {
        /*
         * S-Boxes are used to transform the input bits to output bits.
         * 
         * @param input the input bits to be transformed
         * 
         * @return the output bits after transformation
         * 
         */

        // int index = 6;
        // System.out.println(Math.floorMod(index - 4, 9));
        // System.out.println(Math.floorMod(index - 5, 9));
        // System.out.println(Math.floorMod(index - 6, 9));
        // System.out.println(Math.floorMod(index - 7, 9));

        byte[] c = { 0, 0, 1, 1, 0, 0, 0, 1, 1 }; // x063h
        byte[] b = { 0, 1, 0, 0, 0, 1, 0, 1, 0 }; // x08Ah
        byte[] newB = new byte[b.length - 1];

        byte[] bReversed = poly.reverse(Arrays.copyOfRange(b, 1, b.length)); // reversed and length reduced to 8
        byte[] cReversed = poly.reverse(Arrays.copyOfRange(c, 1, c.length)); // reversed and length reduced to 8

        for (int i = 0; i < bReversed.length; i++) {
            newB[i] = (byte) (bReversed[i] ^
                    bReversed[(i + 4) % 8] ^
                    bReversed[(i + 5) % 8] ^
                    bReversed[(i + 6) % 8] ^
                    bReversed[(i + 7) % 8] ^
                    cReversed[i]);
        }

        newB = poly.reverse(newB); // reverse the array [X^7 X^6 X^5 X^4 X^3 X^2 X^1 X^0]

        System.out.println("b = " + Arrays.toString(b));
        System.out.println("newB = " + Arrays.toString(newB));
    }

    // ---------------- Stage #2: ShiftRows ----------------
    public String[][] shiftRows(String[][] state) {
        String[][] newState = new String[4][4];

        newState[0] = state[0]; // The 1st row (number 0) is not altered.
        /**
         * The 1st row (number 0) is not altered.
         * Row number i is shifted left by i-byte circular left shift, i = 1, 2, 3
         */

        // Start from row = 1
        for (int row = 1; row < 4; row++) {
            // Row number i is shifted left by i-byte circular left shift, i = 1, 2, 3
            for (int col = 0; col < 4; col++) {
                newState[row][col] = state[row][(col + row) % 4];
            }
        }
        state = newState;
        return state;
    }

    // ---------------- Stage #3: MixColumns ----------------
    public String[][] mixColumns() {

        String[][] product = new String[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                byte[] cell = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                // each cell is the result of 4 terms XORed
                for (int i = 0; i < 4; i++) {
                    byte[] a = Utils.hexStringToBinArray(mixColumnsMatrix[row][i]);
                    byte[] b = Utils.hexStringToBinArray(state[i][col]);

                    byte[] term = poly.multiply_polynomials_GF2_AES(a, b);

                    cell = poly.xor(cell, term);
                } // end for i
                String hexString = Utils.binArrayToHexString(cell).toUpperCase();
                product[row][col] = hexString;
            } // end for row
        } // end for col

        // Now print the matrix products:
        Utils.printMatrix(product);
        return product;
    }// end method

    public static void main(String[] args) {

        AES aes = new AES();
        // byte[] a = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // a = x^8 + x^4 + x^3 + x + 1
        // byte[] b = { 0, 1, 1, 0, 0, 0, 0, 1, 0 }; // a = x^7 + x^6 + x

        aes.mixColumns();

        // ----- 😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃 -----
        // byte[] m = { 1, 0, 0, 0, 1, 1, 0, 1, 1 };
        // byte[] b = { 0, 1, 0, 0, 1, 0, 1, 0, 1 }; // 0x95h = 1001001001
        // aes.multiplicative_inverse(m, b); // b(x)^-1 mod m(x)
        // ----- 😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃😃 -----

    }
} // end class
