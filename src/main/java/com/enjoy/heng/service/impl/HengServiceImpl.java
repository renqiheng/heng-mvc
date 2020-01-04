package com.enjoy.heng.service.impl;

import com.enjoy.heng.annotation.EnjoyService;
import com.enjoy.heng.service.HengService;

@EnjoyService("HengServiceImpl")
public class HengServiceImpl implements HengService {


	public String query(String name, String age) {
		return "name ==" + name +"; age == "+age;
	}


	public String insert(String param) {
		return "insert successful ..........";
	}


	public String update(String param) {
		return "update successful .......";
	}

}
