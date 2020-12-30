<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<script type="text/javascript">
	$(function() {
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
		<jsp:include page="header.jsp" />
	</header>
	<section>
		<div class="container">
			<h2 class="text-center">로그인</h2>
			<form class="form-horizontal" action="loginProc" method="post" style="padding-top: 20px;">
				<div class="form-group">
					<label for="id" class="col-sm-2 col-sm-offset-2 control-label">아이디</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="m_id" id="id">
					</div>
				</div>
				<div class="form-group">
					<label for="pwd" class="col-sm-2 col-sm-offset-2 control-label">비밀번호</label>
					<div class="col-sm-4">
						<input type="password" class="form-control" name="m_pwd" id="pwd">
					</div>
				</div>
				<div class="row" style="padding-top: 20px;">
					<div class="col-sm-offset-4 col-sm-4">
						<button type="submit" class="btn btn-primary btn-block btn-lg">로그인</button>
					</div>
				</div>
				<div class="row" style="padding-top: 10px;">
					<div class="col-sm-offset-4 col-sm-4">
						<a class="btn btn-default btn-block btn-lg" href="./joinFrm" role="button">회원가입</a>
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
</html>