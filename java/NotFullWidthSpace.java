	package com.example.demo.validator;
	
	import java.lang.annotation.*;
	
	import jakarta.validation.Constraint;
	import jakarta.validation.Payload;
	
	@Constraint(validatedBy = NotFullWidthSpaceValidator.class)
	@Target({ ElementType.METHOD, ElementType.FIELD })
	@Retention(RetentionPolicy.RUNTIME)
	public @interface NotFullWidthSpace {
		// エラーメッセージを設定
	    String message() default "{com.example.demo.validator.NotFullWidthSpace.message}";
	    // バリデーションをグループ化
	    Class<?>[] groups() default {};
	    // メタ情報を与える(アノテーションをつける対象をカテゴライズするのに使う)
	    Class<? extends Payload>[] payload() default {};
	}
