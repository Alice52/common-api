package common.core.annotation.discriptor;

import cn.hutool.core.collection.CollUtil;
import common.core.annotation.ValidList;
import common.core.component.ValidatorContext;
import common.core.exception.ListValidException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * common-log#desensitize#v2
 *
 * @author zack <br>
 * @create 2021-06-04 16:51 <br>
 * @project custom-test <br>
 */
public class ValidListDescriptor implements ConstraintValidator<ValidList, List> {
    Class<?>[] groups = null;
    boolean quickFail = false;

    @Override
    public void initialize(ValidList constraintAnnotation) {
        groups = constraintAnnotation.values();
        quickFail = constraintAnnotation.quickFail();
    }

    @Override
    public boolean isValid(List values, ConstraintValidatorContext context) {

        Validator validator = ValidatorContext.getValidator();

        Map<Integer, Set<ConstraintViolation<Object>>> errors = new HashMap<>();
        int size = values.size();
        for (int i = 0; i < size; i++) {
            Set<ConstraintViolation<Object>> violations = validator.validate(values.get(i), groups);
            if (CollUtil.isNotEmpty(violations)) {
                errors.put(i, violations);
                if (quickFail) {
                    throw new ListValidException(errors);
                }
            }
        }

        if (errors.size() > 0) {
            throw new ListValidException(errors);
        }

        return true;
    }
}
