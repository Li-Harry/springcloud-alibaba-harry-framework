package com.neoderm.core.config;

import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@Configuration
public class ValidatorConfiguration {

	/**
	 * 两种方式设置快速失败
	 * @return
	 */
	@Bean
	public Validator validator() {
		ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
				.configure()
				// 快速失败模式，当遇到第一个不满足条件的参数时就立即返回，不再继续后面的参数校验。
				// 否则会一次性验证所有参数，并返回所有不符合要求的错误信息
				.failFast(true)
				// .addProperty("hibernate.validator.fail_fast", "true") // 快速失败模式
				.buildValidatorFactory();
		Validator validator = validatorFactory.getValidator();
		return validator;
	}

}
