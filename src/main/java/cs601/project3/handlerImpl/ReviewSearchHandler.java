package cs601.project3.handlerImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
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

	/**
	 * Handle a GET method and return a html page to search by query
	 * @param request
	 * @param response
	 */
	private void doGet(HttpRequest request, HttpResponse response) {
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<meta charset='UTF-8'/>\n" + 
				"<title>Review Search</title>\n" + 
				"</head>\n" + 
				"<body>\n" + 
				"<h1>Review search</h1>\n");
		if(response.getMessage()!=null && !response.getMessage().isEmpty()) {
			body.append("<h5>" + response.getMessage() + "</h5>\n");
		}
		body.append("<form method='POST'>\n" + 
				"Keyword:<input required='required' type='text' name='" + keyName + "'/>\n" +  
				"<button type='submit'>Submit</button>\n" + 
				"</form>\n" + 
				"</body>\n" + 
				"</html>");

		//Send header to client
		logger.info(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstantHeader.OK_V0);
		response.getPw().write(HttpConstant.CONNECTIONCLOSE);
		response.getPw().write("Content-Length: " + body.length() + "\n");
		response.getPw().write(System.lineSeparator());

		//Send body to client
		response.getPw().write(body.toString());
		response.getPw().close();
	}

	/**
	 * Handle a POST method, find query in inverted index then render and return html page
	 * @param request
	 * @param response
	 */
	private void doPost(HttpRequest request, HttpResponse response) {
		//Get request body
		String value = "";
		int page = 0;
		Map<String, String> parameters = HttpUtils.parseQuery(request.getBody());
		if(parameters.containsKey(keyName) && parameters.get(keyName)!=null) {
			value = parameters.get(keyName).toString();
		} else {
			response.setMessage("Invalid parameters! Try again!\n");
			this.doGet(request, response);
			return;
		}
		if(parameters.containsKey("page") && parameters.get("page")!=null) {
			page = Integer.parseInt(parameters.get("page").toString());
		}
		System.out.println("\n --- Server --- : Search review:\n");
		//Paging the result
		ArrayList<Product> products = Utils.searchByWordApi(value,
				ReviewInvertedIndex.getInstance(), 
				ProductList.getInstance());
		int maxPage = (int) Math.ceil((double)products.size() / maxInPage);
		int firstIndex = page*maxInPage;
		int lastIndex = (page+1)*maxInPage>=products.size()?products.size():(page+1)*maxInPage;
		List<Product> result = new ArrayList<Product>();
		if(!products.isEmpty()) {
			result = products.subList(firstIndex, lastIndex);
		} else {
			firstIndex = -1;
		}
		StringBuilder body = new StringBuilder("<!DOCTYPE html>\n" + 
				"<html>\n" + 
				"<head>\n" + 
				"<style>\n" + 
				"table, th, td {\n" + 
				"    border: 1px solid black;\n" + 
				"}\n" + 
				"</style>\n" +
				"<meta charset='UTF-8'/>\n" + 
				"<title>Review Search Result</title>\n" + 
				"</head>\n" + 
				"<body>\n" +
				"<h1>Result for: " + value + "</h1>\n"+
				"<table>\n" +
				"<tr>\n" +
				"<th>Asin</th>\n" +
				"<th>Review</th>\n" +
				"<th>Score</th>\n" +
				"</tr>\n");
		result.forEach(product -> body.append("<tr>\n" + 
				"<td>"+ product.getAsin()+"</td>\n"+
				"<td>"+ StringEscapeUtils.escapeHtml(((Review)product).getReviewText())+"</td>\n"+
				"<td>"+ ((Review)product).getOverall()+"</td>\n"+
				"</tr>\n"));
		body.append("</table>");
		body.append("<form id='reviewSearchForm' method='POST'>\n" + 
				"<input hidden='hidden' type='text' name='" + keyName + "' value='" + value + "'/>\n" + 
				"<input hidden='hidden' type='text' id='page' name='page' value='" + page + "'/>\n" +  
				"</form>\n" +
				"Total: " + products.size() + "<br/>\n" +
				"From: " + (firstIndex+1) + " - To: " + (lastIndex) + "<br/>\n" );
		if(page>0) {
			body.append("<a id='back' href='#'>Back</a>\n");
		}
		if(page<maxPage-1) {
			body.append("<a id='next' href='#'>Next</a>\n");
		}
		Paging.addScript(body, page, maxPage);
		body.append("</body>\n");
		body.append("</html>");
		//Send header to client
		logger.info(HttpConstantHeader.OK_V0);
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
