package com.sk.signet.onm.mapper.auth;

import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;

@MapperScan
@SuppressWarnings("rawtypes")
public interface LoginMapper {
	
	Map<String, Object> login(Map param) throws Exception;

	void insertLoginHist(Map param);

	void updateLogoutHist(Map param);
	

}
