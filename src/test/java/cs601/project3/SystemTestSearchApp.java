package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import org.junit.Test;

import cs601.project3.utils.HttpUtils;

/**
 * @author hiepbui
 *
 */
public class SystemTestSearchApp {
	private String host = "localhost";
	private int PORT = 8080;

	/**
	 * Test get FindHandler
	 */
	@Test
	public void testGetFindHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/findHandler/getFind.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "GET";
			path = "/find";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test post FindHandler
	 * asin = b004v9htua
	 */
	@Test
	public void testPostFindHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/findHandler/postFindB001DN5030.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "POST";
			path = "/find";
			requestBody = "asin=B001DN5030";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test get ReviewSearchHandler
	 */
	@Test
	public void testGetReviewSearchHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/reviewSearchHandler/getReviewSearch.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "GET";
			path = "/reviewsearch";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Test post ReviewSearchHandler
	 * key = good
	 */
	@Test
	public void testPostReviewSearchHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/reviewSearchHandler/postReviewSearchGood.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "POST";
			path = "/reviewsearch";
			requestBody = "query=good";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test path not found
	 */
	@Test
	public void pathNotFound() {
		String method = "";
		String path = "";
		String requestBody = null;
		String expected = "404";

		//Get actual result
		method = "GET";
		path = "/findABC";
		String response = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
		String firstLine = response.split(System.lineSeparator(), 2)[0];
		StringTokenizer tokenizer = new StringTokenizer(firstLine);
		String httpVersion = tokenizer.nextToken();
		String actualStatusCode = tokenizer.nextToken();
		assertEquals(expected, actualStatusCode);
	}

	/**
	 * Test method not allowed
	 */
	@Test
	public void methodNotAllowed() {
		String method = "";
		String path = "";
		String requestBody = null;
		String expected = "405";

		//Get actual result
		method = "PUT";
		path = "/find";
		String response = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
		String firstLine = response.split(System.lineSeparator(), 2)[0];
		StringTokenizer tokenizer = new StringTokenizer(firstLine);
		String httpVersion = tokenizer.nextToken();
		String actualStatusCode = tokenizer.nextToken();
		assertEquals(expected, actualStatusCode);
	}
}
