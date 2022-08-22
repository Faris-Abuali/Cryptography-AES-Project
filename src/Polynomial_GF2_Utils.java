import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Polynomial_GF2_Utils {
    private int n; // GF(2^n)

    Polynomial_GF2_Utils() {
        this.n = 8; // default is GF(2^n)
    }
    
    Polynomial_GF2_Utils(int n) {
        this.n = n; //GF(2^n)
    }

    public HashMap<String, byte[]> divide_polynomials_GF2(byte[] a, byte[] b) {

        byte[] q = new byte[n + 1]; // quotient // [0,0,0,0,0,0,0,1,0]
        byte[] remainder = Arrays.copyOf(a, a.length); // [0, 0, ...]
        // byte[][] toBeReturned = new byte[2][]; // array of 2 arrays: q and remainder
        HashMap<String, byte[]> toBeReturned = new HashMap<String, byte[]>();

        int degreeOfRemainder = degree(a);
        int degreeOfB = degree(b);

        // We keep dividing remainder by b until remainder degree(remainder) is less
        // than
        // degree(b).
        // So, `b` doesn't change. `remainder` keeps changing until degree(remainder) <
        // degree(b)

        while (degreeOfRemainder >= degreeOfB) {
            int difference = degreeOfRemainder - degreeOfB;
            // Shifting the `b` by `difference` is equivalent to multiplying `b` by
            // X^difference.
            q[q.length - 1 - difference] = 1; // q[(n-1) - difference] means X^difference

            byte[] bShifted = shiftLeft(b, difference);
            // It returns new version of b, so it doesn't change the original b

            remainder = this.xor(remainder, bShifted);

            degreeOfRemainder = degree(remainder);
            // System.out.println("Degree of remainder: " + degreeOfRemainder);
        } // end while

        // System.out.println("Quotient: " + Arrays.toString(q));
        // System.out.println("Remainder: " + Arrays.toString(remainder));

        toBeReturned.put("q", q);
        toBeReturned.put("remainder", remainder);

        return toBeReturned; // {q, remainder}
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
    } // end method

    // ---- Modular Multiplication of Polynomials in Galois Fields ----
    public byte[] multiply_polynomials_GF2(byte[] a, byte[] b, byte[] m) {
        byte[] product = new byte[n + 1];

        // In case `a` or `b` is > `m`, we need to reduce them.
        a = GF_2_remainder(a, m);
        b = GF_2_remainder(b, m);

        int deg = degree(m);

        while (isZero(a) == false) {
            // If the least significant bit of `a` is set (is 1), then we add `b` to the
            // `result`
            if (a[a.length - 1] == 1) {
                product = this.xor(product, b);
                // XOR is equivalent to addition in GF(2^n)
            }
            a = shiftRight(a, 1);
            b = shiftLeft(b, 1);
            // be careful because `b` may exceed the GF(2^n), (its degree reaches `deg`, so
            // we need to reduce it).

            if (degree(b) == deg) {
                b = this.xor(b, m); // reduce `b` so it stays in GF(2^n)
            }
        } // end while

        // System.out.println("a: " + Arrays.toString(a));
        // System.out.println("b: " + Arrays.toString(b));
        // System.out.println("product: " + Arrays.toString(product));
        return product;
    }// end method

    public byte[] multiply_polynomials_GF2_AES(byte[] a, byte[] b) {
        /**
         * @param a polynomial in GF(2^8)
         * 
         * @param b polynomial in GF(2^8)
         * 
         * return product of a and b in GF(2^8) where m(x) = x^8 + x^4 + x^3 + x + 1; because this is the m(x) that is used in AES
         * 
         */
        byte[] m = { 1, 0, 0, 0, 1, 1, 0, 1, 1 }; // irreducible polynomial for AES

        return multiply_polynomials_GF2(a, b, m);
    }

    public boolean isZero(byte[] polynomial) {
        for (int i = 0; i < polynomial.length; i++) {
            if (polynomial[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isEqual(byte[] polynomial, byte[] target) {
        return Arrays.equals(polynomial, target);
        // if (polynomial.length != target.length) {
        // return false;
        // }
        // for (int i = 0; i < polynomial.length; i++) {
        // if (polynomial[i] != target[i]) {
        // return false;
        // }
        // }
        // return true;
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
        /*
         * Takes two polynomials of the same length and XOR them.
         * 
         * @param arr1 polynomial
         * 
         * @param arr2 another polynomial of the same length
         * 
         * @return (arr1 XOR arr2)
         * 
         */

        byte[] result = new byte[arr1.length]; // return a new copy

        int i = 0;
        for (byte b : arr1)
            result[i] = (byte) (b ^ arr2[i++]);

        return result;
    }

    public byte[][] multiplicative_inverse(byte[] m, byte[] b) {

        /*
         * Using the extended Euclidean algorithm, will find the multiplicative inverse
         * of b(x) mod m(x)
         * 
         * @param m the prime polynomial m(x) = X^8 + X^4 + X^3 + X + 1
         * 
         * @param b the polynomial for which we want to find the multiplicative inverse
         * mod m
         * 
         * @return the modular multiplicative inverse of b(x) mod m(x)
         * 
         */

        byte[] zeroPolynomial = new byte[n + 1]; // { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] onePolynomial = new byte[n + 1];
        onePolynomial[onePolynomial.length - 1] = 1; // { 0, 0, 0, 0, 0, 0, 0, 0, 1 };

        Map<String, byte[]> table = new HashMap<String, byte[]>();
        // Initially: A1 = 1, A2 = 0, A3 = m
        table.put("A1", Arrays.copyOf(onePolynomial, n + 1));
        table.put("A2", Arrays.copyOf(zeroPolynomial, n + 1));
        table.put("A3", m);

        // Initially: B1 = 0, B2 = 1, B3 = b
        table.put("B1", Arrays.copyOf(zeroPolynomial, n + 1));
        table.put("B2", Arrays.copyOf(onePolynomial, n + 1));
        table.put("B3", b);

        // `result` is array of 2 arrays: [gcd(m,b), b^-1 mod m]
        byte[][] result = new byte[2][]; // [gcd(m,b), b^-1 mod m]

        while (true) {
            if (Arrays.equals(table.get("B3"), zeroPolynomial)) {
                result[0] = table.get("A3"); // A3 is the gcd(m(x),b(x));
                result[1] = null; // no inverse mod m, because gcd(m(x),b(x)) is not 1 (m and b are not relatively
                                  // prime.
                break;
            }
            if (Arrays.equals(table.get("B3"), onePolynomial)) {
                result[0] = table.get("B3"); // B3 is the gcd(m(x),b(x));
                result[1] = table.get("B2"); // B2 is the inverse: b(x)^-1 mod m(x)
                break;
            }

            // q = Quotient of A3(x) / B3(x)
            HashMap<String, byte[]> QuotientRemainder = this.divide_polynomials_GF2(
                    table.get("A3"),
                    table.get("B3"));

            byte[] q = QuotientRemainder.get("q");
            byte[] r = QuotientRemainder.get("remainder"); // Remainder of A3(x) / B3(x)

            // Remember: in GF(2), addition and subtraction are the same operation
            // equivalent to XOR

            // B3(x)' = A3(x) - q * B3(x) 👇
            byte[] newB3 = r; // it has already been calculated above.

            // B2(x)' = A2(x) - q * B2(x) 👇
            byte[] newB2 = this.xor(
                    table.get("A2"),
                    this.multiply_polynomials_GF2_AES(q, table.get("B2")));

            // B1(x)' = A1(x) - q * B1(x) 👇
            byte[] newB1 = this.xor(
                    table.get("A1"),
                    this.multiply_polynomials_GF2_AES(q, table.get("B1")));

            // Update the A's
            table.put("A3", table.get("B3"));
            table.put("A2", table.get("B2"));
            table.put("A1", table.get("B1"));

            // Update the B's
            table.put("B3", newB3);
            table.put("B2", newB2);
            table.put("B1", newB1);

            // for (Maaes.Entry<String, byte[]> entry : table.entrySet()) {
            // System.out.println(entry.getKey() + " = " +
            // Arrays.toString(entry.getValue()));
            // }

        } // end while

        System.out.println("gcd(m,b) = " + Arrays.toString(result[0]));
        System.out.println("b^-1 mod m = " + Arrays.toString(result[1]));
        return result;
    } // end method

    public byte[] reverse(byte[] arr) {
        /*
         * Reverses an array of bytes
         * 
         * @param arr the array of bytes to reverse
         * 
         * @return the reversed array of bytes
         * 
         */
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[arr.length - 1 - i];
        }
        return result;
    }
    
}// end class
