package com.enjoy.heng.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enjoy.heng.annotation.EnjoyController;
import com.enjoy.heng.annotation.EnjoyQualifier;
import com.enjoy.heng.annotation.EnjoyRequestMapping;
import com.enjoy.heng.annotation.EnjoyRequestParam;
import com.enjoy.heng.service.HengService;

@EnjoyController
@EnjoyRequestMapping("/heng")
public class HengController {
	
	@EnjoyQualifier("HengServiceImpl")
	private HengService hengService;
	
	
	@EnjoyRequestMapping("/query")
	public void query(HttpServletRequest request, HttpServletResponse response,
			@EnjoyRequestParam("name") String name,@EnjoyRequestParam("age")String age){
		
		try {
			PrintWriter pw = response.getWriter();
			String result =hengService.query(name, age); 
			pw.write(result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
