/**
 * 
 */

function SubmitSearch(reserve)
{
	var query = document.getElementById('query').value;
	
	var url = "http://localhost:8080/searchcode/search.jsp?wd=" + query + "&src=HomePage";
	
	window.location = url;
}