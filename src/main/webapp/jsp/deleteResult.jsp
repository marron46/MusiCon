<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="css/deleteResult.css">
</head>
<body>
	<div class="reverse">
		<a href="top.jsp">TOP</a>
	</div>
	<div class="container">
		<div class="message">
			<h1>ユーザーが<br>削除されました</h1>
			<h2>
				<%=(String) session.getAttribute("user_name")%>さん<br>グッバイ!!
			</h2>
		</div>
	</div>
</body>
</html>
