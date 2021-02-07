게시판 프로젝트
===
목차
---
* [프로젝트 소개](https://github.com/HyojunK/web_programming/tree/master#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EC%86%8C%EA%B0%9C)
* [개발 환경](https://github.com/HyojunK/web_programming/tree/master#%EA%B0%9C%EB%B0%9C-%ED%99%98%EA%B2%BD)
* [주요 기능 및 실행 화면](https://github.com/HyojunK/web_programming/tree/master#%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EB%B0%8F-%EC%8B%A4%ED%96%89-%ED%99%94%EB%A9%B4)
* [컨트롤러 구성](https://github.com/HyojunK/web_programming/tree/master#%EC%BB%A8%ED%8A%B8%EB%A1%A4%EB%9F%AC-%EA%B5%AC%EC%84%B1)
* [Util 패키지 구성](https://github.com/HyojunK/web_programming/tree/master#util-%ED%8C%A8%ED%82%A4%EC%A7%80-%EA%B5%AC%EC%84%B1)
* [DAO 구성](https://github.com/HyojunK/web_programming/tree/master#dao-%EA%B5%AC%EC%84%B1)
* [Database 구성](https://github.com/HyojunK/web_programming/tree/master#database-%EA%B5%AC%EC%84%B1)

프로젝트 소개
---
기초적인 CRUD기능 구현을 위한 1인 게시판 프로젝트 입니다


개발 환경
---
* 언어 - JAVA / JavaScript / HTML
* IDE - Eclipse
* 프레임워크 - Spring Framework
* 웹 서버 - Apache Tomcat 9.0
* 주요 라이브러리 - JQuery / JSON / Apache Commons / MyBatis
* DB 설계 - Oracle SQL Developer

주요 기능 및 실행 화면
---
### 1. 회원 가입
##### - 회원가입시 JavaScript를 이용하여 ID 중복체크가 가능하도록 처리하였습니다.
##### - BCryptPasswordEncoder를 활용하여 비밀번호를 암호화하여 관리하도록 하였습니다.
```javascript
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
```
![회원가입 메인](https://user-images.githubusercontent.com/26563226/107143956-4d144500-697b-11eb-8571-6680d6165e8f.JPG)
<br><br>
#### **아이디 중복체크**
```javascript
//아이디 중복 확인
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
```
![아이디 중복](https://user-images.githubusercontent.com/26563226/107143958-4f769f00-697b-11eb-93e0-e9c3a944f7c6.gif)
![아이디 사용가능](https://user-images.githubusercontent.com/26563226/107143960-51406280-697b-11eb-81c5-64180079a7d2.gif)
### 2. 로그인
##### - Session을 이용하여 로그인/비로그인시 Navbar 메뉴의 출력을 다르게 처리하였습니다.
##### - 로그인 아이디와 비밀번호를 입력하면 DB의 정보와 비교하여 일치 시 로그인 처리, 다를시 상황에 맞는 오류 메시지가 출력되도록 처리하였습니다.
```javascript
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
				
				//로그인 성공시 홈으로 이동
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
```
![로그인 성공](https://user-images.githubusercontent.com/26563226/107144109-433f1180-697c-11eb-99ed-bffc9c3899ed.gif)
<br><br>
#### **비밀번호 오류**
![비밀번호 오류](https://user-images.githubusercontent.com/26563226/107143964-59000700-697b-11eb-85e2-0eaf5e7023f1.gif)
<br><br>
#### **아이디 오류**
![아이디 오류](https://user-images.githubusercontent.com/26563226/107143966-5a313400-697b-11eb-9c5f-6b70b137048e.gif)
<br><br>
### 3. 게시판
##### - JSTL의 c:foreach 기능을 사용하여 게시판 페이지 내 게시글 목록을 출력하였습니다.
##### - 게시글 및 댓글 작성시 작성자의 포인트가 증가하도록 처리하였습니다.
##### - 다중 파일 업로드 및 다운로드가 가능하도록 처리하였습니다.
##### - 게시글 수정이 가능하며 수정 페이지내에서 업로드한 파일 개별 삭제가 가능하도록 처리하였습니다.
##### - Ajax를 통해 페이지 내에서 댓글을 입력할 수 있도록 하였습니다.
##### - 게시글을 삭제하면 게시글 내 파일, 댓글까지 함께 삭제하도록 처리하였습니다.
```javascript
//게시글 목록 가져오는 메소드
	public ModelAndView getBoardList(Integer pageNum) {
		log.info("getBoardlist() - pageNum: " + pageNum);
		
		mv = new ModelAndView();
		
		int num = (pageNum == null) ? 1 : pageNum;
		
		List<BoardDto> bList = bDao.getList(num);
		
		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("bList", bList);
		
		//페이징 처리
		mv.addObject("paging", getPaging(num));
		
		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);
		
		mv.setViewName("boardList");
		
		return mv;
	}//getBoardlist end
```
![게시판 메인](https://user-images.githubusercontent.com/26563226/107143968-5d2c2480-697b-11eb-88e5-f58e63b4bca2.JPG)
<br><br>
#### **글쓰기**
```javascript
//게시글 등록 서비스 메소드
	@Transactional
	public String boardInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardInsert()");
		
		String view = null;
		
		String id = multi.getParameter("bid");
		String title = multi.getParameter("btitle");
		String contents = multi.getParameter("bcontents");
		String check = multi.getParameter("fileCheck");
		
		
		
		//textarea는 입력한 문자열 앞뒤로 공백이 발생
		//문자열 앞뒤 공백 제거. trim()
		contents = contents.trim();
		
		BoardDto board = new BoardDto();
		board.setBid(id);
		board.setBtitle(title);
		board.setBcontents(contents);
		
		try {
			bDao.boardInsert(board);
			
			//포인트 증가
			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("id", id);
			pMap.put("point", "2");
			
			bDao.pointUp(pMap);
			
			MemberDto member = mDao.getMemberInfo(id);
			session.setAttribute("mb", member);
			
			//파일 업로드 메소드 호출
			if(check.equals("1")) {
				fileUp(multi, board.getBnum());
			}
			
			view = "redirect:list";
			rttr.addFlashAttribute("msg", "글 등록 성공");
			
		}catch (Exception e) {
			e.printStackTrace();
			view = "redirect:writeFrm";
			rttr.addFlashAttribute("msg","글 등록 실패");
		}
		
		return view;
	}
	
	//파일 업로드 처리 메소드
	public boolean fileUp(MultipartHttpServletRequest multi, int bnum) throws Exception {
		//저장공간에 대한 절대 경로 구하기
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload/";
		log.info(path);
		
		File dir = new File(path);
		
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
		
		//실제 파일명과 저장 파일명을 함께 관리
		Map<String, String> fmap = new HashMap<String, String>();
		
		fmap.put("bnum", String.valueOf(bnum));
		
		//파일 전송시 기본 값 = 파일 다중 선택
		//멀티파트 요청 객체에서 꺼내올 때는 List를 사용
		List<MultipartFile> fList = multi.getFiles("files");
		
		for(int i = 0; i < fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			String on = mf.getOriginalFilename();
			System.out.println("oriName : " + on);
			fmap.put("oriName", on);
			
			//변경된 파일 이름 저장
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf("."));
			System.out.println("sysName : " + sn);
			fmap.put("sysName", sn);
			
			//해당 폴더에 파일 저장하기
			mf.transferTo(new File(path + sn));
			
			bDao.fileInsert(fmap);
		}
		
		return true;
	}
```
![글쓰기](https://user-images.githubusercontent.com/26563226/107143973-60271500-697b-11eb-8314-fdafba096490.gif)
<br><br>
#### **글 내용 보기**
```javascript
//게시글 내용 가져오기
	public ModelAndView getContents(Integer bnum) {
		mv = new ModelAndView();
		
		//조회수 1 증가
		bDao.viewUp(bnum);
		//글내용 가져오기
		BoardDto board = bDao.getContents(bnum);
		//파일 목록 가져오기
		List<BfileDto> bfList = bDao.getBfList(bnum);
		//댓글 목록 가져오기
		List<ReplyDto> rList = bDao.getReplyList(bnum);
		//모델에 데이터 담기
		mv.addObject("board", board);
		mv.addObject("bfList", bfList);
		mv.addObject("rList", rList);
		
		//뷰 이름 지정하기
		mv.setViewName("boardContents");
		
		return mv;
	}
```
![글 내용 보기](https://user-images.githubusercontent.com/26563226/107143974-64ebc900-697b-11eb-9e88-06aaa5cab363.gif)
<br><br>
#### **글 수정하기**
```javascript
//게시글 수정
	@Transactional
	public String boardUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardUpdate()");
		
		String view = null;
		String snum = multi.getParameter("bnum");
		log.info(snum);
		int num = Integer.parseInt(snum);
		String title = multi.getParameter("btitle");
		String contents = multi.getParameter("bcontents");
		String check = multi.getParameter("fileCheck");
		
		contents = contents.trim();
		
		BoardDto board = new BoardDto();
		
		board.setBnum(num);
		board.setBtitle(title);
		board.setBcontents(contents);
		
		try {
			bDao.boardUpdate(board);
			
			//파일 업로드 메소드 호출
			if(check.equals("1")) {
				fileUp(multi, board.getBnum());
			}
			
			view = "redirect:list";
			rttr.addFlashAttribute("msg", "글 수정 성공");
		} catch (Exception e) {
			e.printStackTrace();
			int pageNum = (int)session.getAttribute("pageNum");
			view = "redirect:list?pageNum=" + pageNum;
			rttr.addFlashAttribute("msg","글 수정 실패");
		}
		
		return view;
	}
```
![글 수정](https://user-images.githubusercontent.com/26563226/107143978-69b07d00-697b-11eb-9d9f-531c25aa1623.gif)
<br><br>
#### **댓글 작성**
```javascrip
//댓글 등록
	@Transactional
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply){
		Map<String, List<ReplyDto>> rMap = null;
		
		try {
			//댓글을 DB에 입력
			bDao.replyInsert(reply);
			
			//포인트 증가
			Map<String, String> pMap = new HashMap<String, String>();
			pMap.put("id", reply.getR_id());
			pMap.put("point", "1");
			
			bDao.pointUp(pMap);
			
			MemberDto member = mDao.getMemberInfo(reply.getR_id());
			session.setAttribute("mb", member);
			
			//댓글 목록을 다시 검색
			List<ReplyDto> rList = bDao.getReplyList(reply.getR_bnum());
			rMap = new HashMap<String, List<ReplyDto>>();
			rMap.put("rList", rList);
		}catch (Exception e) {
			//e.printStackTrace();
			rMap = null;
		}
		
		return rMap;
	}
```
![댓글 작성](https://user-images.githubusercontent.com/26563226/107143985-6d440400-697b-11eb-8b99-f071598ac02a.gif)
<br><br>
#### **글 삭제**
```javascript
//게시글 삭제
	@Transactional
	public String delBoard(int bnum, RedirectAttributes rttr) {
		log.info("delBoard() - bnum: " + bnum);
		
		String view = null;
		String msg = null;
		
		try {
			//댓글 삭제
			bDao.delReply(bnum);
			//파일 삭제
			bDao.delFileList(bnum);
			//게시글 삭제
			bDao.delBoard(bnum);
			
			view = "redirect:boardList";
			msg = "삭제되었습니다";
			
		} catch (Exception e) {
			//e.printStackTrace();
			view = "redirect:boardContents?bnum=" + bnum;
			msg = "삭제에 실패하였습니다";
		}
		
		rttr.addFlashAttribute("msg", msg);
		return view;
	}
```
![글 삭제](https://user-images.githubusercontent.com/26563226/107143991-7208b800-697b-11eb-9319-1614144d3a11.gif)
<br><br>
#### **파일 다운로드**
```javascript
//파일 다운로드
	public void fileDown(String sysName, HttpServletRequest request, HttpServletResponse response) {
		String path = request.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload/";
		log.info(path);
		
		String oriName = bDao.getOriName(sysName);
		path += sysName;//다운로드할 파일 경로 + 파일명
		
		//서버 저장장치에서 저장된 파일을 읽어오는 통로
		InputStream is = null;
		//사용자 컴퓨터에 파일을 보내는 통로
		OutputStream os = null;
		
		try {
			//파일명 인코딩(파일명이 한글일 때 깨짐을 방지)
			String dFileName = URLEncoder.encode(oriName, "UTF-8");
			
			//파일 객체 생성
			File file = new File(path);
			is = new FileInputStream(file);
			
			//응답 객체(response)의 헤더 설정
			//파일 전송용 contentType 설정
			response.setContentType("application/octet-stream");
			response.setHeader("content-Disposition", "attachment; filename=\"" + dFileName + "\"");;
			
			//응답 객체와 보내는 통로 연결
			os = response.getOutputStream();
			
			//파일 전송(byte 단위로 전송)
			byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer)) != -1) {
				os.write(buffer, 0, length);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				os.flush();//OutputStream에 남아 있을지 모르는 데이터를 강제로 전송하도록 처리
				
				os.close();
				is.close();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
	}
```
![파일 다운로드](https://user-images.githubusercontent.com/26563226/107143995-77660280-697b-11eb-9397-2a11717fb900.gif)
<br><br>
컨트롤러 구성
---
회원 관련 정보 처리를 위한 HomeController와 게시판 관련 정보 처리를 위한 BoardController로 나누어져있습니다.
#### **Home Controller**
```javascript
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
```
#### **BoardController**
```javascript
package com.hj.myweb;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hj.myweb.dto.BfileDto;
import com.hj.myweb.dto.ReplyDto;
import com.hj.myweb.service.BoardService;

import lombok.extern.java.Log;

@Controller
@Log
public class BoardController {
	@Autowired
	private BoardService bServ;
	
	private ModelAndView mv;
	
	//게시판으로 이동
	@GetMapping("boardList")
	public ModelAndView boardList(Integer pageNum) {
		log.info("boardList()");
		
		mv = bServ.getBoardList(pageNum);
		return mv;
	}
	
	//글쓰기로 이동
	@GetMapping("writeFrm")
	public String writeFrm() {
		log.info("writeFrm()");
		
		return "writeFrm";
	}
	
	//글 내용 보기
	@GetMapping("boardContents")
	public ModelAndView boardContents(Integer bnum) {
		log.info("boardContents() - bnum : " + bnum);
		
		bServ.viewUp(bnum);
		mv = bServ.getContents(bnum);
		
		return mv;
	}
	
	//글 등록하기
	@PostMapping("boardInsert")
	public String boardInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardInsert()");
		
		return bServ.boardInsert(multi, rttr);
	}
	
	//첨부파일 다운로드
	@GetMapping("download")
	public void fileDownload(String sysName, HttpServletRequest request, HttpServletResponse response) {
		log.info("fileDownload() file: " + sysName);
		
		bServ.fileDown(sysName, request, response);
	}
	
	//댓글 작성
	@PostMapping(value = "replyInsert", produces = "application/json; charset=utf-8")
	@ResponseBody
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply){
		Map<String, List<ReplyDto>> rMap = bServ.replyInsert(reply);
		
		return rMap;
	}
	
	//게시글 수정으로 이동
	@GetMapping("modifyFrm")
	public ModelAndView modifyFrm(int bnum, RedirectAttributes rttr) {
		log.info("modifyFrm()");
		
		mv = bServ.getContents(bnum);
		mv.addObject("bnum", bnum);
		mv.setViewName("modifyFrm");
		
		return mv;
	}
	
	//게시글 수정
	@PostMapping("boardUpdate")
	public String boardUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardUpdate()");
		
		return bServ.boardUpdate(multi, rttr);
	}
	
	//파일 개별 삭제
	@PostMapping(value = "delFile", produces = "application/json; charset=utf8")
	@ResponseBody
	public Map<String, List<BfileDto>> delFile(String sysname, int bnum){
		log.info("sysname : " + sysname + ", bnum : " + bnum);
		
		Map<String, List<BfileDto>> fMap = bServ.delFile(sysname, bnum);
		
		return fMap;
	}
	
	//게시글 삭제
	@GetMapping("delBoard")
	public String delBoard(int bnum, RedirectAttributes rttr) {
		log.info("delBoard() - bnum : " + bnum);
		
		return bServ.delBoard(bnum, rttr);
	}
}	
```
Util 패키지 구성
---
#### **Paging**
##### - 페이징 처리를 통해 한 페이지에 10개의 게시글, 한 페이징 그룹당 2개의 페이지를 출력하도록 처리하였습니다.
```javascript
package com.hj.myweb.util;

public class Paging {
	private int maxNum;//전체 글 개수
	private int curPage;//현재 페이지 번호
	private int listCnt;//페이지 당 글 개수
	private int pageCnt;//보여질 페이지번호 개수
	
	//생성자
	public Paging(int maxNum, int curPage, int listCnt, int pageCnt) {
		this.maxNum = maxNum;
		this.curPage = curPage;
		this.listCnt = listCnt;
		this.pageCnt = pageCnt;
	}
	
	//페이징
	public String makePaging() {
		//전체 페이지 개수
		int totalPage = (maxNum % listCnt > 0) ?
				maxNum/listCnt + 1 : maxNum/listCnt;
		
		//현재 페이지의 그룹 번호
		int curGroup = (curPage % pageCnt > 0) ?
				curPage/pageCnt + 1 : curPage/pageCnt;
		
		StringBuffer sb = new StringBuffer();
		
		//현재 그룹의 시작 페이지 번호
		int start = (curGroup * pageCnt) - (pageCnt - 1);
		
		//현재 그룹의 끝페이지 번호
		int end = (curGroup * pageCnt >= totalPage) ?
				totalPage : curGroup * pageCnt;
		
		//이전 버튼 처리
		if(start != 1) {
			sb.append("<nav><ul class='pagination'><li>");
			sb.append("<a href='boardList?pageNum=" + (start - 1) + "' aria-label='Previous'>");
			sb.append("<span aria-hidden='true'>&laquo;</span></a></li>");
		}
		else {
			sb.append("<nav><ul class='pagination'><li class='disabled'>");
			sb.append("<span aria-hidden='true'>&laquo;</span></li>");
		}
		
		//이전과 다음 버튼 사이의 페이지 번호 처리
		for(int i = start; i <= end; i++) {
			if(curPage != i) {
				//페이지 이동을 위한 링크
				sb.append("<li><a href='boardList?pageNum=" + i + "'>");
				sb.append(i + "</a></li>");
			}
			else {
				//현재 페이지 비활성화
				sb.append("<li class='active'><a href=''>");
				sb.append(i + "</a></li>");
			}
		}
		
		//다음 버튼 처리
		if(end != totalPage) {
			sb.append("<li>");
			sb.append("<a href='boardList?pageNum=" + (end + 1) + "' aria-label='Next'>");
			sb.append("<span aria-hidden='true'>&raquo;</span></a></li>");
		}
		else {
			sb.append("<li class='disabled'>");
			sb.append("<span aria-hidden='true'>&raquo;</span></li>");
		}
		
		return sb.toString();
	}
}//class end
```
#### **SessionInterceptor**
##### - Interceptor 기능을 사용하여 로그인을 하지 않은 경우 메인/로그인/회원가입 페이지를 제외한 페이지로의 접근을 차단하고 첫 페이지로 이동하도록 처리하였습니다.
```javascript
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
```
DAO 구성
---
회원 데이터를 처리하는 MemberDao와 게시판 데이터를 처리하는 BoardDao로 나누어져 있으며 각각 MappingInterface와 실제 쿼리문이 작성된 mapper.xml로 구성되어 있습니다.
#### **MemberDao.java**
```javascript
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

```
#### **MemberDao.xml**
```javascript
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper 
   PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
   "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   
<mapper namespace="com.hj.myweb.dao.MemberDao">
	<insert id="regMember" parameterType="com.hj.myweb.dto.MemberDto">
		INSERT INTO M
		VALUES (#{m_id},#{m_pwd},#{m_name},#{m_birth},#{m_addr},#{m_phone},DEFAULT)
	</insert>
	<select id="idCheck" parameterType="String" resultType="Integer">
		SELECT COUNT(*) FROM MINFO WHERE M_ID=#{id}
	</select>
	<select id="getEncPwd" parameterType="String" resultType="String">
		SELECT M_PWD FROM M WHERE M_ID=#{id}
	</select>
	<select id="getMemberInfo" parameterType="String" resultType="com.hj.myweb.dto.MemberDto">
		SELECT * FROM MINFO WHERE M_ID=#{id}
	</select>
</mapper>
```
#### **BoardDao.java**
```javascript
package com.hj.myweb.dao;

import java.util.List;
import java.util.Map;

import com.hj.myweb.dto.BfileDto;
import com.hj.myweb.dto.BoardDto;
import com.hj.myweb.dto.ReplyDto;

public interface BoardDao {
	//게시글 목록 구하기
	public List<BoardDto> getBoardList(int pageNum);
	//게시글 전체 개수 구하기
	public int getBoardCnt();
	//게시글 내용 가져오기
	public BoardDto getContents(Integer bnum);
	//게시글 파일 목록 가져오기
	public List<BfileDto> getBfList(Integer bnum);
	//게시글 댓글 목록 가져오기
	public List<ReplyDto> getReplyList(Integer bnum);
	//게시글 등록
	public void boardInsert(BoardDto board);
	//게시글 파일 등록
	public void fileInsert(Map<String, String> fmap);
	//게시글 댓글 등록
	public void replyInsert(ReplyDto reply);
	//파일 원래 이름 구하기
	public String getOriName(String sysName);
	//게시글 수정
	public void boardUpdate(BoardDto board);
	//조회수 증가
	public void viewUp(Integer bnum);
	//포인트 증가
	public void pointUp(Map<String, String> pMap);
	//파일 개별 삭제
	public void delFile(String sysname);
	//댓글 전체 삭제
	public void delReply(Integer bnum);
	//파일 전체 삭제
	public void delFileList(Integer bnum);
	//게시글 삭제
	public void delBoard(Integer bnum);
	
}

```
#### **BoardDao.xml**
```javascript
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper 
   PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
   "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
   
<mapper namespace="com.hj.myweb.dao.BoardDao">
	<!-- 페이징 처리 -->
	<select id="getBoardList" parameterType="Integer" resultType="com.hj.myweb.dto.BoardDto">
		<![CDATA[
		SELECT * FROM BLIST_1
		WHERE RONUM >= (#{pageNum}*10)-9 AND RONUM <= (#{pageNum}*10)
		]]>
	</select>
	
	<!-- 전체 게시글 개수 가져오기 -->
	<select id="getBoardCnt" resultType="int">
		SELECT COUNT(*) FROM BLIST_1
	</select>
	
	<!-- 게시글 내용 가져오기 -->
	<select id="getContents" parameterType="Integer" resultType="com.hj.myweb.dto.BoardDto">
		SELECT * FROM BLIST
		WHERE BNUM=#{bnum}
	</select>
	
	<!-- 게시글 파일 목록 가져오기 -->
	<select id="getBfList" parameterType="Integer" resultType="com.hj.myweb.dto.BfileDto">
		SELECT * FROM BF
		WHERE BF_BNUM=#{bnum}
	</select>
	
	<!-- 게시글 댓글 목록 가져오기 -->
	<select id="getReplyList" parameterType="Integer" resultType="com.hj.myweb.dto.ReplyDto">
		SELECT * FROM RLIST
		WHERE R_BNUM=#{bnum}
	</select>
	
	<!-- 게시글 저장 -->
	<insert id="boardInsert" parameterType="com.hj.myweb.dto.BoardDto" useGeneratedKeys="true" keyProperty="bnum">
	<selectKey keyProperty="bnum" resultType="int" order="BEFORE">
		SELECT BOARD_SEQ.NEXTVAL FROM DUAL
	</selectKey>
		INSERT INTO B
		VALUES (${bnum}, #{btitle}, #{bcontents}, #{bid}, DEFAULT, DEFAULT)
	</insert>
	
	<!-- 게시글 파일 저장 -->
	<insert id="fileInsert" parameterType="HashMap">
		INSERT INTO BF
		VALUES (BF_SEQ.NEXTVAL,#{bnum},#{oriName},#{sysName})
	</insert>
	
	<!-- 게시글 댓글 저장 -->
	<insert id="replyInsert" parameterType="com.hj.myweb.dto.ReplyDto">
		INSERT INTO R
		VALUES(REPLY_SEQ.NEXTVAL,#{r_bnum},#{r_contents},#{r_id},DEFAULT)
	</insert>
	
	<!-- 파일 원래 이름 구하기 -->
	<select id="getOriName" parameterType="String" resultType="String">
		SELECT BF_ORINAME FROM BF
		WHERE BF_SYSNAME=#{sysName}
	</select>
	
	<!-- 게시글 수정 -->
	<update id="boardUpdate">
		UPDATE B
		SET B_TITLE=#{btitle}, B_CONTENTS=#{bcontents}, B_DATE=SYSDATE
		WHERE B_NUM=#{bnum}
	</update>
	
	<!-- 조회수 증가 -->
	<update id="viewUp" parameterType="int">
		UPDATE B
		SET B_VIEWS = B_VIEWS + 1
		WHERE B_NUM=#{bnum}
	</update>
	
	<!-- 포인트 증가 -->
	<update id="pointUp" parameterType="HashMap">
		UPDATE M
		SET M_POINT=M_POINT+${point}
		WHERE M_ID=#{id}
	</update>
	
	<!-- 파일 개별 삭제 -->
	<delete id="delFile" parameterType="String">
		DELETE FROM BF
		WHERE BF_SYSNAME=#{sysname}
	</delete>
	
	<!-- 댓글 전체 삭제 -->
	<delete id="delReply" parameterType="Integer">
		DELETE FROM R
		WHERE R_BNUM=#{bnum}
	</delete>
	
	<!-- 파일 전체 삭제 -->
	<delete id="delFileList" parameterType="Integer">
		DELETE FROM BF
		WHERE BF_BNUM=#{bnum}
	</delete>
	
	<!-- 게시글 삭제 -->
	<delete id="delBoard" parameterType="Integer">
		DELETE FROM B
		WHERE B_NUM=#{bnum}
	</delete>
	
</mapper>
```
Database 구성
---
#### **회원 정보 처리를 위한 Member 테이블**
![member](https://user-images.githubusercontent.com/26563226/107144461-a8940200-697e-11eb-96a8-025a1bee0f28.JPG)
#### **회원 등급 및 포인트 처리를 위한 Grade 테이블**
![grade](https://user-images.githubusercontent.com/26563226/107144467-adf14c80-697e-11eb-9fef-2a71549816cf.JPG)
#### **게시글 처리를 위한 Board 테이블**
![board](https://user-images.githubusercontent.com/26563226/107144463-aa5dc580-697e-11eb-94d6-c65afc134a8c.JPG)
#### **파일 관리를 위한 Boardfile 테이블**
![boardfile](https://user-images.githubusercontent.com/26563226/107144465-ab8ef280-697e-11eb-86cc-4ba9a8697cd8.JPG)
#### **댓글 처리를 위한 Reply 테이블**
![reply](https://user-images.githubusercontent.com/26563226/107144468-afbb1000-697e-11eb-8348-3afddcf49a71.JPG)
