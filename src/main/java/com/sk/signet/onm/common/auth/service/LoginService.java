package com.sk.signet.onm.common.auth.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sk.signet.onm.mapper.auth.LoginMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service("loginService")
@SuppressWarnings({ "unchecked", "rawtypes", "unused" })
public class LoginService {

	@Autowired
	private LoginMapper loginMapper;
	
	public Map<String, Object> login( Map param) throws Exception {
		
		try {
			Map<String, Object> loginMap = loginMapper.login(param);

			return loginMap;
		} catch(Exception e) {
			log.error(" LoginService Exception===============>" + e);
			return null;
		}
	}
	
	//로그인 추적이력생성
	@Transactional
	public void insertLoginHist(Map param) throws Exception {
	
		loginMapper.insertLoginHist(param);
	}    

    //로그아웃 이력업데이트 2021.12.20 김도형
	public void updateLogoutHist(Map param) throws Exception {
	
		loginMapper.updateLogoutHist(param);
	}
	
}
