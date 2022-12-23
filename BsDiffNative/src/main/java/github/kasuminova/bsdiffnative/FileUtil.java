package github.kasuminova.bsdiffnative;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    /**
     * 提取 Jar 内文件至指定路径
     *
     * @param src JAR 内文件
     * @param dst 提取路径
     * @throws IOException IO 错误
     */
    public static void extractJarFile(String src, String dst) throws IOException {
        InputStream input = FileUtil.class.getResourceAsStream(src);
        if (input == null) {
            throw new IOException(String.format("File %s does not exist!", src));
        }

        OutputStream output = Files.newOutputStream(Paths.get(dst));
        byte[] buf = new byte[8192];
        int bytesRead;
        while ((bytesRead = input.read(buf)) > 0) {
            output.write(buf, 0, bytesRead);
        }
        input.close();
        output.close();
    }
}
