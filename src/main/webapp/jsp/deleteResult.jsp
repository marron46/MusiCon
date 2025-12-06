<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/deleteResult.css">
</head>
<div class="reverse">
	<img src="${pageContext.request.contextPath}/png/MusiConLogo.png"
		alt="MusiConのロゴ" width="180" class="icon">
</div>
<body>
	<div class="container">
		<div class="message">
			<h1>
				ユーザーが<br>削除されました
			</h1>
			<h2>
				<%=(String) session.getAttribute("user_name")%>さん<br>グッバイ!!
			</h2>
			<div class="toLoginBtn">
				<a href="${pageContext.request.contextPath}/jsp/login.jsp"> ←ログインへ </a>
			</div>
		</div>
	</div>
</body>
</html>
