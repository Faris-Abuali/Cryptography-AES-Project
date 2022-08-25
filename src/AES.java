import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class AES {
    private static final byte[] m = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // irreducible polynomial for AES
    private Polynomial_GF2_Utils poly; // composition
    private String[][] state;

    public AES(String[][] state) {
        this.poly = new Polynomial_GF2_Utils(8);
        this.state = state;
    }

    private static final String[][] MIX_COLS_MATRIX = {
            { "02", "03", "01", "01" },
            { "01", "02", "03", "01" },
            { "01", "01", "02", "03" },
            { "03", "01", "01", "02" }
    };

    public String[][] getState() {
        return this.state;
    }
    public static byte[] getMofX() {
        return m;
    }

    public byte[] sBoxStage1(byte[] b) {
        /**
         * Calculates the inverse of the input byte mod m(x)
         * 
         * param byte[] b: the input byte to whom the inverse b(x)^-1 mod m(x) is to be
         * computed.
         * ex: {"95"} --> {"8A"} in Hexadecimal
         * 
         * @return the inverse: (b(x)^-1 mod m(x))
         */
        return poly.multiplicative_inverse_mod_m(b); // b(x)^-1 mod m(x)
    } // end method

    public byte[] sBoxStage2(byte[] b) {
        /**
         * Receives byte[] b, which is the output of sBoxStage1: b(x)^-1 mod m(x)
         * and makes the following bit transformation:
         * bi' = bi XOR b(i+4 mod 8) XOR b(i+5 mod 8) XOR b(i+6 mod 8) + b(i+7 mod 8) +
         * c(i mod 8)
         * ex: {"8A"} --> {"2A"} in Hexadecimal
         * 
         * @return a new byte[] array after bit transformation.
         */
        // ------------------- Bit Transformation -------------------
        byte[] c = { 0, 0, 1, 1, 0, 0, 0, 1, 1 }; // x0{63} hexa
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

        // System.out.println("b = " + Arrays.toString(b));
        // System.out.println("newB = " + Arrays.toString(newB));
        return newB;
    } // end method

    public byte[] sBoxSingleCell(byte[] b) {
        byte[] bInverse = this.sBoxStage1(b); // b(x)^-1 mod m(x)
        return this.sBoxStage2(bInverse); // bit transformation (ex: "8A" --> "2A")
    }

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
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                String hexString = this.state[row][col]; // ex: {"95"}
                byte[] cell = Utils.hexStringToBinArray(hexString); // ex: [0, 1,0,0,1, 0, 1, 0, 1];
                byte[] sBoxOutput = this.sBoxSingleCell(cell);
                this.state[row][col] = Utils.binArrayToHexString(sBoxOutput);
            }
        }
    }// end method sBox

    // ---------------- Stage #2: ShiftRows ----------------
    public void shiftRows() {
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
                newState[row][col] = this.state[row][(col + row) % 4];
                // row: 3, col: 3
                // newState[3][3] = state[3][(3 + 3) % 4] --> state[3][2]
            }
        }
        // state = newState;
        // return state;
        this.state = newState;
    }

    // ---------------- Stage #3: MixColumns ----------------
    public void mixColumns() {

        String[][] product = new String[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                byte[] cell = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                // each cell is the result of 4 terms XORed
                for (int i = 0; i < 4; i++) {
                    byte[] a = Utils.hexStringToBinArray(MIX_COLS_MATRIX[row][i]);
                    byte[] b = Utils.hexStringToBinArray(this.state[i][col]);

                    byte[] term = poly.multiply_polynomials_GF2_AES(a, b);

                    cell = poly.xor(cell, term);
                } // end for i
                String hexString = Utils.binArrayToHexString(cell).toUpperCase();
                product[row][col] = hexString;
            } // end for row
        } // end for col

        // return product;
        this.state = product;
    }// end method
} // end class
