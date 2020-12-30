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
