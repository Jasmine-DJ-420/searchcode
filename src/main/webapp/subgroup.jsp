<%@ page import="act.CodeClone" %>
<%@ page import="act.SaxXML" %>
<%@ page import="act.SearchHit2" %>
<%@ page import="java.util.ArrayList" %>
<jsp:useBean id="clone" class="act.CodeClone" scope="session">
</jsp:useBean>

<jsp:useBean id="codecontent" class="act._content" scope="page">
</jsp:useBean>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script type="text/javascript" src="./Scripts/search.js"></script>
  <script type="text/javascript" src="./vendor/jquery/1.7.1/jquery.js"></script>
  <script type="text/javascript" src="./vendor/linkify/1.0/plugins/jquery.highlight-5.js"></script>
  <link rel="stylesheet" href="./vendor/bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="./vendor/jquery-ui-1.8.18.custom/jquery-ui-1.8.18.custom.css">
  <link rel="stylesheet" href="./css/facetview.css">
  <link rel="stylesheet" href="./font-awesome-4.5.0/css/font-awesome.min.css">
  
  <link rel="stylesheet" href="./bootstrap-3.3.6-dist/css/bootstrap.min.css">
  <link rel="stylesheet" href="./bootstrap-3.3.6-dist/css/bootstrap-theme.min.css">
  <script type="text/javascript" src="./bootstrap-3.3.6-dist/js/bootstrap.min.js"></script>
  <link rel="stylesheet" href="./Styles/search_page.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Code Search</title>
</head>
<body>
<%
	String query = request.getParameter("query");
	request.setAttribute("query", query);
	
	clone.init();
	
	SaxXML sax = clone.get_sax();
	
	int[] nclones = sax.get_page(1);
	int number = sax.get_number();
	ArrayList<SearchHit2>[] shs = sax.getsh2();
	
	request.setAttribute("nclones", nclones);
	request.setAttribute("shs",shs);
	request.setAttribute("groups", number);
	
%>

	<nav class="navbar navbar-default navbar-fixed-top"  role="navigation">
        <div class="navbar-header">
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
			<a class="navbar-brand" id="title-search" href="www.baidu.com">Subgroups:</a>
        </div>
        <div id="navbarCollapse" class="collapse navbar-collapse">
            <form role="search" class="navbar-form navbar-left">
                <div class="form-group">
                    <input class="form-control" type="text" name="query" placeholder="Search..."
						id="query" value="${query}" onkeypress="EnterSearch()" onkeyup="handlekey('HomePage')" />
                </div>
                <button class="btn btn-default" onclick="javascript:SubmitSearch('')"><i class="fa fa-search"></i></button> 
            </form>
            <button class="btn btn-default" id="subgroup" onclick="javascript:SubmitGroupDivide('${url_def}')">Subgroup</button>
        </div>
    </nav>
    
    <div class="container">
	<div class="row">
	<div class="col-1">
		<div id="hits-list">
		<h4>About ${groups} groups</h4>
			<c:forEach items="${nclones}" var="nclone" varStatus="status">	
				<div class="hits-title">
					Class:${status.index+1} (${nclone} fragments)
				</div>
				<div class="hits-summary">
				<c:forEach items="${shs[status.index]}" var="sh2" varStatus="status_2">						
					<c:set target = "${clone}" property="content" value="${sh2.getContent()}" />
						<div class="hit">			
							<div class="hit-title">
								<a href=${sh2.getSH().get("file_url")}>${sh2.getSH().get("fileName")}</a>
							</div>
							<dl class="hit-data">
								<dt>Project: </dt>
								<dd><a href="${sh2.getSH().get('project_url')}">${sh2.getSH().get("projectName")}</a></dd>
								<dd>  |  </dd>
								<dt>Author: </dt>
								<dd><a href="https://github.com/${sh2.getSH().get('userName')}">${sh2.getSH().get("userName")}</a></dd>
								<dd>  |  </dd>
								<dt>Start_line: </dt>
								<dd>${sh2.getSL()}</dd>
								<dd>  |  </dd>
								<dt>End_line: </dt>
								<dd>${sh2.getEL()}</dd>
								<c:if test="${status_2.index<=0}">
								<dd>  |  </dd>
								<a href="#">Hide ${nclone} matches</a>
								</c:if>
							</dl>
							<div class="hit-summary">
							<%String[] codes = clone.get_content();%>
								<c:set var = "index" value = "code-content${status.index}+${status_2.index}" />
								<table id ="${index}"></table>		
								<script type="text/javascript">	
									var codes = new Array();
									<%for(int i=0;i<codes.length;i++){%>
									var code = '<%=codes[i]%>';
									codes[<%=i%>]=code;
									<%}%>
									fullTable('<c:out value="${index}"/>',codes,'${sh2.getSL()}');
									$('td#code-row').highlight('<%=query %>');
								</script>
							</div>
							<hr>
						</div>	
				</c:forEach>
				</div>	
				<hr class="spacer">
			</c:forEach>
		</div>
		</div>
		</div>
		</div>

</body>
</html>