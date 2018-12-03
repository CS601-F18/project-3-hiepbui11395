package cs601.project3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.gson.JsonObject;

import cs601.project3.utils.HttpUtils;

/**
 * Integration test between application and slack api
 * @author hiepbui
 *
 */
public class IntergrationTest {
	@Test
	public void testIntegrateSlackApi() {
		String apiUrl = "https://slack.com/api/chat.postMessage";
		String token = "xoxb-378520430422-466222641045-prs118PZ7KdIheVzFvf77m2w";
		String channel = "hiepbui";
		String text = "Hi!";
		boolean expectedResult = true;
		JsonObject json = HttpUtils.callApi(apiUrl, token, channel, text);
		assertEquals(expectedResult, json.get("ok").getAsBoolean());
	}

	@Test
	public void testWrongApiUrl() {
		String apiUrl = "https://slack.com/api/chat.postMessageTest";
		String token = "xoxb-378520430422-466222641045-prs118PZ7KdIheVzFvf77m2w";
		String channel = "hiepbui";
		String text = "Hi!";
		boolean expectedResult = false;
		String expectedError = "unknown_method";
		JsonObject json = HttpUtils.callApi(apiUrl, token, channel, text);
		assertEquals(expectedResult, json.get("ok").getAsBoolean());
		assertEquals(expectedError, json.get("error").getAsString());
	}
	
	@Test
	public void testWrongToken() {
		String apiUrl = "https://slack.com/api/chat.postMessage";
		String token = "xoxb-378520430422-466222641045-prs118PZ7KdIheVzFvf";
		String channel = "hiepbui";
		String text = "Hi!";
		boolean expectedResult = false;
		String expectedError = "invalid_auth";
		JsonObject json = HttpUtils.callApi(apiUrl, token, channel, text);
		assertEquals(expectedResult, json.get("ok").getAsBoolean());
		assertEquals(expectedError, json.get("error").getAsString());
	}
	
	@Test
	public void testWrongChannel() {
		String apiUrl = "https://slack.com/api/chat.postMessage";
		String token = "xoxb-378520430422-466222641045-prs118PZ7KdIheVzFvf77m2w";
		String channel = "hiepbuiTest";
		String text = "Hi!";
		boolean expectedResult = false;
		String expectedError = "channel_not_found";
		JsonObject json = HttpUtils.callApi(apiUrl, token, channel, text);
		assertEquals(expectedResult, json.get("ok").getAsBoolean());
		assertEquals(expectedError, json.get("error").getAsString());
	}
}
