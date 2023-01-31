package common.kotlin;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@Getter
@Setter
@ToString
public class SomePojo {

    @NonNull private String name;
    private int age;

    private boolean human;
}
