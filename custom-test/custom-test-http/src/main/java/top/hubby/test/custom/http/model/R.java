package top.hubby.test.custom.http.model;

import com.fasterxml.jackson.core.type.TypeReference;
import common.http.component.DecryptComponent;
import lombok.Data;

@Data
@Deprecated
public class R<T> {
    private int code;
    private String msg;
    private T data;

    public void setData(T data) {

        // json-util is also can work here.
        this.data = DecryptComponent.tryDecrypt(data.toString(), new TypeReference<T>() {});
    }

    @Deprecated
    public void _setData(T data) {
        this.data = data;
    }
}
