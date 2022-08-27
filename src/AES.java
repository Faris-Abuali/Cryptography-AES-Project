import java.util.Arrays;

public class AES {
    private static final byte[] m = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // irreducible polynomial for AES
    private Polynomial_GF2_Utils poly; // composition
    private String[][] state;
    private String[] RC; // 10 Round Constants, each constant is a byte represented as hex string
    private String[] key = { "0F", "15", "71", "C9", "47", "D9", "E8", "59", "0C", "B7", "AD", "D6", "AF", "7F", "67",
            "98" };
    private Word[] expandedKey; // 44 Words, each word is a column of 4-bytes

    public AES() {
        this(null);
    }

    public AES(String[][] state) {
        this.poly = new Polynomial_GF2_Utils(8);
        this.state = state;
        this.RC = this.generateTenRoundsConstants();
        this.expandedKey = this.keyExpansion();
    }

    private static final String[][] MIX_COLS_MATRIX = {
            { "02", "03", "01", "01" },
            { "01", "02", "03", "01" },
            { "01", "01", "02", "03" },
            { "03", "01", "01", "02" }
    };

    private static final String[][] INV_MIX_COLS_MATRIX = {
            { "0E", "0B", "0D", "09" },
            { "09", "0E", "0B", "0D" },
            { "0D", "09", "0E", "0B" },
            { "0B", "0D", "09", "0E" }
    };

    public void setState(String[][] state) {
        this.state = state;
    }

    public String[][] getState() {
        return this.state;
    }

    public static byte[] getMofX() {
        return m; // the irreducible polynomial for AES
    }

    public Word[] getExpandedKey() {
        return this.expandedKey;
    }

    public String[] getKey() {
        return this.key;
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

        // Special Case (Edge Case): If the input byte is 0 ("00"), it has no inverse:
        if (poly.isZero(b)) {
            return b;
            // Let it pass to the sBoxStage2 as it is. (the inverse of 0 is 0)
        }

        return poly.multiplicative_inverse_mod_m(b); // b(x)^-1 mod m(x)
    } // end method

    public byte[] sBoxStage2(byte[] b) {
        /**
         * Receives byte[] b, which is the output of sBoxStage1: b(x)^-1 mod m(x)
         * and makes the following bit transformation:
         * bi' = bi XOR b(i+4 mod 8) XOR b(i+5 mod 8) XOR b(i+6 mod 8) + b(i+7 mod 8) +
         * ci
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

        // Since all our work is with arrays of length 9, we need to add a 0 to the
        // beginning of the array:
        return Utils.toNineBitsArray(newB);
    } // end method

    public byte[] sBoxSingleByte(byte[] b) {
        byte[] bInverse = this.sBoxStage1(b); // b(x)^-1 mod m(x)
        return this.sBoxStage2(bInverse); // bit transformation (ex: "8A" --> "2A")
    }

    public byte[] invSBoxStage1(byte[] b) {
        /**
         * Receives byte[] b,
         * and makes the following bit transformation:
         * bi' = bi XOR b(i+2 mod 8) XOR b(i+5 mod 8) XOR b(i+7 mod 8) +
         * di
         * ex: {"2A"} --> {"8A"} in Hexadecimal
         * 
         * @return a new byte[] array after bit transformation.
         */
        // ------------------- Bit Transformation -------------------
        byte[] d = { 0, 0, 0, 0, 0, 0, 1, 0, 1 }; // x0{05} hexa
        byte[] newB = new byte[b.length - 1];

        byte[] bReversed = poly.reverse(Arrays.copyOfRange(b, 1, b.length)); // reversed and length reduced to 8
        byte[] dReversed = poly.reverse(Arrays.copyOfRange(d, 1, d.length)); // reversed and length reduced to 8

        for (int i = 0; i < bReversed.length; i++) {
            newB[i] = (byte) (bReversed[(i + 2) % 8] ^
                    bReversed[(i + 5) % 8] ^
                    bReversed[(i + 7) % 8] ^
                    dReversed[i]);
        }

        newB = poly.reverse(newB); // reverse the array [X^7 X^6 X^5 X^4 X^3 X^2 X^1 X^0]

        // System.out.println("b = " + Arrays.toString(b));
        // System.out.println("newB = " + Arrays.toString(newB));

        // Since all our work is with arrays of length 9, we need to add a 0 to the
        // beginning of the array:
        return Utils.toNineBitsArray(newB);
    } // end method

