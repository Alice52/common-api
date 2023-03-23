package common.oss.v2.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import common.oss.v2.configuration.properties.OssProperties;
import common.oss.v2.service.OssTemplate;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author T04856 <br>
 * @create 2023-03-23 4:39 PM <br>
 * @project project-cloud-custom <br>
 */
@RequiredArgsConstructor
public class OssTemplateImpl implements OssTemplate {

    private final AmazonS3 amazonS3;

    private final OssProperties ossProperties;

    /**
     * 创建Bucket
     *
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_CreateBucket.html
     * @param bucketName bucket名称
     */
    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {

        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        if (!amazonS3.doesBucketExistV2(bucketName)) {
            amazonS3.createBucket((bucketName));
        }
    }

    /**
     * 获取所有的buckets
     *
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListBuckets.html
     * @return
     */
    @Override
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    /**
     * 通过Bucket名称删除Bucket
     *
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteBucket.html
     * @param bucketName
     */
    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {

        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        amazonS3.deleteBucket(bucketName);
    }

    /**
     * 上传对象
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @param contextType 文件类型
     *     AmazonS3：https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     */
    @Override
    @SneakyThrows
    public void putObject(
            String bucketName, String objectName, InputStream stream, String contextType) {

        putObject(bucketName, objectName, stream, stream.available(), contextType);
    }
    /**
     * 上传对象
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream 文件流
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_PutObject.html
     */
    @Override
    @SneakyThrows
    public void putObject(String bucketName, String objectName, InputStream stream) {
        putObject(bucketName, objectName, stream, stream.available(), "application/octet-stream");
    }

    /**
     * 通过bucketName和objectName获取对象
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_GetObject.html
     */
    @Override
    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {

        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }
        return amazonS3.getObject(bucketName, objectName);
    }

    /**
     * 获取对象的url
     *
     * @param bucketName
     * @param objectName
     * @param expires
     * @return
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_GeneratePresignedUrl.html
     */
    @Override
    @SneakyThrows
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        URL url = amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime());
        return url.toString();
    }

    /**
     * 通过bucketName和objectName删除对象
     *
     * @param bucketName
     * @param objectName
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_DeleteObject.html
     */
    @Override
    @SneakyThrows
    public void removeObject(String bucketName, String objectName) {
        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        amazonS3.deleteObject(bucketName, objectName);
    }

    /**
     * 根据bucketName和prefix获取对象集合
     *
     * @param bucketName bucket名称
     * @param prefix 前缀
     * @param recursive 是否递归查询
     * @return
     * @link https://docs.aws.amazon.com/AmazonS3/latest/API/API_ListObjects.html
     */
    @Override
    @SneakyThrows
    public List<S3ObjectSummary> getAllObjectsByPrefix(
            String bucketName, String prefix, boolean recursive) {
        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        ObjectListing objectListing = amazonS3.listObjects(bucketName, prefix);
        return objectListing.getObjectSummaries();
    }

    /**
     * @param bucketName
     * @param objectName
     * @param stream
     * @param size
     * @param contextType
     * @return
     */
    @SneakyThrows
    private PutObjectResult putObject(
            String bucketName,
            String objectName,
            InputStream stream,
            long size,
            String contextType) {

        return putObject(bucketName, objectName, stream, stream.available(), contextType, null);
    }

    @SneakyThrows
    private PutObjectResult putObject(
            String bucketName,
            String objectName,
            InputStream stream,
            long size,
            String contextType,
            Date expirationTime) {

        if (OssProperties.OssVender.COS.equals(ossProperties.getVender())) {
            bucketName = bucketName + "-" + ossProperties.getCosAppId();
        }

        byte[] bytes = IOUtils.toByteArray(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // objectMetadata.setContentLength(size);
        objectMetadata.setContentType(contextType);
        Optional.ofNullable(expirationTime)
                .ifPresent(x -> objectMetadata.setExpirationTime(expirationTime));

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        // 上传
        return amazonS3.putObject(bucketName, objectName, byteArrayInputStream, objectMetadata);
    }
}
