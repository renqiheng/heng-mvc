package com.enjoy.heng.argumentResolver;

import java.lang.reflect.Method;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyService;

@EnjoyService("HttpServletRequestArgResolver")
public class HttpServletRequestArgResolver implements ArgumentResolver{

	@Override
	public boolean support(Class<?> type, int index, Method method) {
		return ServletRequest.class.isAssignableFrom(type);
	}

	@Override
	public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index,
			Method method) {
		return request;
	}

}
