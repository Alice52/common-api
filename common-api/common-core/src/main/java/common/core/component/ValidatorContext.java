package common.core.component;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Validator;

/**
 * @author zack <br>
 * @create 2021-06-04 16:52 <br>
 * @project custom-test <br>
 */
@Component
public class ValidatorContext {
    private static Validator validator;

    public static Validator getValidator() {
        return validator;
    }

    @Resource
    public void setValidator(Validator validator) {
        ValidatorContext.validator = validator;
    }
}
