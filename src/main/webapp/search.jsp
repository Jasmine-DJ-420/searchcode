<%@ page import="act._es" %>
<%@ page import="org.elasticsearch.search.SearchHit" %>

<jsp:useBean id="es" class="act._es" scope="session">
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
<% String url = request.getRequestURI() + "?" + request.getQueryString().split("&page=")[0];
   String query = request.getParameter("query");
   request.setAttribute("query", query);
   String curpage = request.getParameter("page");
   String def = request.getParameter("def");
   String[] def_str;
   boolean class_checked = false;
   boolean method_checked = false;
   boolean extend_checked = false;
   boolean comment_checked = false;
   boolean implement_checked = false;
   boolean import_checked = false;
   if(def!=null){
	   def_str = def.split(",");
	   for(String def_temp : def_str){
		   if(def_temp.equals("class"))
			   class_checked = true;
		   else if(def_temp.equals("method"))
			   method_checked = true;
		   else if(def_temp.equals("extend"))
			   extend_checked = true;
		   else if(def_temp.equals("comment"))
			   comment_checked = true;
		   else if(def_temp.equals("implement"))
			   implement_checked = true;
		   else if(def_temp.equals("import"))
			   import_checked = true;
	   }
   }

   es.connection();

   
   if(curpage == null){
	   es.search(query);
	   
	   curpage = "1";
	   es.getTotalPage();
   }
  
   int c_page = Integer.parseInt(curpage);
   if(def!=null){
	   es.search(query, def, c_page);
	   es.getTotalPage();
   }
   else if(c_page!=1){
	   es.search(query, "content", c_page);
	   es.getTotalPage();
   }
   long TotalPage = es.get_max_page();
  
   SearchHit[] hits = es.getData();
   long lines = es.get_lines();
   request.setAttribute("hits", hits);
   request.setAttribute("lines",lines);
   
   
   request.setAttribute("page",c_page);
   request.setAttribute("TotalPage", TotalPage);
   request.setAttribute("url",url);
   if(def!=null){
	   request.setAttribute("url_def","&def="+def);
   }
   else{
	   request.setAttribute("url_def","");
   }
   
   request.setAttribute("class_number", es.getCla());
   request.setAttribute("comment_number", es.getComm());
   request.setAttribute("extend_number", es.getExt());
   request.setAttribute("method_number", es.getMe());
   request.setAttribute("implement_number", es.getImpl());
   request.setAttribute("import_number", es.getIm());
   
   request.setAttribute("class_checked", class_checked);
   request.setAttribute("method_checked", method_checked);
   request.setAttribute("extend_checked", extend_checked);
   request.setAttribute("comment_checked", comment_checked);
   request.setAttribute("import_checked", import_checked);
   request.setAttribute("implement_checked", implement_checked);
   
   %>
   	
	<nav class="navbar navbar-default navbar-fixed-top"  role="navigation">
        <div class="navbar-header">
            <button type="button" data-target="#navbarCollapse" data-toggle="collapse" class="navbar-toggle">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
			<a class="navbar-brand" id="title-search" href="www.baidu.com">Code Search</a>
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
		<div id="hit-list">
			<h4>About ${lines} results, ${TotalPage} pages</h4>
			<c:forEach items = "${hits}" var="hit" varStatus="status">
			<div class="hit">			
				<div class="hit-title">
					<a href=${hit.getSource().get("file_url")}>${hit.getSource().get("fileName")}</a>
				</div>
				<dl class="hit-data">
					<dt>Project: </dt>
					<dd><a href="${hit.getSource().get('project_url')}">${hit.getSource().get("projectName")}</a></dd>
					<dd>  |  </dd>
					<dt>Author: </dt>
					<dd><a href="https://github.com/${hit.getSource().get('userName')}">${hit.getSource().get("userName")}</a></dd>
				</dl>
				<div class="hit-summary${status.index}" id="summary-id">
					
					<c:set target = "${codecontent}" property="content" value='${hit.getSource().get("content")}' />
					<%String[] codes = codecontent.splitContent();%>
					<c:set var = "index" value = "code-content${status.index}" />
					<table id ="${index}"></table>		
					<script type="text/javascript">	
					var codes = new Array();
					<%for(int i=0;i<codes.length;i++){%>
						var code = '<%=codes[i]%>';
						codes[<%=i%>]=code;
					<%}%>
					fullTable('<c:out value="${status.index}"/>',codes,1);
					$('td#code-row'+'<c:out value="${index}"/>').highlight('<%=query %>');
					</script>	
				</div>
				<hr class="spacer">
			</div>
			</c:forEach>
		</div>	
		
		<script type="text/javascript">
		function gotoSelectedPage()
		{  
			var paging = document.getElementsByName("pageNumber")[0].value;
			var url = '${url}';
			window.location.href = url+"&page="+paging+'${url_def}';
		} 
		</script>		
		<form id="paging"> 
		 <a href="${url}&page=1${url_def}"><u>Fir</u></a>
		 <a>&nbsp</a>
		 <c:if test="${page>1}">   
         <a href="${url}&page=${page-1}${url_def}"><u>Prev</u></a>
         <a>&nbsp</a></c:if>        
         <u>Go To Page</u>
         <select name="pageNumber" onchange="gotoSelectedPage()">  
    		<c:forEach begin="1" end="${TotalPage}" step="1" var="pageIndex">  
        	<c:choose>  
            <c:when test="${pageIndex eq page}">  
                <option value="${pageIndex}" selected="selected">${pageIndex}</option>  
            </c:when>  
            <c:otherwise>  
                <option value="${pageIndex}">${pageIndex}</option>  
            </c:otherwise>  
        </c:choose>  
    	</c:forEach>  
    	</select>
    	<a>&nbsp</a>
         <c:if test="${page<TotalPage}">  
         <a href="${url}&page=${page+1}${url_def}"><u>Next</u></a><a>&nbsp</a></c:if>  
         <a href="${url}&page=${TotalPage}${url_def}"><u>Last</u></a><a>&nbsp</a>  
         <br/>  
		</form>
		</div>
		<script type="text/javascript">
		function DefinitionFilter(){
			var checked_def = document.forms["form_def"]["definition"];
			var checked_array = new Array();
			
			for (var i=0; i<checked_def.length;++i){
				if(checked_def[i].checked){
					checked_array[checked_array.length] = checked_def[i].value;
				}
			}
			var str="&def=";
			if(checked_array.length==0){
				alert("Please choose a definition filter!");
			}
			else{
				str = str + checked_array[0];
			}
			for (var j=1; j<checked_array.length;j++){
				str= str + "," + checked_array[j];
			}
			if(checked_array.length!=0){
				var url_che = '${url}'+"&page=1" + str;
				window.location.href = url_che;
			}
		}
		
		function ClearFilter(){
			window.location.href = '${url}';
		}
		</script>
		<div class = "col-2" >
			<form class="form" name="form_def">
			<input type="button" onclick="DefinitionFilter()" value="Definition Filter">
			<input type="button" onclick="ClearFilter()" value="Clear All">
			<div class="checkbox"><label>
			<c:choose><c:when test="${class_checked==true}"><input name="definition" type="checkbox" value="class" checked="checked"/>class</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="class"/>class</c:otherwise></c:choose>
			<span class="checkbox_number">${class_number}</span></label></div>
			<div class="checkbox"><label>
			<c:choose><c:when test="${method_checked==true}"><input name="definition" type="checkbox" value="method" checked="checked"/>method</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="method"/>method</c:otherwise></c:choose>
			<span class="checkbox_number">${method_number}</span></label></div>
			<div class="checkbox"><label>
			<c:choose><c:when test="${import_checked==true}"><input name="definition" type="checkbox" value="import" checked="checked"/>import</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="import"/>import</c:otherwise></c:choose>
			<span class="checkbox_number">${import_number}</span></label></div>
			<div class="checkbox"><label>
			<c:choose><c:when test="${comment_checked==true}"><input name="definition" type="checkbox" value="comment" checked="checked"/>comment</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="comment"/>comment</c:otherwise></c:choose>
			<span class="checkbox_number">${comment_number}</span></label></div>
			<div class="checkbox"><label>
			<c:choose><c:when test="${extend_checked==true}"><input name="definition" type="checkbox" value="extend" checked="checked"/>extend</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="extend"/>extend</c:otherwise></c:choose>
			<span class="checkbox_number">${extend_number}</span></label></div>
			<div class="checkbox"><label>
			<c:choose><c:when test="${implement_checked==true}"><input name="definition" type="checkbox" value="implement" checked="checked"/>implement</c:when>
			<c:otherwise><input name="definition" type="checkbox" value="implement"/>implement</c:otherwise></c:choose>
			<span class="checkbox_number">${implement_number}</span></label></div>

			<p></p>
			
			</form>
		</div>
	</div>
	<br/>
	<br/>
	<div class="footer">
		<p class="copyright">
			@2016 buaa.act
		</p>
	</div>
</div>
</body>
</html>
<% es.close(); %>