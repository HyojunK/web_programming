<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="true"%>
<html>
<head>
<title>Home</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.css">
<script src="https://cdn.jsdelivr.net/bxslider/4.2.12/jquery.bxslider.min.js"></script>
<script type="text/javascript">
$(function(){
	//메시지 출력
	var chk = "${msg}";
	
	if(chk != ""){
		alert(chk);
		location.reload(true);
	}
	
	//로그인에 따른 메뉴 변경
	var id = "${user.m_id}";
	console.log(id);
	if(id != ""){
		$("#mid").html("<a href='#'>" + id + "님</a>");
		$(".after-login").css("display", "block");
		$(".before-login").css("display", "none");	
	}
	
	//bx-slider
	$('.slider').bxSlider({
		auto: true,
		slideWidth: 600,
	});
	
	var mql = window.matchMedia("screen and (max-width: 768px)");
	mql.addListener(function(e){
		if(!e.matches){
			slider.reloadSlider();
		}
	});
});
</script>
<body>
	<header>
		<jsp:include page="header.jsp" />
	</header>
	<section>
		<div class="container">
			<div class="jumbotron">
				<h1>환영합니다!</h1>
				<p>Bootstrap과 Spring을 활용한 회원가입, 로그인 및 게시판 기능 구현 사이트 입니다</p>
				<p>
					<a class="btn btn-primary btn-lg" role="button" onclick="toBoard('${user.m_id}')">게시판 이동</a>
				</p>
			</div>
			<div class="slider">
				<div><img src="resources/img/aurora.jpg"></div>
				<div><img src="resources/img/iceland.jpg"></div>
				<div><img src="resources/img/meerkats.jpg"></div>
				<div><img src="resources/img/poppies.jpg"></div>
			</div>
		</div>
	</section>
	<footer>
		<jsp:include page="footer.jsp"/>
	</footer>
</body>
<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
<script type="text/javascript">
function toBoard(id){
	if(id == ""){
		alert("로그인이 필요합니다");
	}
	else {
		location.href="./boardList";
	}
}
</script>
</html>
