package com.enjoy.heng.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//���÷�Χ"�ֶκ���"
@Target({java.lang.annotation.ElementType.TYPE})
//���ֽ��ļ��д��ڣ�����ʱͨ��������Ի�ȡ
@Retention(RetentionPolicy.RUNTIME)
//ע�������java��doc��
@Documented
//ע��ɱ��̳�
@Inherited
public @interface EnjoyService {

	//����ע�������ֵ,����@EnjoyQualifier("value")
	String value() default "";
}