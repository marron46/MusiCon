<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	// ログイン済みの場合はtop.jspにリダイレクト
	String userName = (String) session.getAttribute("user_name");
	if (userName != null) {
		response.sendRedirect(request.getContextPath() + "/PlayMusic");
		return;
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MusiCon</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/top.css">
</head>
<body>
	<div class="main">
		<div class="container" style="text-align: center; padding: 50px;">
			<h1 style="font-size: 3em; margin-bottom: 50px;">MusiCon</h1>

			<div style="display: flex; flex-direction: column; gap: 20px; align-items: center;">
				<a href="${pageContext.request.contextPath}/jsp/login.jsp" class="menu" style="display: inline-block; padding: 15px 40px; background-color: #4CAF50; color: white; text-decoration: none; border-radius: 5px; font-size: 1.2em;">ログイン</a>

				<a href="${pageContext.request.contextPath}/jsp/createUser.jsp" class="menu" style="display: inline-block; padding: 15px 40px; background-color: #2196F3; color: white; text-decoration: none; border-radius: 5px; font-size: 1.2em;">新規登録</a>

				<a href="${pageContext.request.contextPath}/jsp/deleteUser.jsp" class="menu" style="display: inline-block; padding: 15px 40px; background-color: #f44336; color: white; text-decoration: none; border-radius: 5px; font-size: 1.2em;">アカウント削除</a>
			</div>
		</div>
	</div>
</body>
</html>