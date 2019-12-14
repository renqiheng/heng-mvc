package com.enjoy.heng.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//作用范围"类"
@Target({java.lang.annotation.ElementType.TYPE})
//在字节文件中存在，运行时通过反射可以获取
@Retention(RetentionPolicy.RUNTIME)
//注解包含在java的doc中
@Documented
//注解可被继承
@Inherited
public @interface EnjoyController {

	//可在注解后增加值,比如@requestMapping("")
	String value() default "";
}
