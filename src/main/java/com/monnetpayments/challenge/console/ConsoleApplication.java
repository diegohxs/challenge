/* MONNET PAYMENTS: (C)2023 */
package com.monnetpayments.challenge.console;

public class ConsoleApplication {

    public static void main(String[] args) {
        int i = 0;
        while (++i < 100) {
            int n = (int) (Math.random() * 1000000);
            int d = (int) (Math.random() * 10);
            int withAlgorithm1 = calculateWithAlgorithm1(n, d);
            System.out.println("Result Algorithm1: " + withAlgorithm1);
            int withAlgorithm2 = calculateWithAlgorithm2(n, d);
            System.out.println("Result Algorithm2: " + withAlgorithm2);
            if (withAlgorithm1 != withAlgorithm2) {
                System.out.println("Fails for case number: " + i + " -> (" + n + "," + d + ")");
                return;
            }
            System.out.println("No fails for case number: " + i + " -> (" + n + "," + d + ")");
        }
    }

    public static int calculateWithAlgorithm1(int n, int d) {
        if (d < 0 || d > 9) {
            return n;
        }
        if (n == 0) {
            return n;
        }
        if (n % 10 == d) {
            return calculateWithAlgorithm1(n / 10, d);
        } else {
            return calculateWithAlgorithm1(n / 10, d) * 10 + (n % 10);
        }
    }

    public static int calculateWithAlgorithm2(int n, int d) {
        if (d < 0 || d > 9) {
            return n;
        }
        int result = 0;
        int applied = 0;
        for (int i = 0; n > Math.pow(10, i); i++) {
            int pow10 = (int) Math.pow(10, i);
            int nDivPow10 = n / pow10;
            if (nDivPow10 % 10 != d) {
                result = (result + (nDivPow10 % 10) * (int) Math.pow(10, i - applied));
            } else {
                applied++;
            }
        }
        return result;
    }
}
