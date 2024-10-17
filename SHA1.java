import java.util.Scanner;

public class SHA1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int A, B, C, D, E;

        System.out.print("Enter hexadecimal value for A (32-bit): ");
        A = (int) Long.parseLong(scanner.nextLine(), 16);
        System.out.print("Enter hexadecimal value for B (32-bit): ");
        B = (int) Long.parseLong(scanner.nextLine(), 16);
        System.out.print("Enter hexadecimal value for C (32-bit): ");
        C = (int) Long.parseLong(scanner.nextLine(), 16);
        System.out.print("Enter hexadecimal value for D (32-bit): ");
        D = (int) Long.parseLong(scanner.nextLine(), 16);
        System.out.print("Enter hexadecimal value for E (32-bit): ");
        E = (int) Long.parseLong(scanner.nextLine(), 16);

        // Perform operation: (B AND C) OR (B' AND D)
        int notB = ~B;  // Bitwise NOT of B
        int result = (B & C) | (notB & D);  // (B AND C) OR (B' AND D)

        // Add result to E
        int finalResult = result + E;

        System.out.println("Hexadecimal values entered:");
        System.out.printf("A: 0x%08X\n", A);
        System.out.printf("B: 0x%08X\n", B);
        System.out.printf("C: 0x%08X\n", C);
        System.out.printf("D: 0x%08X\n", D);
        System.out.printf("E: 0x%08X\n", E);
        System.out.printf("Result of (B AND C) OR (B' AND D): 0x%08X\n", result);
        System.out.printf("Final Result (Result + E): 0x%08X\n", finalResult);

        // Perform left circular shift of A by 5 bits
        int circularShiftA = (A << 5) | (A >>> (32 - 5));
        System.out.printf("Left Circular Shift of A by 5 bits: 0x%08X\n", circularShiftA);

        // Add circularShiftA to finalResult
        int combinedResult = circularShiftA + finalResult;
        System.out.printf("Combined Result (Circular Shift A + Final Result): 0x%08X\n", combinedResult);

        // Get a string input from the user
        System.out.print("Enter a string: ");
        String inputString = scanner.nextLine();

        // Handle substring
        String substring;
        if (inputString.length() >= 4) {
            substring = inputString.substring(0, 4);
        } else {
            substring = inputString; // If the string is shorter than 4 characters
        }

        // Convert the substring to hexadecimal
        int hexValue = 0;
        for (char c : substring.toCharArray()) {
            hexValue = (hexValue << 8) + (int) c; // Shift left and add ASCII value
        }

        // Add hexValue to combinedResult
        int finalCombinedResult = combinedResult + hexValue;
        System.out.printf("Hex value of first 4 chars of string: 0x%08X\n", hexValue);
        System.out.printf("Final Combined Result (Combined Result + Hex Value): 0x%08X\n", finalCombinedResult);

        // Define RCON constant
        int RCON = 0x5A827999;

        // Add RCON to finalCombinedResult
        int resultWithRCON = finalCombinedResult + RCON;
        System.out.printf("Result after adding RCON (0x5A827999): 0x%08X\n", resultWithRCON);

        // Perform left circular shift of B by 30 bits
        int circularShiftB = (B << 30) | (B >>> 2);
        System.out.printf("Left Circular Shift of B by 30 bits: 0x%08X\n", circularShiftB);

        // Replacing register values
        B = A;
        A = resultWithRCON;
        E = D;
        D = C;
        C = circularShiftB;

        // Display updated register values
        System.out.println("\nUpdated values after replacements:");
        System.out.printf("A: 0x%08X\n", A);
        System.out.printf("B: 0x%08X\n", B);
        System.out.printf("C: 0x%08X\n", C);
        System.out.printf("D: 0x%08X\n", D);
        System.out.printf("E: 0x%08X\n", E);

        scanner.close();
    }
}
