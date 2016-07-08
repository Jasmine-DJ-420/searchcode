/**
 * 
 */

function SubmitSearch(reserve)
{
	var query = document.getElementById('query').value;
	
	var url = "/searchcode/search.jsp?query=" + query + "&src=HomePage";
	
	window.location = url;
}

function SubmitGroupDivide(definition)
{
	var query = document.getElementById('query').value;
	
	var url = "/searchcode/subgroup.jsp?query=" + query;
	var url_def = "/searchcode/subgroup.jsp?query=" + query + "&definition=" + definition;
	
	if(definition=="")
		window.location.href = url;
	else
		window.location.href = url_def;
}


function fullTable(tableID,codes,startline) {
	// Get a reference to the table
	var tableRef = document.getElementById(tableID);	
	var row,tr,td1,td2,tn1,tn2,tbody,tmp;
	
	// set tr and td tag
	for (row=0,row_num=Number(startline); row<codes.length; row++,row_num++){
		tbody = document.createElement('tbody');
		tr = document.createElement('tr');
		td1 = document.createElement('td');
		td2 = document.createElement('td');
		tn1 = document.createTextNode(row_num);
		tmp = new String(codes[row]);
		tn2 = document.createTextNode(tmp);
		td1.appendChild(tn1);
		td1.className = "line-numbers";
		td2.appendChild(tn2);
		td2.id = "code-row";
		tr.appendChild(td1);
		tr.appendChild(td2);
		tbody.appendChild(tr);
		
		tableRef.appendChild(tbody);
	}
}

function EnterSearch()
{
	if(event.keyCode==13)
		SubmitSearch();
}

