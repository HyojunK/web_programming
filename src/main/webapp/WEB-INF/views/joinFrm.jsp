<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="resources/css/bootstrap.css">
<link rel="stylesheet" href="resources/css/additional.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
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
			<h2 class="text-center">회원 가입</h2>
			<form class="form-horizontal" action="regMember" method="post" style="padding-top: 20px;">
				<div class="form-group">
					<label for="id" class="col-sm-2 col-sm-offset-2 control-label">아이디</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" name="m_id" id="id" required>
					</div>
					<div class="col-sm-1" style="padding-left: 0;">
						<input type="button" class="btn btn-default" value="중복확인" onclick="idCheck()" required>
					</div>
				</div>
				<div class="form-group">
					<label for="pwd" class="col-sm-2 col-sm-offset-2 control-label">비밀번호</label>
					<div class="col-sm-4">
						<input type="password" class="form-control" name="m_pwd" id="pwd" required>
					</div>
				</div>
				<div class="form-group">
					<label for="name" class="col-sm-2 col-sm-offset-2 control-label">이름</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="m_name" id="name" required>
					</div>
				</div>
				<div class="form-group">
					<label for="birth" class="col-sm-2 col-sm-offset-2 control-label">생년월일</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name="m_birth" id="birth"
							placeholder="예) 920101" required>
					</div>
				</div>
				<div class="form-group">
					<label for="addr" class="col-sm-2 col-sm-offset-2 control-label">주소</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name = "m_addr" id="addr" required>
					</div>
				</div>
				<div class="form-group">
					<label for="phone" class="col-sm-2 col-sm-offset-2 control-label">전화번호</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" name = "m_phone" id="phone" placeholder="' - '를 제외한 숫자만 입력해주세요" 
						required>
					</div>
				</div>
				<div class="row" style="padding-top: 20px;">
					<div class="col-sm-offset-4 col-sm-4">
						<button type="submit" class="btn btn-primary btn-block btn-lg">가입</button>
					</div>
				</div>
				<div class="row" style="padding-top: 10px;">
					<div class="col-sm-offset-4 col-sm-4">
						<button type="reset" class="btn btn-default btn-block btn-lg">취소</button>
					</div>
				</div>
			</form>
		</div>
	</section>
	<footer>
		<jsp:include page="footer.jsp" />
	</footer>
</body>
<script type="text/javascript">
//아이디 중복확인
function idCheck(){
	var id = $('#id').val();
	console.log(id);
	if(id == ""){
		alert("아이디를 입력하세요");
		$('#id').focus();
		return;
	}
	
	var ckObj = {"id": id};
	console.log(id);
	
	$.ajax({
		url: "idCheck",
		type: "get",
		data: ckObj,
		success: function(data){
			if(data == "success"){
				alert('사용 가능한 ID입니다');
			}
			else{
				alert('이미 존재하는 ID입니다');
				$('#id').val('');//입력 초기화
				$('#id').focus();
			}
		},
		error: function(error){
			console.log(error);
		}
	});
}
</script>
</html>