package cs601.project3;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Test;
import org.junit.runner.JUnitCore;

import cs601.project3.http.HttpRequest;
import cs601.project3.utils.HttpUtils;

public class SearchApplicationTest {

	@Test
	public void testParsingRequestHeader() {
		HttpRequest request = new HttpRequest();
		
		try {
			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), true);

			htmlRequest = new File("src/test/resources/right/getTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), true);

			htmlRequest = new File("src/test/resources/wrong/postTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), false);
			

			htmlRequest = new File("src/test/resources/wrong/getTest.txt");
			inputStream = new FileInputStream(htmlRequest);
			assertEquals(HttpUtils.handleRequestHeader(inputStream, request), false);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRequestHeaderContent() {
		HttpRequest request = new HttpRequest();
		
		try {
			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			Path htmlRequestBody = new File("src/test/resources/right/postBodyTest.txt").toPath();
			byte[] body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);
			InputStream in = new FileInputStream(htmlRequest);
			HttpUtils.handleRequestHeader(in, request);
			assertEquals(request.getMethod(), "POST");
			assertEquals(request.getPath(), "/slackbot");
			assertEquals(request.getProtocol(), "HTTP/1.1");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	public void testGetRequestBody() {
		HttpRequest request = new HttpRequest();
		
		try {
			Path htmlRequestBody = new File("src/test/resources/right/postBodyTest.txt").toPath();
			byte[] body = Files.readAllBytes(htmlRequestBody);
			String expected = new String(body);

			File htmlRequest = new File("src/test/resources/right/postTest.txt");
			InputStream in = new FileInputStream(htmlRequest);
			HttpUtils.handleRequestHeader(in, request);
			HttpUtils.handleRequestBody(in, request);
			String actual = request.getBody();
			assertEquals(actual, expected);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
