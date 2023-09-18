package common.core.annotation.discriptor;

import common.core.annotation.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author T04856 <br>
 * @create 2023-03-27 9:45 AM <br>
 * @project project-cloud-custom <br>
 */
public class EnumValueDescriptor implements ConstraintValidator<EnumValue, Object> {

    @Deprecated private String[] strValues;
    @Deprecated private int[] intValues;

    private Class<? extends Enum> enums;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        strValues = constraintAnnotation.strValues();
        intValues = constraintAnnotation.intValues();
        enums = constraintAnnotation.values();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (value instanceof Enum) {
            for (Enum constant : enums.getEnumConstants()) {
                if (constant.equals(value)) {
                    return true;
                }
            }
        }

        if (value instanceof String) {
            for (String s : strValues) {
                if (s.equals(value)) {
                    return true;
                }
            }
        }

        if (value instanceof Integer) {
            for (int s : intValues) {
                if (((int) value) == s) {
                    return true;
                }
            }
        }
        return false;
    }
}
