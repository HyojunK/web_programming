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
