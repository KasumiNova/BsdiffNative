package github.kasuminova;

import java.io.IOException;
import java.util.Arrays;

import static github.kasuminova.BsdiffNative.bsdiff;
import static github.kasuminova.BsdiffNative.bspatch;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] oldData = {0, 1, 2, 3, 4, 5};
        byte[] newData = {0, 1, 2, 3, 5, 6};
        byte[] patchData = bsdiff(oldData, newData);
        byte[] patchedData = bspatch(oldData, patchData, newData.length);

        System.out.println("oldData: " + Arrays.toString(oldData));

        System.out.println();
        System.out.println("newData: " + Arrays.toString(newData));

        System.out.println();
        System.out.println("patchData: " + Arrays.toString(patchData));

        System.out.println();
        System.out.println("patchedData: " + Arrays.toString(patchedData));
    }
}
