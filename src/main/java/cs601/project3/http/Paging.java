package cs601.project3.http;

public class Paging {
	public static StringBuilder addScript(StringBuilder body, int page, int maxPage) {
		body.append("<script>\n"); 
		if(page>0) {
			body.append("buttonBack = document.getElementById('back'),\n" +
					"	submitBackForm = function(e) {\n" + 
					"		document.getElementById('page').value = \n" +
					"			Number(document.getElementById('page').value) -1;\n"+
					"		e.preventDefault();\n"+ 
					"		document.getElementById('reviewSearchForm').submit();\n"+
					"	};\n"+
					"	buttonBack.addEventListener('click',submitBackForm);");
		}
		if(page<maxPage-1) {
			body.append("	buttonNext = document.getElementById('next'),\n" +
					"	submitNextForm = function(e) {\n" + 
					"		document.getElementById('page').value = \n" +
					"			Number(document.getElementById('page').value) +1;\n"+
					"		e.preventDefault();\n"+ 
					"		document.getElementById('reviewSearchForm').submit();\n"+
					"	};\n"+
					"	buttonNext.addEventListener('click',submitNextForm);\n");
		}
		body.append("</script>\n");
		return body;
		
	}
}
