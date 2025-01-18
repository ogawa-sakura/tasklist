package com.example.demo.validator;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotFullWidthSpaceValidator implements ConstraintValidator<NotFullWidthSpace, String> {

    @Override
    public void initialize(NotFullWidthSpace constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
    	// 空の値は他のバリデーションでチェックされる
    	if (value == null || value.isEmpty()) {
    		return true; 
    	}
    	// 全て全角スペースかチェック
        return !value.chars().allMatch(ch -> ch == '\u3000');
    }
}

