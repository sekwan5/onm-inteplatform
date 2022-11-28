package com.sk.signet.onm.common.aop;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sk.signet.onm.common.auth.error.UnauthorizedException;
import com.sk.signet.onm.common.auth.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@RequiredArgsConstructor
public class AuthCheckerAspect {

	@Autowired
	private JwtService jwtService;
	private static final String HEADER_AUTH = "Authorization";

	private static final String[] excludedPaths = {
			"/auth/login"
	};

	public boolean excludePath(HttpServletRequest request) {
		for (String excludedPath : excludedPaths) {
			String requestURI = (String) request.getRequestURI();
			log.debug("[비교]:" + requestURI + "==" + request.getContextPath() + excludedPath);
			if (requestURI.contains(excludedPath)) {
				log.debug(
						">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
				log.debug("제외:" + requestURI + "!=" + request.getContextPath() + excludedPath);
				return true;
			}
		}
		return false;
	}

	/**
	 * Controller API 조회 전 인증 확인 AOP
	 * TODO: 인증서비스 개발완료 후 @Before 주석제거.
	 * 
	 * @param joinPoint
	 */
	@Before("execution(* *..*Controller.*(..))")
	public void before(final JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();

		if (excludePath(request)) {
			// by pass
		} else {

			String token = request.getHeader(HEADER_AUTH);

			if (Pattern.matches("^Bearer .*", token)) {
				token = token.replaceAll("^Bearer( )*", "");
			}

			if (token != null && jwtService.isUsable(token) && jwtService.getExpToken(token)) {

				String newToken = jwtService.refreshToken(token);
				response.setHeader("Access-Control-Expose-Headers", HEADER_AUTH);
				response.setHeader(HEADER_AUTH, newToken); // 요청 웹브라우즈에 새로 생성한 token 되돌려줌.
			} else {
				throw new UnauthorizedException();
			}
		}
	}

}
