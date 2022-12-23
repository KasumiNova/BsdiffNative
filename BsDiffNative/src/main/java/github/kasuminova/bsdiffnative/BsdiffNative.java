package github.kasuminova;

import java.io.IOException;

public class BsdiffNative {
    static {
        System.loadLibrary("mcpatch_manage_native");
    }

    public static native byte[] bsdiff(byte[] oldData, byte[] newData) throws IOException;
    public static native byte[] bspatch(byte[] oldData, byte[] patchData, long newFileSize) throws IOException;
}
