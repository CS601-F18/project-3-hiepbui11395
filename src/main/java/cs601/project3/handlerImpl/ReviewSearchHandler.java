package cs601.project3.handlerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cs601.project3.applications.ReviewInvertedIndex;
import cs601.project3.handler.Handler;
import cs601.project3.http.HttpConstant;
import cs601.project3.http.HttpConstantHeader;
import cs601.project3.http.HttpRequest;
import cs601.project3.http.HttpResponse;
import cs601.project3.http.Paging;
import cs601.project3.invertedIndex.Product;
import cs601.project3.invertedIndex.ProductList;
import cs601.project3.invertedIndex.Review;
import cs601.project3.invertedIndex.Utils;
import cs601.project3.utils.HttpUtils;

public class ReviewSearchHandler implements Handler {
	Logger logger = LogManager.getLogger();
	private String keyName = "query";
	private int maxInPage = 10;
	@Override
	public void handle(HttpRequest request, HttpResponse response) {
		switch(request.getMethod()) {
		case HttpConstant.GET:
			this.doGet(request, response);
			break;

		case HttpConstant.POST:
			this.doPost(request, response);
			break;
		default:
			MethodNotFoundHandler methodNotFound = MethodNotFoundHandler.getInstance();
			methodNotFound.handle(request, response);
			break;
		}
	}

	private void doGet(HttpRequest request, HttpResponse response) {
		String body = "<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset='UTF-8'>\n" + 
				"<title>Review Search</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Review search</h1>\n" + 
				"<form method='POST'>\n" + 
				"Keyword:<input required type='text' name='" + keyName + "'>\n" +  
				"<button type='submit'>Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>";

		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write("Content-Length: " + body.length() + "\n");
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body);
		response.getPw().close();
	}

	private void doPost(HttpRequest request, HttpResponse response) {
		//Get request body
		String value = "";
		int page = 0;
		Map<String, String> parameters = HttpUtils.parseQuery(request.getBody());
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString();
		}
		if(parameters.containsKey("page") && parameters.get("page")!=null) {
			page = Integer.parseInt(parameters.get("page").toString());
		}
		System.out.println("\n --- Server --- : Search review:\n");
		ArrayList<Product> products = Utils.searchByWordApi(value,
				ReviewInvertedIndex.getInstance(), 
				ProductList.getInstance());
		int maxPage = products.size()/maxInPage;
		int firstIndex = page*maxInPage;
		int lastIndex = (page+1)*maxInPage>=products.size()?products.size():(page+1)*maxInPage;
		List<Product> result = new ArrayList<Product>();
		if(!products.isEmpty()) {
			result = products.subList(firstIndex, lastIndex);
		}
		StringBuilder body = new StringBuilder("<!DOCTYPE html>" + 
				"<html>" + 
				"<head>" + 
				"<style>" + 
				"table, th, td {" + 
				"    border: 1px solid black;" + 
				"}" + 
				"</style>" +
				"<meta charset='UTF-8'>" + 
				"<title>Review Search Result</title>" + 
				"</head>" + 
				"<body>" +
				"<h1>Result for: " + value + "</h1>"+
				"<table>" +
				"<tr>" +
				"<th>Asin</th><th>Review</th><th>Score</th>" +
				"<tr>");
		result.forEach(product -> body.append("<tr>" + 
				"<th>"+ product.getAsin()+"</th>"+
				"<th>"+ ((Review)product).getReviewText()+"</th>"+
				"<th>"+ ((Review)product).getOverall()+"</th>"+
				"</tr>"));
		body.append("</table>");
		body.append("<form id='reviewSearchForm' method='POST'>\n" + 
				"<input hidden type='text' name='" + keyName + "' value='" + value + "'>\n" + 
				"<input hidden type='text' id='page' name='page' value='" + page + "'>\n" +  
				"</form>\n");
		if(page>0) {
			body.append("<a id='back' href='#'>Back</a>");
		}
		if(page<maxPage-1) {
			body.append("<a id='next' href='#'>Next</a>");
		}
		body.append("</body>\n");
		Paging.addScript(body, page, maxPage);
		body.append("</html>\n");

		//Send header to client
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write("Content-Length: " + body.length() + "\n");
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body.toString());
		response.getPw().close();
		logger.info("\n --- Server --- : Finished\n");
	}

}
