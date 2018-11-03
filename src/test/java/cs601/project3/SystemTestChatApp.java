package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;

import cs601.project3.utils.HttpUtils;

public class SystemTestChatApp {
	private String host = "localhost";
	private int PORT = 9090;
	
	@Test
	public void testGetChatHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/chatHandler/getChat.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "GET";
			path = "/slackbot?abc=1";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPostChatHandler() {
		String method = "";
		String path = "";
		String requestBody = null;
		Path htmlRequestBody = new File("src/test/resources/right/response/chatHandler/postChat.html").toPath();
		byte[] body;
		try {
			body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			//Get actual result
			method = "POST";
			path = "/slackbot";
			requestBody = "message=Hi!";
			String actual = HttpUtils.httpFetcher(host, PORT, method, path, requestBody);
			assertEquals(expected, actual);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
