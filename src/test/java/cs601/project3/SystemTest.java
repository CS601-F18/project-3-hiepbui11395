package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.StringTokenizer;

import org.junit.Test;

/**
 * @author hiepbui
 *
 */
public class SystemTest {
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
			String actual = httpFetcher(host, PORT, method, path, requestBody);
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
		Path htmlRequestBody = new File("src/test/resources/right/response/findHandler/postFindB004v9htua.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "POST";
			path = "/find";
			requestBody = "asin=B004V9HTUA";
			String actual = httpFetcher(host, PORT, method, path, requestBody);
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
		Path htmlRequestBody = new File("src/test/resources/right/response/findHandler/getFind.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "GET";
			path = "/find";
			String actual = httpFetcher(host, PORT, method, path, requestBody);
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
		Path htmlRequestBody = new File("src/test/resources/right/response/findHandler/postFindB004v9htua.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "POST";
			path = "/find";
			requestBody = "asin=B004V9HTUA";
			String actual = httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		String response = httpFetcher(host, PORT, method, path, requestBody);
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
		String response = httpFetcher(host, PORT, method, path, requestBody);
		String firstLine = response.split(System.lineSeparator(), 2)[0];
		StringTokenizer tokenizer = new StringTokenizer(firstLine);
		String httpVersion = tokenizer.nextToken();
		String actualStatusCode = tokenizer.nextToken();
		assertEquals(expected, actualStatusCode);
	}

	private static String httpFetcher(String host, int PORT, String method, String path, String body) {
		StringBuffer buf = new StringBuffer();

		try (
				Socket sock = new Socket(host, PORT); //create a connection to the web server
				OutputStream out = sock.getOutputStream(); //get the output stream from socket
				InputStream instream = sock.getInputStream(); //get the input stream from socket
				//wrap the input stream to make it easier to read from
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
				) { 
			String request = null;
			//send request
			request = request(method, host, path, body);

//			if(method.equals("POST")) {
//				request = postRequest(host, path, body);
//			}
			out.write(request.getBytes());
			out.flush();

			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			String line = reader.readLine();
			while(line != null) {				
				buf.append(line + "\n"); //append the newline stripped by readline
				line = reader.readLine();
			}
			sock.close();
		} catch (IOException e) {
			System.out.println("HTTPFetcher::download " + e.getMessage());
		}
		return buf.toString();
	}

	private static String request(String method, String host, String path, String body) {
		String request = method + " " + path + " HTTP/1.1" + "\n" //GET request
				+ "Host: " + host + "\n" //Host header required for HTTP/1.1
				+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
				+ (body!=null? "Content-Length: " + body.length() + "\n" : "")
				+ "\r\n"
				+ (body!=null?body:"");
		return request;
	}
}
