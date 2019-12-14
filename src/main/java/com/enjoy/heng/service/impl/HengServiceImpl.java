package com.enjoy.heng.service.impl;

import com.enjoy.heng.annotation.EnjoyService;
import com.enjoy.heng.service.HengService;

@EnjoyService("HengServiceImpl")
public class HengServiceImpl implements HengService {

	@Override
	public String query(String name, String age) {
		return "name ==" + name +"; age == "+age;
	}

	@Override
	public String insert(String param) {
		return "insert successful ..........";
	}

	@Override
	public String update(String param) {
		return "update successful .......";
	}

}
