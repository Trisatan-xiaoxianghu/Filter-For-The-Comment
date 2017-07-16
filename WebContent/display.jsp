<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   

	<script src="ckeditor/ckeditor.js"></script>
	<link rel="stylesheet" href="ckeditor/samples/sample.css">
	
  </head>
  <body>
  ${requestScope.content} 

  	<form name="frmDis" action="DisServlet" method="post">
  	  The comment: <textarea class="ckeditor" rows="6" cols="30" name="content"></textarea>
  	  
  	  <input type="submit" value="submit" >
  	</form>

  </body>
   
</html>