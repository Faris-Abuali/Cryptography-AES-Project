import java.util.Arrays;

public class TestAll {
    public static void main(String[] args) {

        // ------ Make instance of AES class to use it for encryption/decryption: ------
        AES aes = new AES();
        System.out.println("10-byte Key: " + Arrays.toString(aes.getKey()));

        // This is the message to be encrypted: 👇
        String message = "Faris Hatem Tawfiq Abu Ali 201810";
        System.out.println("-----------------\nOriginal message to be encrypted:\n-----------------");
        System.out.println(message);

        // ------ Encoding Process -----
        // Encode `message` to 16-byte 4x4 blocks pf plainTexts: 👇
        Block16Byte[] plainTexts = Utils.encodeMessage(message);

        System.out.println("-----------------\nBlocks of Plaintexts before encryption:\n-----------------");
        for (int i = 0; i < plainTexts.length; i++) {
            System.out.println("Plaintext " + i + ":\n" + plainTexts[i]);
        }

        // Make another array of the same length to store the Ciphers resulting from
        // encryption:
        Block16Byte[] cipherTexts = new Block16Byte[plainTexts.length];

        Block16Byte[] decryptedCiphers = new Block16Byte[plainTexts.length];

        // ------ Encryption Process -----
        for (int i = 0; i < plainTexts.length; i++) {
            // Set the current block of PlainText to be encrypted: 👇
            aes.setState(plainTexts[i].getHexStringMatrix()); // Block of 16-Bytes
            // Invoke the encrypt() method to encrypt the current block: 👇
            String[][] cipher = aes.encrypt();
            // Store the encrypted block (Cipher) in the Block16Byte[] cipherTexts array: 👇
            cipherTexts[i] = new Block16Byte(cipher); // store the cipher in cipherTexts array.
        }

        System.out.println("-----------------\nBlocks of Ciphertexts before encryption:\n-----------------");
        for (int i = 0; i < cipherTexts.length; i++) {
            System.out.println("cipherText " + i + ":\n" + cipherTexts[i]);
        }

        // ------ Decryption Process -----
        for (int i = 0; i < cipherTexts.length; i++) {
            // Set the current block of cipherText to be decrypted: 👇
            aes.setState(cipherTexts[i].getHexStringMatrix()); // Block of 16-Bytes
            // Invoke the encrypt() method to encrypt the current block: 👇
            String[][] decryptedCipher = aes.decrypt();
            // Store the decrypted block in the Block16Byte[] decryptedCiphers array: 👇
            decryptedCiphers[i] = new Block16Byte(decryptedCipher); // store the decryptedCipher in decryptedCiphers
                                                                    // array.
        }

        System.out.println("-----------------\nBlocks of Decrypted Ciphers after encryption:\n-----------------");
        for (int i = 0; i < decryptedCiphers.length; i++) {
            System.out.println("decryptedCipher " + i + ":\n" + decryptedCiphers[i]);
        }

        // ------ Decoding Process -----
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < decryptedCiphers.length; i++) {
            String decodedBlock = Utils.decodePlainText(decryptedCiphers[i]);
            sb.append(decodedBlock);
        }

        // Print the decoded message: 👇
        System.out.println("The decrypted cipher message after decoding:");
        System.out.println(sb.toString());
    }// end main
} // end class
