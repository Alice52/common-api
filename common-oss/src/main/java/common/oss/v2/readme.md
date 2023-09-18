## core point

1. 适配 阿里云OSS、腾讯COS、七牛云OSS、MInio等对象存储服务

    - [腾讯COS](https://cloud.tencent.com/document/product/436/37421#java)

2. AmazonS3

    - Amazon S3 是 AWS 最早推出的云服务之一
    - S3 协议在对象存储行业事实上已经成为标准: **`因此市面上几乎所有的对象存储服务都兼容S3标准`**

       ```js
       1. 提供了统一的接口 REST/SOAP 来统一访问任何数据
       2. 对 S3 来说，存在里面的数据就是对象名（键），和数据（值）
       3. 不限量，单个文件最高可达 5TB，可动态扩容。
       4. 高速。每个 bucket 下每秒可达 3500 PUT/COPY/POST/DELETE 或 5500 GET/HEAD 请求。
       5. 具备版本，权限控制能力
       6. 具备数据生命周期管理能力
       ```

3. 因此基于S3封装可以获得 `高适配, 高迁移, 高可扩展` 的优点

   ![avatar](/docs/images/oss-v2.png)

    - 更换服务商只需要修改配置即可

4. WIP: 考虑到一个项目一般只会使用一个 Bucket, 因此需要将 Bucket 做进配置项

## 依赖

1. com.amazonaws:aws-java-sdk-s3
2. org.springframework.boot:spring-boot-starter

## link

1. https://mp.weixin.qq.com/s?__biz=MzU3MDAzNDg1MA==&mid=2247523579&idx=1&sn=cf22f30ade7dc3d20d8aed4b3601ea1f