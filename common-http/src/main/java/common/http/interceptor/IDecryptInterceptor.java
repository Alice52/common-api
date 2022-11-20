//package common.http.interceptor;
//
//import okhttp3.MediaType;
//import okhttp3.Response;
//
//import java.util.Arrays;
//
///**
// * @author zack <br>
// * @create 2022-11-20 23:00 <br>
// * @project custom-auth <br>
// */
//public interface IDecryptInterceptor<T> {
//
//    /**
//     * is json type
//     *
//     * @return
//     */
//    Type type();
//
//    /**
//     * get encrypt data.
//     *
//     *
//     * @param res
//     * @param t
//     * @return
//     */
//    Response decryptJson(Response res, T t);
//
//    enum Type {
//        JSON,
//        STRING;
//
//        public static boolean contains(Type type) {
//            return Arrays.stream(Type.values()).filter(x -> x.equals(type)).count() > 0;
//        }
//    }
//}
