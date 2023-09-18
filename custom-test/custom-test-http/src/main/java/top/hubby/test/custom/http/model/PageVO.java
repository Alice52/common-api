// package top.hubby.test.custom.http.model;
//
// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.core.type.TypeReference;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.SneakyThrows;
// import lombok.val;
//
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.concurrent.Future;
// import java.util.stream.Collectors;
//
// import static common.http.component.DecryptComponent.EXECUTOR;
// import static common.http.component.DecryptComponent.tryDecrypt;
//
/// **
// * @author asd <br>
// * @create 2021-12-07 4:47 PM <br>
// * @project project-cloud-custom <br>
// */
// @Data
// @NoArgsConstructor
// @JsonIgnoreProperties(ignoreUnknown = true)
// public class PageVO<T> {
//    private String status;
//    private String message;
//
//    private long page;
//    private long size;
//    private long totalPages;
//    private long totalElements;
//
//    private List<T> responseData;
//
//    @SneakyThrows
//    public void setResponseData(List<T> responseData) {
//
//        List<T> list = Collections.synchronizedList(new ArrayList<>());
//        List<Future<Boolean>> collect =
//                responseData.parallelStream()
//                        .map(x -> EXECUTOR.submit(() -> list.add(tryDecrypt(x.toString(), new
// TypeReference<T>() {
//                        }))))
//                        .collect(Collectors.toList());
//
//        for (val a : collect) {
//            a.get();
//        }
//
//        this.responseData = responseData;
//    }
// }
