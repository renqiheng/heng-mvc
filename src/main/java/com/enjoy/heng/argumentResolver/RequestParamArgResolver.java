package com.enjoy.heng.argumentResolver;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyRequestParam;
import com.enjoy.heng.annotation.EnjoyService;

@EnjoyService("RequestParamArgResolver")
public class RequestParamArgResolver implements ArgumentResolver{

	@Override
	public boolean support(Class<?> type, int index, Method method) {
		Annotation[][] anno = method.getParameterAnnotations();
		Annotation[] paramAnnos = anno[index];
		for(Annotation an : paramAnnos){
			if(EnjoyRequestParam.class.isAssignableFrom(an.getClass())){
				return true;
			}
		}
		return false;
	}

	@Override
	public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class<?> type, int index,
			Method method) {
		Annotation[][] anno = method.getParameterAnnotations();
		Annotation[] paramAnnos = anno[index];
		for(Annotation an : paramAnnos){
			if(EnjoyRequestParam.class.isAssignableFrom(an.getClass())){
				EnjoyRequestParam er = (EnjoyRequestParam)an;
				String value = er.value();
				return request.getParameter(value);
			}
		}
		return null;
	}

}
