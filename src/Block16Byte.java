public class Block16Byte {
    private String[][] hexMatrix; // matrix of strings, each string represents a byte written in hex
    private int row;
    private int col;
    private int size;

    public Block16Byte() {
        this.hexMatrix = new String[4][4];
        this.fillWithSpaces();  //Fill the matrix with spaces as the default value instead of null
        this.row = 0;
        this.col = 0;
    }

    public Block16Byte(String[][] matrix) {
        this.hexMatrix = matrix;
        this.row = 0;
        this.col = 0;
    }

    public String[][] getHexStringMatrix() {
        return this.hexMatrix;
    }

    public void fillWithSpaces() {
        // Fill the matrix with spaces as the default value instead of null:
        
        // Char: ' ' (space)
        // DECIMAL: 32
        // Hex: 20

        // Char --> DEC --> HEX
        // ' ' --> 32 --> 20
        char space = ' ';
        String hexSpace = Integer.toHexString((int)space);
        for (int row = 0; row <4; row++) {
            for (int col = 0; col <4; col++){
                this.hexMatrix[row][col] = hexSpace;
            }
        }
    }

    public void append(String s) {
        if (size < 16) {
            if (col == 4) {
                col = 0;
                row++;
            }
            this.hexMatrix[col][row] = s;
            col++;
            size++;
        }
        // if (row == 3 && col == 3) {
        //     return; // matrix is full
        // }
        // this.hexMatrix[row][col] = s;
        // if (col <= 3 && row <= 3) {
        //     this.hexMatrix[row][col] = s;
        //     row++;
        // }
    }

    // public String getByte(int index) {
    // return this.hexBytes[index];
    // }

    // public void setByte(int index, String value) {
    // this.hexBytes[index] = value;
    // }

    // public String[] getBytes() {
    // return this.hexBytes;
    // }

    // public int size() {
    // return this.hexBytes.length;
    // }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                // System.out.print(this.hexMatrix[row][col] + " ");
                sb.append(this.hexMatrix[row][col]).append(" ");
            }
            // System.out.println();
            sb.append("\n");
        }
        return sb.toString();
    }

}// end class
