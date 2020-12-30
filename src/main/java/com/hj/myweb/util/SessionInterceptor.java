package com.hj.myweb.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.java.Log;

@Log
public class SessionInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	HttpSession session;
	
	//컨트롤러로 요청이 전달되기 전에 처리하는 메소드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("preHandle() - 인터셉트");
		
		//세션에 로그인 정보가 없으면 첫번째 페이지로 이동
		if(session.getAttribute("user") == null) {
			response.sendRedirect("./");
			return false;
		}
		return true;
	}

}
