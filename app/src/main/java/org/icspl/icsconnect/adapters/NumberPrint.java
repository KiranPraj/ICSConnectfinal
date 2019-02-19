package org.icspl.icsconnect.adapters;

public class NumberPrint {

    public static void main(String[] args) {

        int i = 0;

        for (int j = 5; j >= 1; j--) {
            for (int k = 5; k >= j; k--) {
                System.out.print(k);
            }
            System.out.println();

        }
        for (int h = 2; h <= 5; h++) {
            for (int l = 5; h <= l; l--) {
                System.out.print(l);

            }
            System.out.println();
        }

        int[] arr = {1, 24, 6, 1, 8, 6, 48, 8};
        for (int l : arr) {
            for (int a = 0; a <= arr.length; a--) {

            }
        }
    }

}
