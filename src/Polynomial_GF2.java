import java.util.Arrays;

public class Polynomial_GF2 {
    private static int n = 8; // GF(2^n)
    // private byte[] a = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // a = x^8 + x^4 + x^3 + x
    // + 1
    // private byte[] b = { 0, 1, 1, 0, 0, 0, 0, 1, 0 }; // a = x^7 + x^6 + x

    public void divide_polynomials_GF2(byte[] a, byte[] b) {
        byte[] q = new byte[n]; // quotient
        byte[] r = new byte[n]; // remainder

        int degreeOfResult = degree(a);
        int degreeOfB = degree(b);
        byte[] result = Arrays.copyOf(a, a.length);
        // We keep dividing result by b until result degree(result) is less than
        // degree(b).
        // So, `b` doesn't change. `result` keeps changing until degree(result) <
        // degree(b)

        while (degreeOfResult >= degreeOfB) {
            int difference = degreeOfResult - degreeOfB;
            // Shifting the `b` by `difference` is equivalent to multiplying `b` by
            // X^difference.
            q[(n - 1) - difference] = 1; // q[(n-1) - difference] means X^difference

            byte[] bShifted = shiftLeft(b, difference);
            // It returns new version of b, so it doesn't change the original b

            result = this.xor(result, bShifted);

            degreeOfResult = degree(result);
            // System.out.println("Degree of result: " + degreeOfResult);
        } // end while

        System.out.println("Quotient: " + Arrays.toString(q));
        System.out.println("Remainder: " + Arrays.toString(result));
        return;
    }

    public byte[] GF_2_remainder(byte[] a, byte[] m) {
        // returns the remainder of a mod m

        int degreeOfResult = degree(a);
        int degreeOfM = degree(m);
        byte[] result = Arrays.copyOf(a, a.length);
        // We keep dividing result by b until result degree(result) is less than
        // degree(b).
        // So, `b` doesn't change. `result` keeps changing until degree(result) <
        // degree(b)

        while (degreeOfResult >= degreeOfM) {
            int difference = degreeOfResult - degreeOfM;
            byte[] bShifted = shiftLeft(m, difference);
            // It returns new version of m, so it doesn't change the original m

            result = this.xor(result, bShifted);

            degreeOfResult = degree(result);
            // System.out.println("Degree of result: " + degreeOfResult);
        } // end while

        // System.out.println("Remainder: " + Arrays.toString(result));
        return result;
    } //end method

// ---- Modular Multiplication of Polynomials in Galois Fields ----
    public byte[] multiply_polynomials_GF2(byte[] a, byte[] b, byte[] m) {
        byte[] product = new byte[n+1];

        // In case `a` or `b` is > `m`, we need to reduce them.
        a = GF_2_remainder(a, m);
        b = GF_2_remainder(b, m);

        int deg = degree(m);

        while (isZero(a) == false) {
            // If the least significant bit of `a` is set (is 1), then we add `b` to the `result`
            if (a[a.length - 1] == 1) { 
                product = this.xor(product, b);
                // XOR is equivalent to addition in GF(2^n)
            }
            a = shiftRight(a, 1);
            b = shiftLeft(b, 1);
            // be careful because `b` may exceed the GF(2^n), (its degree reaches `deg`, so we need to reduce it).

            if (degree(b) == deg) {
                b = this.xor(b, m); //reduce `b` so it stays in GF(2^n)
            }
        }// end while

        System.out.println("a: " + Arrays.toString(a));
        System.out.println("b: " + Arrays.toString(b));
        System.out.println("product: " + Arrays.toString(product));
        return product;
    }//end method

    public boolean isZero(byte[] polynomial) {
        for (int i = 0; i < polynomial.length; i++) {
            if (polynomial[i] != 0) {
                return false;
            }
        }
        return true;
    }


    public int degree(byte[] polynomial) {
        for (int i = 0; i < polynomial.length; i++) {
            if (polynomial[i] == 1) {
                return polynomial.length - 1 - i;
            }
        }
        return -1;
    }

    public byte[] shiftLeft(byte[] b, int numberOfShifts) {
        byte[] bShifted = Arrays.copyOf(b, b.length);
        // It returns new version of b, so it doesn't change the original b

        if (numberOfShifts <= 0) {
            return bShifted;
        }

        for (int i = 0; i < b.length - numberOfShifts; i++) {
            bShifted[i] = b[i + numberOfShifts];
        }

        for (int i = b.length - numberOfShifts; i < b.length; i++) {
            bShifted[i] = 0;
        }
        return bShifted;
    }

    public byte[] shiftRight(byte[] b, int numberOfShifts) {
        byte[] bShifted = Arrays.copyOf(b, b.length);
        // It returns new version of b, so it doesn't change the original b

        if (numberOfShifts <= 0) {
            return bShifted;
        }

        for (int i = b.length - 1; i >= numberOfShifts; i--) {
            bShifted[i] = b[i - numberOfShifts];
        }

        for (int i = 0; i < numberOfShifts; i++) {
            bShifted[i] = 0;
        }

        return bShifted;
    }

    public byte[] xor(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length]; // return a new copy

        int i = 0;
        for (byte b : arr1)
            result[i] = (byte) (b ^ arr2[i++]);

        return result;
    }

    public static void main(String[] args) {
        Polynomial_GF2 p = new Polynomial_GF2();
        byte[] a = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // a = x^8 + x^4 + x^3 + x + 1
        byte[] b = { 0, 1, 1, 0, 0, 0, 0, 1, 0 }; // a = x^7 + x^6 + x

        // System.out.println(Arrays.toString(p.shiftLeft(b, 2)));
        // System.out.println(Arrays.toString(p.xor(a, b)));
        // p.divide_polynomials_GF2(a, b);

        // byte[] temp = {0, 1, 0, 0, 1, 1, 1, 1, 1};
        // System.out.println(p.degree(temp));


        // byte[] m = {0, 0, 0, 0, 1, 0, 0, 1, 1};
        // p.GF_2_remainder(a1, m);


// ---- Modular Multiplication of Polynomials in Galois Fields ----
        // byte[] aa = {0, 0, 0, 0, 0, 1, 1, 0, 1};
        // byte[] bb = {0, 0, 0, 0, 0, 0, 1, 1, 0};
        byte[] aa = {0, 0, 0, 0, 0, 0, 0, 1, 1};
        byte[] bb = {0, 0, 0, 0, 0, 0, 0, 1, 1};
        byte[] mm = {1, 0, 0, 0, 1, 1, 0, 1, 1};

        p.multiply_polynomials_GF2(aa, bb, mm);
    }

} // end class
