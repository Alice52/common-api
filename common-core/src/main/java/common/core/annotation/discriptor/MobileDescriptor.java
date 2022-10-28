package common.core.annotation.discriptor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import cn.hutool.core.util.StrUtil;
import common.core.annotation.Mobile;
import common.core.util.valid.ValidatorUtil;

/**
 * @author zack <br>
 * @create 2021-06-04 16:47 <br>
 * @project custom-test <br>
 */
public class MobileDescriptor implements ConstraintValidator<Mobile, String> {

    private boolean required = false;

    @Override
    public void initialize(Mobile constraint) {
        this.required = constraint.required();
    }

    @Override
    public boolean isValid(String obj, ConstraintValidatorContext context) {

        if (!required && StrUtil.isBlank(obj)) {
            return true;
        }

        return ValidatorUtil.validateMobile(obj);
    }
}
