import java.util.Arrays;

public class TestDrive {
    public static void main(String[] args) {
        // String[][] state = {
        // { "87", "F2", "4D", "97" },
        // { "6E", "4C", "90", "EC" },
        // { "46", "E7", "4A", "C3" },
        // { "A6", "8C", "D8", "95" }
        // };
        String[][] state = {
                { "01", "89", "FE", "76" },
                { "23", "AB", "DC", "54" },
                { "45", "CD", "BA", "32" },
                { "67", "EF", "98", "10" }
        };

        
        AES aes = new AES(state);

        // System.out.println("10-byte Key: " + Arrays.toString(aes.getKey()));
        // System.out.println("Plaintext before encryption: ");
        // Utils.printMatrix(aes.getState());
        // aes.encrypt();
        // aes.decrypt();

        System.out.println(" ----------------- Plaintext -----------------");
        Utils.printMatrix(aes.getState());

        // Utils.printExpandedKey(aes.getExpandedKey());
        // aes.addRoundKey(1);

        // Utils.printMatrix(aes.getState());
        // aes.encrypt();
        // aes.decrypt();


        System.out.println(" ----------------- SubBytes Output -----------------");
        aes.subBytes();
        Utils.printMatrix(aes.getState());

        // System.out.println(" ----------------- InvSubBytes Output -----------------");
        // aes.invSubBytes();
        // Utils.printMatrix(aes.getState());

        System.out.println(" ----------------- ShiftRows Output -----------------");
        aes.shiftRows();
        Utils.printMatrix(aes.getState());

        // System.out.println(" ----------------- InvShiftRows Output -----------------");
        // aes.invShiftRows();
        // Utils.printMatrix(aes.getState());

        System.out.println(" ----------------- MixColumns Output -----------------");
        aes.mixColumns();
        Utils.printMatrix(aes.getState());

        // System.out.println(" ----------------- InvMixColumns Output-----------------");
        // aes.invMixColumns();
        // Utils.printMatrix(aes.getState());

    } // end main
}// end class
