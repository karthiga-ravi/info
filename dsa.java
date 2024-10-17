import java.util.*;
import java.math.*;

public class dsa {
    private static BigInteger p, q, g, x, y, r, s, hm; // Global variables for key and signature

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. Key Generation");
            System.out.println("2. Signature Generation");
            System.out.println("3. Signature Verification");
            System.out.println("4. Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    keyGeneration(sc);
                    break;
                case 2:
                    signatureGeneration(sc);
                    break;
                case 3:
                    verification(sc);
                    break;
                case 4:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Key Generation
    private static void keyGeneration(Scanner sc) {
        BigInteger one = BigInteger.ONE;
        // Get prime p value
        System.out.print("Enter prime number p: ");
        p = sc.nextBigInteger();
        // Get q value ensuring p-1 is divisible by q
        do {
            System.out.print("Enter q value: ");
            q = sc.nextBigInteger();
        } while (!p.subtract(one).mod(q).equals(BigInteger.ZERO));
        // Generate g using the formula g = h^((p-1)/q) mod p
        BigInteger h;
        do {
            System.out.print("Enter value of h (1 < h < p-1): ");
            h = sc.nextBigInteger();
        } while (h.compareTo(one) <= 0 || h.compareTo(p.subtract(one)) >= 0);
        g = h.modPow(p.subtract(one).divide(q), p);
        System.out.println("Generated g: " + g);
        // Get private key x ensuring it is less than q
        do {
            System.out.print("Enter x value (private key): ");
            x = sc.nextBigInteger();
        } while (x.compareTo(q) >= 0);
        // Calculate public key y
        y = g.modPow(x, p);
        // Print private and public keys
        System.out.println("Private key: " + x);
        System.out.println("Public key: " + y);
    }

    // Signature Generation
    private static void signatureGeneration(Scanner sc) {
        if (p == null || q == null || g == null || x == null || y == null) {
            System.out.println("Keys not generated. Please generate keys first.");
            return;
        }
        // Get k value ensuring it is less than q
        BigInteger k;
        do {
            System.out.print("Enter k value (must be less than q): ");
            k = sc.nextBigInteger();
        } while (k.compareTo(q) >= 0);
        // Get hash value
        System.out.print("Enter h(M) value (hash of message): ");
        hm = sc.nextBigInteger();
        // Calculate r
        r = g.modPow(k, p).mod(q);
        // Calculate s
        BigInteger ki = k.modInverse(q);
        s = ki.multiply(hm.add(x.multiply(r))).mod(q);
        // Print signature (r, s)
        System.out.println("Signature r: " + r);
        System.out.println("Signature s: " + s);
    }

    // Signature Verification
    private static void verification(Scanner sc) {
        if (p == null || q == null || g == null || y == null || r == null || s == null || hm == null) {
            System.out.println("Signature or keys not generated. Please generate keys and signature first.");
            return;
        }
        // Calculate w
        BigInteger w = s.modInverse(q);
        // Calculate u1 and u2
        BigInteger u1 = hm.multiply(w).mod(q);
        BigInteger u2 = r.multiply(w).mod(q);
        // Calculate v
        BigInteger v = (g.modPow(u1, p).multiply(y.modPow(u2, p))).mod(p).mod(q);
        // Print verification result
        System.out.println("V calculated: " + v);
        System.out.println("R calculated: " + r);
        if (v.equals(r)) {
            System.out.println("Signature verified, correct.");
        } else {
            System.out.println("Signature verification failed.");
        }
    }
}