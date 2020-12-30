package com.hj.myweb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hj.myweb.dto.MemberDto;
import com.hj.myweb.service.MemberSerivce;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private MemberSerivce mServ;
	
	//홈으로 이동
	@GetMapping("/")
	public String home() {
		logger.info("home()");
		
		return "home";
	}
	
	//회원가입으로 이동
	@GetMapping("joinFrm")
	public String joinFrm() {
		logger.info("joinFrm()");
		
		return "joinFrm";
	}
	
	//로그인으로 이동
	@GetMapping("loginFrm")
	public String loginFrm() {
		logger.info("loginFrm()");
		
		return "loginFrm";
	}
	
	//회원가입
	@PostMapping("regMember")
	public String regMember(MemberDto member, RedirectAttributes rttr) {
		logger.info("regMember");
		
		String view = mServ.regMember(member, rttr);
		
		return view;
	}
	
	//아이디 중복확인
	@GetMapping(value = "idCheck", produces = "application/text; charset=utf-8")
	@ResponseBody
	public String idCheck(String id) {
		logger.info("idCheck() id : " + id);
		
		String result = mServ.idCheck(id);
		
		return result;
	}
	
	//로그인
	@PostMapping("loginProc")
	public String loginProc(MemberDto member, RedirectAttributes rttr) {
		logger.info("loginProc() - id : " + member.getM_id());
	
		return mServ.loginProc(member,rttr);
	}
	
	//로그아웃
	@GetMapping("logout")
	public String logout() {
		
		return mServ.logout();
	}
	
}
