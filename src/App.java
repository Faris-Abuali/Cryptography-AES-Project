import java.util.Arrays;

public class App {
    public static int gcd(int a, int b) {
        if (b == 0) {
            return a;
        }
        return gcd(b, a % b);
    }

    public static Integer[] extendedEuclidean(int m, int b) {
        // returns gcd(m,b) and x,y such that gcd(m,b) = xm + yb
        int[] A = { 1, 0, m };
        int[] B = { 0, 1, b };

        Integer[] result = new Integer[2]; // [gcd(m,b), b^-1 mod m]

        int A1, A2, A3;
        A1 = 1;
        A2 = 0;
        A3 = m;

        int B1, B2, B3;
        B1 = 0;
        B2 = 1;
        B3 = b;

        while (true) {
            if (B3 == 0) {
                result[0] = A3; // A3 is the gcd(m,b);
                result[1] = null; // no inverse mod m, because gcd(m,b) is not 1 (m and b are not relatively
                                  // prime)
                return result;
            }
            if (B3 == 1) {
                result[0] = 1; // B3 is the gcd(m,b);
                result[1] = B2; // B2 is the inverse: b^-1 mod m
                return result;
            }

            int q = A3 / B3; // quotient
            // System.out.println("Q = " + q);
            int[] newB = { A1 - q * B1, A2 - q * B2, A3 - q * B3 };

            A = B;
            B = newB;
            A1 = A[0];
            A2 = A[1];
            A3 = A[2];
            B1 = B[0];
            B2 = B[1];
            B3 = B[2];

            // System.out.print(Arrays.toString(A));
            // System.out.print("\t");
            // System.out.print(Arrays.toString(newB));
            // System.out.println("\n");
            // return result;
        }
    } // end method

    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        // System.out.println(gcd(1759, 550));

        // Integer[] result = extendedEuclidean(1759, 550);
        int m,b;
        m = 360;
        b = 7;
        Integer[] result = extendedEuclidean(m, b);
        System.out.println("m = " + m);
        System.out.println("b = " + b);
        System.out.println("[gcd(m,b), b^-1 mod m] = " + Arrays.toString(result));

    }
} // end class App
