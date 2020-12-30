<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script type="text/javascript">
$(function(){
	//로그인에 따른 메뉴 변경
	var id = "${user.m_id}";
	console.log(id);
	if(id != ""){
		$("#mid").html("<a href='#'>" + id + "님</a>");
		$(".after-login").css("display", "block");
		$(".before-login").css("display", "none");	
	}
	
	//메시지 출력
	var chk = "${msg}";
	
	if(chk != ""){
		alert(chk);
		location.reload(true);
	}
});
</script>
</head>
<body>
<header>
<jsp:include page="header.jsp"/>
</header>
<section>
	<div class="container">
		<div class="row">
			<div class="col-md-offset-10 col-md-1 col-xs-offset-6 col-xs-3">
				<h4><span class="label label-primary">등급 [${user.g_name}]</span></h4>
			</div>
			<div class="col-md-1 col-xs-2">
				<h4><span class="label label-warning">포인트 [${user.m_point}]</span></h4>
			</div>
		</div>
		<h2 class="text-center">게시판</h2>
		<table class="table table-bordered">
		<tr>
			<th class="col-md-1 col-xs-2">글번호</th>
			<th class="col-md-5 col-xs-7">글제목</th>
			<th class="col-md-2 col-xs-3">작성자</th>
			<th class="col-md-2 hidden-xs">작성일</th>
			<th class="col-md-1 hidden-xs">조회수</th>
		</tr>
		<c:forEach var="bitem" items="${bList}">
			<tr>
				<td class="col-md-1 col-xs-2 text-center">${bitem.bnum}</td>
				<td class="col-md-5 col-xs-7"><a href="boardContents?bnum=${bitem.bnum}">${bitem.btitle}</a></td>
				<td class="col-md-2 col-xs-3 text-center">${bitem.mname}</td>
				<td class="col-md-2 hidden-xs text-center"><fmt:formatDate value="${bitem.bdate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td class="col-md-1 hidden-xs text-center">${bitem.bviews}</td>
			</tr>
		</c:forEach>
		</table>
		<div class="row">
			<div class="col-md-offset-5 col-md-2">
				<button type="button" class="btn btn-primary btn-lg btn-block" onclick="location.href='./writeFrm'">글쓰기</button>
			</div>
		</div>
		<nav class="text-center">${paging}</nav>
	</div>
</section>
<footer>
<jsp:include page="footer.jsp"/>
</footer>
</body>
</html>