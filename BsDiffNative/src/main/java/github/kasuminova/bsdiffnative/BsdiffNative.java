package github.kasuminova.bsdiffnative;

import java.io.File;
import java.io.IOException;

import static github.kasuminova.bsdiffnative.Main.currentDir;
import static github.kasuminova.bsdiffnative.Main.osName;

public class BsdiffNative {
    static {
        if (osName.contains("Windows")) {
            String dllPath = currentDir + File.separator + "bsdiff_native.dll";
            if (!new File(dllPath).exists()) {
                try {
                    FileUtil.extractJarFile("/native/bsdiff_native.dll", dllPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            System.load(currentDir + File.separator + "bsdiff_native.dll");
        } else if (osName.contains("Linux")) {
            String soPath = currentDir + File.separator + "bsdiff_native.so";
            if (!new File(soPath).exists()) {
                try {
                    FileUtil.extractJarFile("/native/bsdiff_native.so", soPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            System.load(currentDir + File.separator + "bsdiff_native.so");
        } else {
            throw new RuntimeException("Unsupported system.");
        }
    }

    /**
     * 将 oldData 和 newData 相比较，输出 patch 内容
     *
     * @param oldData 旧内容的 bytes
     * @param newData 新内容的 bytes
     * @return patch 内容
     * @throws IOException 缓冲区写入错误时抛出
     */
    public static native byte[] bsdiff(byte[] oldData, byte[] newData) throws IOException;

    /**
     * 将 oldData 使用 patchData 覆盖，并输出 patch 后的 bytes
     * @param oldData 旧内容的 bytes
     * @param patchData 对应内容的 patch bytes
     * @param newFileSize 输出新文件的对应大小
     * @return patch 后的 bytes
     * @throws IOException 缓冲区写入错误时抛出
     */
    public static native byte[] bspatch(byte[] oldData, byte[] patchData, long newFileSize) throws IOException;
}
