package com.hj.myweb.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hj.myweb.dao.MemberDao;
import com.hj.myweb.dto.MemberDto;

import lombok.extern.java.Log;

@Service
@Log
public class MemberSerivce {
	@Autowired
	private MemberDao mDao;
	
	@Autowired
	private HttpSession session;
	
	//회원가입
	@Transactional
	public String regMember(MemberDto member, RedirectAttributes rttr) {
		String view = null;
		String msg = null;
		
		//비밀번호 암호화
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		String encPwd = pwdEncoder.encode(member.getM_pwd());
		member.setM_pwd(encPwd);
		
		try {
			mDao.regMember(member);
			
			view = "redirect:/";
			msg = "가입이 완료되었습니다";
		} catch (Exception e) {
			//e.printStackTrace();
			view = "redirect:joinFrm";
			msg = "이미 존재하는 아이디입니다";
		}
		
		rttr.addFlashAttribute("msg", msg);
		
		return view;
	}

	public String idCheck(String id) {
		log.info("idCheck() id : " + id);
		
		String result = null;
		
		int cnt = mDao.idCheck(id);
		
		if(cnt == 0) {
			//사용 가능한 아이디
			result = "success";
		}
		else {
			//중복된 아이디
			result = "fail";
		}
		
		return result;
	}

	//로그인
	public String loginProc(MemberDto member, RedirectAttributes rttr) {
		String view = null;
		
		//DB에서 암호화된 비밀번호 구하기
		String encPwd = mDao.getEncPwd(member.getM_id());
		
		//암호화된 비밀번호와 입력한 비밀번호의 비교 처리를 위한 인코더 생성
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		if(encPwd != null) {
			//가입된 아이디 있음
			if(pwdEncoder.matches(member.getM_pwd(), encPwd)) {
				//로그인 후 사용할 사용자 정보
				member = mDao.getMemberInfo(member.getM_id());
				session.setAttribute("user", member);
				
				//로그인 성공시 게시판 컨트롤러로 이동
				view = "redirect:/";
			}
			else {
				//비밀번호 오류
				view = "redirect:loginFrm";
				rttr.addFlashAttribute("msg", "비밀번호가 일치하지 않습니다");
			}
		}
		else {
			//아이디 없음
			view = "redirect:loginFrm";
			rttr.addFlashAttribute("msg", "존재하지 않는 ID입니다");
		}
		
		return view;
	}
	
	//회원 탈퇴
	public String resignMember(MemberDto member, RedirectAttributes rttr) {
		String view = null;
		String msg = null;
		System.out.println("id : " + member.getM_id() + ", pwd : " + member.getM_pwd());
		
		String encPwd = mDao.getEncPwd(member.getM_id());
		BCryptPasswordEncoder pwdEncoder = new BCryptPasswordEncoder();
		
		if(pwdEncoder.matches(member.getM_pwd(), encPwd)) {
			mDao.resignMember(member.getM_id());
			
			view = "redirect:/";
			msg = "탈퇴가 완료되었습니다";
		}
		else {
			view = "redirect:resignFrm";
			msg = "비밀번호가 일치하지 않습니다";
		}
		
		rttr.addFlashAttribute("msg", msg);
		return view;
	}

	//로그아웃
	public String logout() {
		session.invalidate();
		
		return "home";
	}
}//class end
