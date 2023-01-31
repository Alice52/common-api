package common.kotlin;

import org.codehaus.commons.nullanalysis.NotNull;
import org.springframework.lang.Nullable;

public class ManualPojo {
    private Integer age;
    public String getFoo() {
        return null;
    }

    @NotNull
    public String getBar() {
        return "234";
    }

    @Nullable
    public Object someMethod() {
        return null;
    }
}
