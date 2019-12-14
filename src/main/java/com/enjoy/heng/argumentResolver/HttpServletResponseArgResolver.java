package com.enjoy.heng.argumentResolver;

import java.lang.reflect.Method;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyService;

@EnjoyService("HttpServletResponseArgResolver")
public class HttpServletResponseArgResolver implements ArgumentResolver{

	@Override
	public boolean support(Class<?> type, int index, Method method) {
		return ServletResponse.class.isAssignableFrom(type);
	}

	@Override
	public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index,
			Method method) {
		return response;
	}

}
