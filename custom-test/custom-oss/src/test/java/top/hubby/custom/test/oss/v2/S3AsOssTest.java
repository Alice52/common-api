package top.hubby.custom.test.oss.v2;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3Object;
import common.oss.v2.service.OssTemplate;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.hubby.test.custom.oss.OssApplication;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

/**
 * 只需要修改配置文件即可测试不同的服务商
 *
 * @author T04856 <br>
 * @create 2023-03-23 4:49 PM <br>
 * @project project-cloud-custom <br>
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OssApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class S3AsOssTest {
    private final String URL_PATH =
            "https://imgcache.qq.com/open_proj/proj_qcloud_v2/cvm/src/styles/img/sence1.png";
    @Resource private OssTemplate ossTemplate;

    @Test
    public void test1CreateBucket() {
        ossTemplate.createBucket("oss02");
    }

    @Test
    public void test2GetAllBuckets() {
        List<Bucket> allBuckets = ossTemplate.getAllBuckets();
        log.info("allBuckets: {}", allBuckets);
    }

    @SneakyThrows
    @Test
    public void test3Upload() {
        URI u = URI.create(URL_PATH);
        InputStream inputStream = u.toURL().openStream();
        ossTemplate.putObject("oss02", "45.png", inputStream);
    }

    @SneakyThrows
    @Test
    public void test4GetObject() {
        S3Object object = ossTemplate.getObject("oss02", "45.png");
        log.info("GetObject: {}", object);
    }

    @SneakyThrows
    @Test
    public void test5GetObjectUrl() {
        String objectURL = ossTemplate.getObjectURL("oss02", "45.png", 7);
        log.info("GetObjectURL: {}", objectURL);
    }

    @SneakyThrows
    @Test
    public void test6RemoveObject() {
        ossTemplate.removeObject("oss02", "45.png");
    }

    @Test
    public void test7RemoveBucket() {
        ossTemplate.removeBucket("oss02");
    }
}
