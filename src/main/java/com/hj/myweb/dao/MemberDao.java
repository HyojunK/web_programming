package com.hj.myweb.dao;

import com.hj.myweb.dto.MemberDto;

public interface MemberDao {
	//회원가입
	public void regMember(MemberDto member);
	//아이디 중복확인
	public int idCheck(String id);
	//암호화된 비밀번호 구하기
	public String getEncPwd(String id);
	//로그인 후 사용할 사용자 정보 가져오기
	public MemberDto getMemberInfo(String id);
}
