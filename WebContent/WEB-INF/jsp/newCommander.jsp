<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%
List<String> traitValueList = (List<String>) session.getAttribute("traitValueList");
List<String> traitNameList = (List<String>) session.getAttribute("traitNameList");
List<String> countryValueList = (List<String>) session.getAttribute("countryValueList");
List<String> countryNameList = (List<String>) session.getAttribute("countryNameList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>陸軍司令官作成</title>
<style>
label{margin-right: 20px;}
</style>
</head>
<body>
	<form action="/commander/NewCommander" method="post">
		名前:<input type="text" name="name"><br />
		スキル:<input type="number" name="skill"><br />
		攻撃:<input type="number" name="attack"><br />
		防御:<input type="number" name="defense"><br />
		計画:<input type="number" name="planning"><br />
		兵站:<input type="number" name="logistics"><br />
		特性<br />
		<% for(int i=0; i<traitNameList.size(); i++) { %>
			<label for="check"><input type="checkbox" name="trait" value=<%= traitValueList.get(i) %>><%= traitNameList.get(i) %></label>
		<% } %>
			<br />
		国:
		<select name="country">
		<% for(int i=0; i<countryNameList.size(); i++) { %>
			<option value=<%= countryValueList.get(i) %>><%= countryValueList.get(i) %></option>
		<% } %>
		</select>
			<br />
		<input type="submit" value="作成">
	</form>
	<a href="/commander/index.html">戻る</a>
</body>
</html>