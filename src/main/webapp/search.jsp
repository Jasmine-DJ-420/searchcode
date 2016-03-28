<%@ page import="act._es" %>
<%@ page import="org.elasticsearch.search.SearchHit" %>

<jsp:useBean id="es" class="act._es" scope="session">
<% es.connection(); %>
</jsp:useBean>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <script type="text/javascript" src="./Scripts/search.js"></script>
  <link rel="stylesheet" href="./vendor/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="./vendor/jquery-ui-1.8.18.custom/jquery-ui-1.8.18.custom.css">
  <link rel="stylesheet" href="./css/facetview.css">
  <link rel="stylesheet" href="./font-awesome-4.5.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="./Styles/search_page.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Code Search</title>
</head>
<body>
<% String url = request.getRequestURI() + "?" + request.getQueryString().split("&page=")[0];
   String query = request.getParameter("wd");
   request.setAttribute("query", query);
   String curpage = request.getParameter("page");
   if(curpage == null){
	   es.search(query);
	   curpage = "1";
	   es.getTotalPage();
   }
   long TotalPage = es.get_max_page();
   int c_page = Integer.parseInt(curpage);
   SearchHit[] hits = es.getData(c_page);
   request.setAttribute("hits", hits);
   
   
   request.setAttribute("page",c_page);
   request.setAttribute("TotalPage", TotalPage);
   request.setAttribute("url",url);
   
   %>
	<div id="searchbar" class="searchbar_outer">
		<table class="searchtable" align="left" >
			<tr align="left">
				<td>
					<div class="a_link" style="float: left;">
						<a href="./">Code Search</a>
					</div>
				</td>
			</tr>
			<tr align="center">
				<td>
					<div class="search_box_outer">
					<input class="search_box" type="text" name="query" placeholder="Search..."
						id="query" value="${query}" onkeyup="handlekey('HomePage')" /> 
					<button class="icon" onclick="javascript:SubmitSearch('')"><i class="fa fa-search"></i></button>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="content-result-body">
		<div id="hit-list">
			<c:forEach items = "${hits}" var="hit">
			<div class="hit">
				<div class="hit-title">
					<a href="${hit.getSource().get('url')}">Project: ${hit.getSource().get("projectName")}</a>
				</div>
				<div class="hit-data">
					<dl>
					<dt class="username"> Username: ${hit.getSource().get("userName")}</dt>
					<dt class="filename"> Filename: ${hit.getSource().get("fileName")}</dt>
					</dl>
				</div>
				<div class="hit-summary">
				</div>
			</div>
			</c:forEach>
		</div>	
		
		<div class="paging"> 
		 <a href="${url}&page=1">Fir</a>
		 <c:if test="${page>1}">   
         <a href="${url}&page=${page-1}">Pre</a></c:if>  
         <a href="${url}&page=${page+1}">Next</a>  
         <a href="${url}&page=${TotalPage}">Last</a>  
         <br/>  
		</div>
	</div>
	<div id="footer">
		<p class="copyright">
			@2015 buaa.act
		</p>
	</div>
</body>
<% es.close(); %>
</html>