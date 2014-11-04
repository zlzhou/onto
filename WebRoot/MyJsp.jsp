<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'MyJsp.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" href="css/site.css" type="text/css" charset="UTF-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	
  </head>
  
  <body>
  <div id="arbor">
  <div id="nav"> 
  </div>
  <canvas id="sitemap" width="800" height="400"></canvas>

 
  <script src="js/jquery-1.6.1.min.js"></script>
  <script src="js/jquery.address-1.4.min.js"></script>
  <script src="js/arbor.js"></script>
  <script src="js/arbor-graphics.js"></script>
  <script src="js/arbor-tween.js"></script>
  <script src="js/site.js"></script>
    
 
    
  </body>
</html>
