package com.enjoy.heng.hand;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandToolsService {

	public Object[] hand(HttpServletRequest request, HttpServletResponse response,Method method,Map<String,Object> beans);
}
