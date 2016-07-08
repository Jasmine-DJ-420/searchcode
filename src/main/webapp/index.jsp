<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="act._es" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <script type="text/javascript" src="./Scripts/search.js"></script>
  <link rel="stylesheet" href="./vendor/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="./vendor/jquery-ui-1.8.18.custom/jquery-ui-1.8.18.custom.css">
  <link rel="stylesheet" href="./css/facetview.css">
  <link rel="stylesheet" href="./font-awesome-4.5.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="./Styles/index_page.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Code Search</title>
</head>
<body class="main_body">
	<div id="searchbar" class="searchbar">
		<table align="center" style="width: 785px" cellpadding="20px"> 
			<tr align="center">
				<td>Code Search</td>
				
			</tr>
			<tr>
				<td>
					<div class="search_box_outer">
					<input class="search_box" type="text" name="query" placeholder="Search..."
						id="query" value="" onkeypress="EnterSearch()" onkeyup="handlekey('HomePage')" />
					<button class="icon" onclick="javascript:SubmitSearch()" ><i class="fa fa-search"></i></button>
					</div>
				</td>
			</tr>
		</table>
	</div>
	<div class="footer">
		<p class="copyright">
			@2016 buaa.act
		</p>
	</div>
</body>
</html>