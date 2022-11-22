// package top.hubby.test.custom.http.component;
//
// import common.http.interceptor.DecryptInterceptor;
// import common.http.interceptor.IDecryptInterceptor;
// import lombok.extern.slf4j.Slf4j;
// import okhttp3.Response;
// import org.springframework.stereotype.Component;
// import top.hubby.test.custom.http.model.PageResponseVO;
//
// import javax.annotation.Resource;
//
/// **
// * @author zack <br>
// * @create 2022-11-20 23:17 <br>
// * @project custom-auth <br>
// */
// @Slf4j
// @Component
// public class HmpDecryptInter<P> implements IDecryptInterceptor<PageResponseVO<P>> {
//
//    @Resource private DecryptInterceptor decryptInter;
//
//    @Override
//    public Type type() {
//        return Type.JSON;
//    }
//
//    /**
//     * @param res
//     * @param vo
//     * @return
//     */
//    @Override
//    public Response decryptJson(Response res, PageResponseVO<P> vo) {
//
//        return null;
//    }
// }
