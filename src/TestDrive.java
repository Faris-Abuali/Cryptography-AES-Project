public class TestDrive {
    public static void main(String[] args) {
        String[][] state = {
                { "87", "F2", "4D", "97" },
                { "6E", "4C", "90", "EC" },
                { "46", "E7", "4A", "C3" },
                { "A6", "8C", "D8", "95" }
        };

        AES aes = new AES(state);

        System.out.println(" ----------------- Plaintext -----------------");
        Utils.printMatrix(aes.getState());

        aes.sBox();
        System.out.println(" ----------------- S-Box Output -----------------");
        Utils.printMatrix(aes.getState());

        aes.shiftRows();
        System.out.println(" ----------------- ShiftRows Output -----------------");
        Utils.printMatrix(aes.getState());

        System.out.println(" ----------------- MixColumns Output -----------------");
        aes.mixColumns();
        Utils.printMatrix(aes.getState());

    } // end main
}// end class
