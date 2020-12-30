package com.hj.myweb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hj.myweb.dao.BoardDao;
import com.hj.myweb.dao.MemberDao;
import com.hj.myweb.dto.BfileDto;
import com.hj.myweb.dto.BoardDto;
import com.hj.myweb.dto.MemberDto;
import com.hj.myweb.dto.ReplyDto;
import com.hj.myweb.util.Paging;

import lombok.extern.java.Log;

@Service
@Log
public class BoardService {
	@Autowired
	private BoardDao bDao;
	
	@Autowired
	private MemberDao mDao;
	
	@Autowired
	private HttpSession session;
	
	private ModelAndView mv;
	
	//게시글 목록 가져오기
	public ModelAndView getBoardList(Integer pageNum) {
		log.info("getBoardList() - pageNum: " + pageNum);
		
		mv = new ModelAndView();
		
		int num = (pageNum == null || pageNum < 1) ? 1 : pageNum;
		
		List<BoardDto> bList = bDao.getBoardList(num);
		
		//DB에서 조회한 데이터 목록을 모델에 추가
		mv.addObject("bList", bList);
		
		//페이징 처리
		mv.addObject("paging", getPaging(num));
		
		//세션에 페이지 번호 저장
		session.setAttribute("pageNum", num);
		
		mv.setViewName("boardList");
		
		return mv;
	}
	
	//페이징 처리
	private Object getPaging(int num) {
		//전체 글 개수
		int maxNum = bDao.getBoardCnt();
		//페이지 당 글 개수
		int listCnt = 10;
		//그룹당 페이지 개수
		int pageCnt = 2;
		
		Paging paging = new Paging(maxNum, num, listCnt, pageCnt);
		
		String pagingHtml = paging.makePaging();
		
		return pagingHtml;
	}
	
	//게시글 내용 가져오기
	public ModelAndView getContents(Integer bnum) {
		mv = new ModelAndView();

		//글 내용 가져오기
		BoardDto board = bDao.getContents(bnum);
		//파일 목록 가져오기
		List<BfileDto> bfList = bDao.getBfList(bnum);
		//댓글 목록 가져오기
		List<ReplyDto> rList = bDao.getReplyList(bnum);
		//모델에 데이터 담기
		mv.addObject("board", board);
		mv.addObject("bfList", bfList);
		mv.addObject("rList", rList);
		
		mv.setViewName("boardContents");
		
		return mv;
	}
	
	//게시글 등록
	@Transactional
	public String boardInsert(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardInsert()");
		
		String view = null;
		
		String id = multi.getParameter("bid");
		String title = multi.getParameter("btitle");
		String contents = multi.getParameter("bcontents");
		String check = multi.getParameter("fileCheck");
		
		//textarea 문자열 앞뒤 공백 제거
		contents = contents.trim();
		
		BoardDto board = new BoardDto();
		board.setBid(id);
		board.setBtitle(title);
		board.setBcontents(contents);
		
		try {
			bDao.boardInsert(board);
			
			//포인트 증가(100포인트까지만)
			MemberDto member = (MemberDto)session.getAttribute("user");
			if(member.getM_point() < 99) {
				Map<String, String> pMap = new HashMap<String, String>();
				pMap.put("id", id);
				pMap.put("point", "2");
				
				bDao.pointUp(pMap);
				
				member = mDao.getMemberInfo(id);
				session.setAttribute("user", member);
			}
			else if(member.getM_point() == 99) {
				Map<String, String> pMap = new HashMap<String, String>();
				pMap.put("id", id);
				pMap.put("point", "1");
				
				bDao.pointUp(pMap);
				
				member = mDao.getMemberInfo(id);
				session.setAttribute("user", member);
			}
			
			//업로드한 파일이 존재할 때 파일 업로드
			if(check.equals("1")) {
				fileUp(multi, board.getBnum());
			}
			
			view = "redirect:boardList";
			rttr.addFlashAttribute("msg", "게시글이 등록되었습니다");
			
		} catch (Exception e) {
			//e.printStackTrace();
			view = "redirect:writeFrm";
			rttr.addFlashAttribute("msg", "게시글 등록에 실패하였습니다");
		}
		
		return view;
	}
	
