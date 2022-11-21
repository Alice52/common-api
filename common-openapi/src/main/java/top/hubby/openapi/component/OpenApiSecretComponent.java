//package top.hubby.openapi.component;
//
//import java.util.HashMap;
//
//import javax.annotation.PostConstruct;
//
//import org.springframework.beans.factory.annotation.Value;
//
///**
// * @author zack <br>
// * @create 2022-04-07 16:32 <br>
// * @project mc-platform <br>
// */
//public final class OpenApiSecretComponent {
//
//    public static HashMap<String, String> APP_MAP = new HashMap<>();
//
//    private static String eArrivalAppId;
//    private static String eArrivalSecret;
//
//    @PostConstruct
//    public void init() {
//        APP_MAP.put(eArrivalAppId, eArrivalSecret);
//    }
//
//    @Value("${common.openapi.earrival.client-id:}")
//    public void seteArrivalAppId(String eArrivalAppId) {
//        OpenApiSecretComponent.eArrivalAppId = eArrivalAppId;
//    }
//
//    @Value("${common.openapi.earrival.client-secret:}")
//    public void seteArrivalSecret(String eArrivalSecret) {
//        OpenApiSecretComponent.eArrivalSecret = eArrivalSecret;
//    }
//}
