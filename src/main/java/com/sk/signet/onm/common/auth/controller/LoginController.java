package com.sk.signet.onm.common.auth.controller;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sk.signet.onm.common.auth.error.UnauthorizedException;
import com.sk.signet.onm.common.auth.service.JwtService;
import com.sk.signet.onm.common.auth.service.LoginService;

@RestController
public class LoginController {
	
	
	@Autowired
	private JwtService jwtService;
	@Autowired
	private LoginService loginService;

	@PostMapping(value = "/auth/login")
	public ResponseEntity<Map<String, Object>> loginCheck(@RequestBody Map<String, Object> data,
			  HttpServletRequest request,
			  HttpServletResponse response) throws UnknownHostException {
		
		Map<String, Object> rcvData=new HashMap<>();
		
		try {
			/**
			 * TEST Data
			 * {
				  "userId": "INSOFT1",
				  "userPassword": "InSOFT1!@#$"
				}
			 */
			Map<String, Object> loginMap = loginService.login(data);
			if(loginMap != null) {
				// 로그인 이력저장
				loginService.insertLoginHist(loginMap);
				
				// JWT 토큰 생성 
				String token = jwtService.create("memberInfo", loginMap, "user");
				response.setHeader("Access-Control-Expose-Headers", "Authorization");
				response.setHeader("Authorization", token);
				rcvData.put("data","success");
			} else {
				rcvData.put("data","fail");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<Map<String, Object>>(rcvData, HttpStatus.OK);
	}
}
