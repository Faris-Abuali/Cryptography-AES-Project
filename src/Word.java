import java.util.Arrays;

public class Word {
    private String[] hexBytes; // array of strings, each string represents a byte written in hex

    public Word() {
        this.hexBytes = new String[4];
    }

    public Word(String[] fourBytes) {
        this.hexBytes = fourBytes;
        // Each String represents a byte written in hexadecimal
    }

    public String getByte(int index) {
        return this.hexBytes[index];
    }

    public void setByte(int index, String value) {
        this.hexBytes[index] = value;
    }

    public String[] getBytes() {
        return this.hexBytes;
    }

    public int size() {
        return this.hexBytes.length;
    }

    public String toString() {
        return Arrays.toString(this.hexBytes);
    }

}// end class
