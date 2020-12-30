<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 탈퇴</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
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

	if (chk != "") {
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
	<h2 class="text-center">회원 탈퇴</h2>
	<form class="form-horizontal" action="resignMember" method="post" style="padding-top: 20px;">
				<div class="form-group">
					<label for="pwd" class="col-sm-2 col-sm-offset-2 control-label">비밀번호 확인</label>
					<div class="col-sm-4">
						<input type="hidden" name="m_id" value="${user.m_id}">
						<input type="password" class="form-control" name="m_pwd" id="pwd" placeholder="비밀번호를 입력하세요" required>
					</div>
				</div>
				
				<div class="row" style="padding-top: 20px;">
					<div class="col-sm-offset-4 col-sm-4">
						<button type="submit" class="btn btn-primary btn-block btn-lg">확인</button>
					</div>
				</div>
				<div class="row" style="padding-top: 10px;">
					<div class="col-sm-offset-4 col-sm-4">
						<input type="button" class="btn btn-default btn-block btn-lg" value="취소" onclick="javascript:history.back();">
					</div>
				</div>
			</form>

</div>
</section>
<footer>
<jsp:include page="footer.jsp"/>
</footer>
</body>
</html>