	//파일 업로드
	public void fileUp(MultipartHttpServletRequest multi, int bnum) throws Exception {
		//저장공간에 대한 절대 경로 구하기
		String path = multi.getSession().getServletContext().getRealPath("/");
		
		path += "resources/upload/";
		log.info(path);
		
		File dir = new File(path);
		
		//해당 경로 디렉토리(폴더)가 존재하지 않으면 디렉토리 생성
		if(dir.isDirectory() == false) {
			dir.mkdir();
		}
		
		//실제 파일명과 저장 파일명을 함께 관리
		Map<String, String> fmap = new HashMap<String, String>();
		
		fmap.put("bnum", String.valueOf(bnum));
		System.out.println("bnum : " + fmap.get("bnum"));
		
		//파일 전송시 기본 값 = 파일 다중 선택
		//멀티파트 요청 객체에서 꺼내올 때는 List를 사용
		List<MultipartFile> fList = multi.getFiles("file");
		
		for(int i = 0; i < fList.size(); i++) {
			MultipartFile mf = fList.get(i);
			//실제 파일명 저장
			String on = mf.getOriginalFilename();
			System.out.println("oriName : " + on);
			fmap.put("oriName", on);
			
			//변경된 파일명 저장
			String sn = System.currentTimeMillis() + "." + on.substring(on.lastIndexOf(".") + 1);
			System.out.println("sysName : " + sn);
			fmap.put("sysName", sn);
			
			//해당 폴더에 파일 저장하기
			mf.transferTo(new File(path + sn));
			
			bDao.fileInsert(fmap);
		}
	}
	
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

	//댓글 입력
	@Transactional
	public Map<String, List<ReplyDto>> replyInsert(ReplyDto reply) {
		Map<String, List<ReplyDto>> rMap = null;
		
		try {
			bDao.replyInsert(reply);
			
			//포인트 증가
			MemberDto member = (MemberDto)session.getAttribute("user");
			if(member.getM_point() < 100) {
				Map<String, String> pMap = new HashMap<String, String>();
				String id = member.getM_id();
				pMap.put("id", id);
				pMap.put("point", "1");
				
				bDao.pointUp(pMap);
				
				member = mDao.getMemberInfo(id);
				session.setAttribute("user", member);
			}
			//댓글 목록을 다시 검색
			List<ReplyDto> rList = bDao.getReplyList(reply.getR_bnum());
			rMap = new HashMap<String, List<ReplyDto>>();
			rMap.put("rList", rList);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return rMap;
	}

	//게시글 수정
	@Transactional
	public String boardUpdate(MultipartHttpServletRequest multi, RedirectAttributes rttr) {
		log.info("boardUpdate()");
		
		String view = null;
		int num = Integer.parseInt(multi.getParameter("bnum"));
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
			
			if(check.contentEquals("1")) {
				fileUp(multi, board.getBnum());
			}
			
			view = "redirect:boardList";
			rttr.addFlashAttribute("msg", "수정되었습니다");
		} catch (Exception e) {
			//e.printStackTrace();
			int pageNum = (int)session.getAttribute("pagenum");
			view = "redirect:boardList?pageNum=" + pageNum;
			rttr.addFlashAttribute("msg", "수정에 실패하였습니다");
		}
		
		return view;
	}
	
	//조회수 증가
	public void viewUp(Integer bnum) {
		bDao.viewUp(bnum);
	}

	//개별 파일 삭제
	@Transactional
	public Map<String, List<BfileDto>> delFile(String sysname, int bnum) {
		Map<String, List<BfileDto>> fMap = null;
		String path = session.getServletContext().getRealPath("/");
		
		log.info(path);
		path += "resources/upload/" + sysname;
		
		try {
			bDao.delFile(sysname);
			
			File file = new File(path);
			
			if(file.exists()) {
				file.delete();
				
				List<BfileDto> fList = bDao.getBfList(bnum);
				fMap = new HashMap<String, List<BfileDto>>();
				fMap.put("fList", fList);
			}
		} catch (Exception e) {
			//e.printStackTrace();
		}
		
		return fMap;
	}
	
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

}//class end
