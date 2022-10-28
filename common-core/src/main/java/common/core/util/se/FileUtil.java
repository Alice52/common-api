package common.core.util.se;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import lombok.experimental.UtilityClass;

/**
 * @author zack <br>
 * @create 2021-06-25<br>
 * @project project-custom <br>
 */
@UtilityClass
public final class FileUtil extends cn.hutool.core.io.FileUtil {

    /**
     * Convert input stream to file.
     *
     * @param inputStream
     * @param file
     * @throws IOException
     */
    public static void copyInputStreamToFile(InputStream inputStream, File file)
            throws IOException {

        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        }
    }
}
