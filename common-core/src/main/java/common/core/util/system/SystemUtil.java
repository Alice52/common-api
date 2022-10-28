package common.core.util.system;

import java.lang.management.ManagementFactory;
import java.util.Properties;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author asd <br>
 * @create 2021-10-19 2:07 PM <br>
 * @project swagger-3 <br>
 */
@Slf4j
public class SystemUtil extends EnvPropertiesUtil {
    @SneakyThrows
    public static void loadPropertySource(Class clazz, String fileName) {
        Properties p = new Properties();
        p.load(clazz.getResourceAsStream(fileName));
        p.forEach(
                (k, v) -> {
                    log.info("{}={}", k, v);
                    System.setProperty(k.toString(), v.toString());
                });
    }

    public static int getPid() {
        // Note: this will trigger local host resolve, which might be slow.
        String name = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(name.split("@")[0]);
    }
}
