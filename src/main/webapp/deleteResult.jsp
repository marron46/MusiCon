<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ユーザー削除</title>
<%-- cssの連携 --%>
<link rel="stylesheet" href="webapp/css/style.css">
</head>
<body>
	<a href="top.jsp" class="top">TOP</a>
	<p>
		<%=(String)session.getAttribute("user_name")%>さんはじめまして
	</p>
</body>
</html>
