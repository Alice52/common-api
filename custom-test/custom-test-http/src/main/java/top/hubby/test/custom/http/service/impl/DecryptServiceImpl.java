package top.hubby.test.custom.http.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Method;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import common.core.model.Pagination;
import common.core.util.R;
import common.http.configuration.HttpProperties;
import common.http.support.HttpSupport;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Service;
import top.hubby.test.custom.http.service.DecryptService;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class DecryptServiceImpl implements DecryptService {

    @Resource private HttpProperties properties;
    @Resource private ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public R<Pagination> fullEncrypt() {

        String response =
                HttpSupport.doRequest(
                        Method.GET,
                        "/openapi/mockup/encrypted/full",
                        MapUtil.<String, String>builder()
                                .put("clientId", "custom-test-http")
                                .build(),
                        null,
                        HttpProperties.DecryptTypeEnum.FULL);
        return objectMapper.readValue(response, new TypeReference<R<Pagination>>() {});
    }

    @SneakyThrows
    @Override
    public R<Pagination> fullEncryptV2() {

        R<Pagination> response =
                HttpSupport.doRequest(
                        Method.GET,
                        properties.getHost(),
                        "/openapi/mockup/encrypted/full",
                        () ->
                                MapUtil.<String, String>builder()
                                        .put("clientId", "custom-test-http")
                                        .build(),
                        null,
                        () -> {
                            ObjectNode objectNode = objectMapper.createObjectNode();
                            objectNode.put("name", "zack");
                            return objectNode;
                        },
                        x -> {
                            try {
                                return objectMapper.readValue(
                                        x, new TypeReference<R<Pagination>>() {});
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        HttpProperties.DecryptTypeEnum.FULL);

        return response;
    }

    @SneakyThrows
    @Deprecated
    @Override
    public top.hubby.test.custom.http.model.R<Pagination> partyEncrypt() {

        String response =
                HttpSupport.doRequest(
                        Method.GET,
                        "/openapi/mockup/encrypted/party",
                        MapUtil.<String, String>builder()
                                .put("clientId", "custom-test-http")
                                .build(),
                        null,
                        HttpProperties.DecryptTypeEnum.DATA);

        // error: it will parse Pagination from string, which is encrypted data and cnanot be parsed
        // val reference = new TypeReference<top.hubby.test.custom.http.model.R<Pagination>>() {};

        // ok: it will call data setter to init value,
        //      and this response's data will become LinkedHashMap
        val reference = new TypeReference<top.hubby.test.custom.http.model.R>() {};

        top.hubby.test.custom.http.model.R res = objectMapper.readValue(response, reference);
        Object data = res.getData();
        if (data instanceof Map) {
            Pagination p = convert2Bean((Map) data);
            res._setData(p);
        }

        return res;
    }

    @Deprecated
    private Pagination convert2Bean(Map data) {

        return BeanUtil.mapToBean(data, Pagination.class, false);
    }
}