    public byte[] invSBoxStage2(byte[] b) {
        /**
         * Calculates the inverse of the output byte of invSBoxStage1 mod m(x)
         * 
         * param byte[] b: the output byte of invSBoxStage1, to whom the inverse b(x)^-1
         * mod m(x) is to be computed.
         * ex: {"8A"} --> {"95"} in Hexadecimal
         * 
         * @return the inverse: (b(x)^-1 mod m(x))
         */

        // Special Case (Edge Case): If the input byte is 0 ("00"), it has no inverse:
        if (poly.isZero(b)) {
            return b;
            // Let it pass as it is. (the inverse of 0 is 0)
        }

        byte[] ex = Arrays.copyOf(b, b.length + 1);
        return poly.multiplicative_inverse_mod_m(b); // b(x)^-1 mod m(x)
    } // end method

    public byte[] invSBoxSingleByte(byte[] b) {
        byte[] bTransformed = this.invSBoxStage1(b); // bit transformation (ex: "2A" --> "8A")
        return this.invSBoxStage2(bTransformed); // b(x)^-1 mod m(x) (ex: "8A" --> "95")
    }

    // ---------------- Stage #1: SubBytes (Substitution Box) ----------------
    public void subBytes() {
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
                byte[] sBoxOutput = this.sBoxSingleByte(cell);
                this.state[row][col] = Utils.binArrayToHexString(sBoxOutput);
            }
        }
    }// end method sBox

    public void invSubBytes() {
        /*
         * Inverse SubBytes are used to transform the input bits to output bits.
         * 
         * @param input the input bits to be transformed
         * 
         * @return the output bits after transformation
         * 
         */
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                String hexString = this.state[row][col]; // ex: {"2A"}
                byte[] cell = Utils.hexStringToBinArray(hexString); // ex: [0, 0,0,1,0, 1, 0, 1, 0];
                byte[] sBoxOutput = this.invSBoxSingleByte(cell);
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

    public void invShiftRows() {
        String[][] newState = new String[4][4];

        newState[0] = state[0]; // The 1st row (number 0) is not altered.
        /**
         * The 1st row (number 0) is not altered.
         * Row number i is shifted RIGHT by i-byte circular left shift, i = 1, 2, 3
         */

        // Start from row = 1
        for (int row = 1; row < 4; row++) {
            // Row number i is shifted RIGHT by i-byte circular left shift, i = 1, 2, 3
            for (int col = 0; col < 4; col++) {
                newState[row][col] = this.state[row][Math.floorMod((col - row), 4)];
                // row: 1, col: 2
                // newState[1][2] = state[1][(1) % 4] --> state[3][2]
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

    public void invMixColumns() {

        String[][] product = new String[4][4];

        for (int col = 0; col < 4; col++) {
            for (int row = 0; row < 4; row++) {
                byte[] cell = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                // each cell is the result of 4 terms XORed
                for (int i = 0; i < 4; i++) {
                    byte[] a = Utils.hexStringToBinArray(INV_MIX_COLS_MATRIX[row][i]);
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

    // ---------------- Stage #4: AddRoundKey ----------------
    public void addRoundKey(int round) {
        /*
         * AddRoundKey is used to XOR the key with the state.
         * 
         * @param round
         * 
         */
        String[][] currentRoundKey = new String[4][4];

        // --- Take four words from the `expandedKey` and build a 4x4 matrix, each word
        // constitutes a column in the 4x4 matrix ---
        for (int col = 0; col < 4; col++) {
            Word w = this.expandedKey[(round * 4) + col];

            currentRoundKey[0][col] = w.getByte(0);
            currentRoundKey[1][col] = w.getByte(1);
            currentRoundKey[2][col] = w.getByte(2);
            currentRoundKey[3][col] = w.getByte(3);
        }

        // --------------- currentState XOR currentRoundKey ----------------
        for (int i = 0; i < 4; i++) {
            Word w;
            w = poly.xorWord(new Word(currentRoundKey[i]), new Word(this.state[i]));
            this.state[i] = w.getBytes();
        }

        // System.out.println(" --- AddRoundKey Output ----");
        // Utils.printMatrix(this.state);
    }// end method addRoundKey

    // ---------------- Key Expansion -----------------
    public String[] generateTenRoundsConstants() {
        /**
         * 
         * @return array os 10 round constants, each round constant is a string, which
         *         represents a byte written in hexadecimal
         */
        String[] RC = new String[10]; // each element is byte written in hexa
        RC[0] = "01"; // this is the first round constant is defined as "01"

        byte[] two = { 0, 0, 0, 0, 0, 0, 0, 1, 0 }; // X^1 == {02}

        for (int i = 1; i < 10; i++) {
            byte[] previousRC = Utils.hexStringToBinArray(RC[i - 1]);
            byte[] product = poly.multiply_polynomials_GF2_AES(two, previousRC);
            RC[i] = Utils.binArrayToHexString(product);
        }

        // System.out.println(Arrays.toString(RC));
        // this.RC = RC;
        return RC;
    }

    public Word[] keyExpansion() {
        Word[] words = new Word[44];

        // The key is a 16-byte key, same length as the block of plaintext to be
        // encrypted.
        Word temp;

        for (int i = 0; i < 4; i++) {
            // Each four bytes will constitute a word:
            String[] fourBytes = { this.key[i * 4], this.key[i * 4 + 1], this.key[i * 4 + 2], this.key[i * 4 + 3] };
            words[i] = new Word(fourBytes);
        }
        // Now we have filled the first 4 words of the `words` array.
        for (int i = 4; i < 44; i++) {
            temp = words[i - 1];
            if (i % 4 == 0) {
                temp = this.subWord(this.rotateWord(temp));

                /**
                 * The effect of an XOR of a word with Rcon is to only perform an XOR on the
                 * leftmost byte of the word:
                 */
                byte[] leftmostByte = Utils.hexStringToBinArray(temp.getByte(0));
                byte[] rconByte = Utils.hexStringToBinArray(this.RC[i / 4 - 1]);
                byte[] updatedByte = poly.xor(leftmostByte, rconByte);
                temp.setByte(0, Utils.binArrayToHexString(updatedByte));
            }
            words[i] = poly.xorWord(words[i - 4], temp);
        }

        // ------------ print the expanded key: -----------
        // System.out.println("-----Expanded key-----");
        // for (int i = 0; i < words.length; i++) {
        // System.out.println(words[i]);
        // }

        // this.expandedKey = words;
        return words;
    }// end method

    private Word rotateWord(Word w) {
        /**
         * performs a 1-byte circular left shift on a word. This means that an input
         * word [b0, b1, b2, b3] is transformed into [b1, b2, b3, b0].
         */
        String[] rotatedArr = new String[4];

        for (int i = 0; i < w.size() - 1; i++) {
            rotatedArr[i] = w.getByte(i + 1);
        }

        rotatedArr[rotatedArr.length - 1] = w.getByte(0);
        return new Word(rotatedArr);
    }

    private Word subWord(Word w) {
        String[] newWord = new String[4];

        for (int i = 0; i < w.size(); i++) {
            byte[] currentByte = Utils.hexStringToBinArray(w.getByte(i));
            currentByte = this.sBoxSingleByte(currentByte);

            newWord[i] = Utils.binArrayToHexString(currentByte);
        }

        return new Word(newWord);
    }

    public String[][] encrypt() {
        /**
         * Encrypts the plaintext using AES.
         */

        // System.out.println("-----Encryption-----");
        this.addRoundKey(0);

        for (int i = 1; i < 10; i++) {
            this.subBytes();
            this.shiftRows();
            this.mixColumns();
            this.addRoundKey(i);
        }
        // The last round (10th round) is a special case: it does not have mixColumns.
        this.subBytes();
        this.shiftRows();
        this.addRoundKey(10);

        // Utils.printMatrix(this.state);
        return this.state; //this is a 16-Byte block (4x4 String[][] matrix) of ciphertext.
    }// end method encrypt

    public String[][] decrypt() {
        /**
         * Decrypts the ciphertext using AES.
         */
        // System.out.println("-----Decryption-----");

        this.addRoundKey(10);

        for (int i = 9; i > 0; i--) {
            this.invShiftRows();
            this.invSubBytes();
            this.addRoundKey(i);
            this.invMixColumns();
        }
        // The last round (0th round) is a special case: it does not have invMixColumns.
        this.invShiftRows();
        this.invSubBytes();
        this.addRoundKey(0);

        // Utils.printMatrix(this.state);
        return this.state; //this is a 16-Byte block (4x4 String[][] matrix) of plainText.
    }// end method decrypt

} // end class
