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
