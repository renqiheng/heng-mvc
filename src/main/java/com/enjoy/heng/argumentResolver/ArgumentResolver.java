package com.enjoy.heng.argumentResolver;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ArgumentResolver {

	//�ж��Ƿ�Ϊ��ǰ��Ҫ��������
	public boolean support(Class<?> type,int index,Method method);
	
	//��������
	public Object argumentResolver(HttpServletRequest request,HttpServletResponse response,
			Class<?> type,int index,Method method);
	
}
