<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>글쓰기</title>
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
						<span class="label label-warning">포인트 [${user.m_point}]</span>
					</h4>
				</div>
			</div>
			<h2 class="text-center">글쓰기</h2>
			<form class="form-horizontal" action="./boardInsert" method="post" enctype="multipart/form-data">
				<div class="form-group">
					<label for="inputEmail3"
						class="col-sm-offset-1 col-sm-2 control-label">제목</label>
					<div class="col-sm-6">
						<input type="hidden" name="bid" value="${user.m_id}">
						<input type="text" class="form-control" id="inputEmail3" name="btitle"
							placeholder="제목을 입력해 주세요" required>
					</div>
				</div>
				<div class="form-group">
					<label for="inputPassword3"
						class="col-sm-offset-1 col-sm-2 control-label">내용</label>
					<div class="col-sm-6">
						<textarea rows="15" name="bcontents" placeholder="내용을 적어주세요"
							class="form-control" required></textarea>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9">
						<label for="file">파일 업로드</label>
						<input type="file" id="file" name="file" multiple>
						<input type="hidden" id="fileCheck" value="0" name="fileCheck">
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-offset-4 col-sm-4">
						<button type="submit" class="btn btn-default btn-block">완료</button>
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
<script type="text/javascript">
$("#file").on('change', function(){
	var fileName = $("#file").val();
	
	if(fileName == ""){
		$("#fileCheck").val(0);
	}
	else{
		$("#fileCheck").val(1);
	}
});
</script>
</html>