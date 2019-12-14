package com.enjoy.heng.hand;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyService;
import com.enjoy.heng.argumentResolver.ArgumentResolver;

@EnjoyService("hengHandTool")
public class HandToolsImpl implements HandToolsService{

	//返回方法里的所有参数
	@Override
	public Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method,
			Map<String, Object> beans) {
		Class<?>[] paramClazzs = method.getParameterTypes();
		Object[] args = new Object[paramClazzs.length];
		
		//拿到所有实现了ArgumentResolver这个接口的实例
		Map<String,Object> argReslovers = getInstanceType(beans,ArgumentResolver.class);
		int index = 0;
		int i = 0;
		for(Class<?> paramClazz : paramClazzs){
			//哪个参数对应了哪个解析类，用策略模式来找
			for(Entry<String,Object> entry : argReslovers.entrySet()){
				ArgumentResolver ar = (ArgumentResolver) entry.getValue();
				if(ar.support(paramClazz, index, method)){
					args[i++] = ar.argumentResolver(request, response, paramClazz, index, method);
				}
			}
			index ++;
		}
		return args;
	}

	private Map<String,Object> getInstanceType(Map<String,Object> beans,Class<?> type){
		Map<String,Object> resultBeans = new HashMap<String,Object>();
		for(Entry<String,Object> entry: beans.entrySet()){
			Class<?>[] infs = entry.getValue().getClass().getInterfaces();
			if(infs != null && infs.length > 0){
				for(Class<?> inf : infs){
					if(inf.isAssignableFrom(type)){
						resultBeans.put(entry.getKey(), entry.getValue());
					}
				}
			}
		}
		return resultBeans;
	}
}
