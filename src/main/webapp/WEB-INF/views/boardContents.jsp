<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내용 보기</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
		//로그인에 따른 메뉴 변경
		var id = "${user.m_id}";
		console.log(id);
		if (id != "") {
			$("#mid").html("<a href='#'>" + id + "님</a>");
			$(".after-login").css("display", "block");
			$(".before-login").css("display", "none");
		}

		//메시지 출력
		var chk = "${msg}";

		if (chk != "") {
			alert(chk);
			location.reload(true);
		}
		
		//로그인에 따른 버튼 숨기기
		$('#modbtn').hide();
		$('#delbtn').hide();
			
		var uid = "${user.m_id}"//로그인한 아이디
		var wid = "${board.bid}"//작성자 아이디
		
		
		if(uid == wid){
			$('#modbtn').show();
			$('#delbtn').show();
		}
	});
</script>
</head>
<body>
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<section>
		<div class="container">
			<div class="row">
				<div class="col-md-offset-10 col-md-1 col-xs-offset-6 col-xs-3">
					<h4>
						<span class="label label-primary">등급 [${user.g_name}]</span>
					</h4>
				</div>
				<div class="col-md-1 col-xs-2">
					<h4>
						<span class="label label-warning" id="point">포인트 [${user.m_point}]</span>
					</h4>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12">
					<h2>${board.btitle}</h2>
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<h3>
						<small>작성자 : ${board.mname}</small>
					</h3>
				</div>
				<div class="col-md-2 col-xs-6">
					<h3>
						<small><fmt:formatDate value="${board.bdate}" pattern="yyyy-MM-dd HH:mm:ss"/></small>
					</h3>
				</div>
				<div class="col-md-offset-5 col-md-2 col-xs-6 text-right">
					<h3>
						<small>조회수 ${board.bviews}</small>
					</h3>
				</div>
			</div>
			<div class="row" style="padding-top: 30px;">
				<div class="col-md-12">${board.bcontents}</div>
			</div>

			<!-- 첨부파일 목록 -->
			<div class="row col-sm-12">
				<h4 style="padding-top: 50px;">
					<span class="label label-default">첨부파일</span>
				</h4>
			</div>
			<div class="row col-sm-12">
				<c:if test="${empty bfList}">
			첨부된 파일이 없습니다
		</c:if>
				<c:if test="${!empty bfList}"></c:if>
				<ul class="nav nav-pills">
					<c:forEach var="file" items="${bfList}">
						<li role="presentation"><a href="./download?sysName=${file.bf_sysname}">${file.bf_oriname}</a></li>
					</c:forEach>
				</ul>
			</div>

			<!-- 첨부파일 미리보기 -->
			<c:if test="${!empty bfList}">
			<div class="row">
				<c:forEach var="f" items="${bfList}">
					<c:if test="${fn:contains(f.bf_sysname, '.jpg')}">
						<div class="col-sm-1 col-xs-3">
						<img src="resources/upload/${f.bf_sysname}" width="70" class="img-rounded">
						</div>
					</c:if>
				</c:forEach>
			</div>
			</c:if>
			
			<!-- 버튼 -->
			<div class="row">
				<div class="text-center">
					<button type="button" class="btn btn-primary btn-lg" id="modbtn" onclick="location.href='./modifyFrm?bnum=${board.bnum}'">수정</button>
					<button type="button" class="btn btn-primary btn-lg" id="delbtn" onclick="delBoard(${board.bnum})">삭제</button>
					<button type="button" class="btn btn-default btn-lg" onclick="location.href='./boardList?pageNum=${pageNum}'">뒤로</button>
				</div>
			</div>
			
			<!-- 댓글 출력 -->
			<table class="table" style="margin-top: 100px;" id="rtable">
				<tr>
					<th colspan="3">댓글 목록</th>
				</tr>
				<tr>
					<th class="col-sm-2">작성자</th>
					<th class="col-sm-7">내용</th>
					<th class="col-sm-3">시간</th>
				</tr>
				<c:forEach var="r" items="${rList}">
					<tr>
						<td class="text-center">${r.r_id}</td>
						<td>${r.r_contents}</td>
						<td class="text-center"><fmt:formatDate value="${r.r_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					</tr>
				</c:forEach>
			</table>
			
			<!-- 댓글 작성 -->
			<form class="form-horizontal" id="replyFrm">
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-6">
						<textarea class="form-control" rows="3" name="r_contents"
							id="comment" placeholder="댓글을 작성해주세요"></textarea>
					</div>
				</div>

				<div class="form-group">
					<div class="col-sm-offset-8 col-sm-4">
						<input type="button" value="입력" class="btn btn-default" onclick="replyInsert(${board.bnum})">
					</div>
				</div>
			</form>
		</div>
	</section>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
<script src="resources/js/jquery.serializeObject.js"></script>
<script type="text/javascript">
function replyInsert(bnum){
	//form 데이터를 json으로 변환
	var replyFrm = $("#replyFrm").serializeObject();
	
	replyFrm.r_bnum = bnum;//글번호 추가
	replyFrm.r_id = "${user.m_id}"//작성자(로그인한) 아이디 추가
	
	console.log(replyFrm);
	
	$.ajax({
		url: "replyInsert",
		type: "post",
		data: replyFrm,
		dataType: "json",
		success: function(data){
			var rlist = '<tbody><tr><th colspan="3">댓글 목록</th></tr>' +
				'<tr><th class="col-sm-2">작성자</th>' +
				'<th class="col-sm-7">내용</th>' +
				'<th class="col-sm-3">시간</th></tr>';
			var dlist = data.rList;
			
			for(var i = 0; i < dlist.length; i++){
				rlist += '<tr><td class="text-center">' + dlist[i].r_id + '</td>'
				+'<td>' + dlist[i].r_contents + '</td>'
				+'<td class="text-center">' + dlist[i].r_date + '</td></tr>';
			}
			rlist += "</tbody>"
			$('#rtable').html(rlist);
		},
		error: function(error){
			console.log(error);
			alert("댓글 입력 실패");
		}
	});
	
	$("#comment").val("");
}
function delBoard(bnum){
	var con = confirm("삭제하시겠습니까?");
	if(con){
		location.href="./delBoard?bnum=" + bnum;
	}
}
</script>
</html>