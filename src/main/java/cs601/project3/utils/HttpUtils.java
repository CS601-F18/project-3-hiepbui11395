package cs601.project3.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import cs601.project3.http.HttpRequest;

public class HttpUtils {
	public static boolean handleRequestHeader(InputStream in, HttpRequest request) {
		String line = HttpUtils.oneLine(in);
		System.out.println(line);
		if(line==null) {
			return false;
		}

		StringTokenizer parse = new StringTokenizer(line);
		if(parse.countTokens()!=3) {
			return false;
		}
		request.setMethod(parse.nextToken());
		request.setPath(parse.nextToken());
		request.setProtocol(parse.nextToken());

		while((line = HttpUtils.oneLine(in))!=null && !line.trim().isEmpty()) {
			request.addHeader(line);
		}
		return true;
	}

	public static void handleRequestBody(InputStream in, HttpRequest request) {
		int length = Integer.parseInt(request.getHeaders().get("Content-Length"));
		byte[] bytes = new byte[length];
		int read;
		try {
			read = in.read(bytes);
			while(read < length) {
				read += in.read(bytes, read, (bytes.length-read));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String body = new String(bytes);
		request.setBody(body);
	}

	/**
	 * Read a line of bytes until \n character.
	 * @param instream
	 * @return
	 * @throws IOException
	 */
	public static String oneLine(InputStream in) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		byte b;
		try {
			b = (byte) in.read();
			while(b != '\n' && b!=-1) {
				bout.write(b);
				b = (byte) in.read();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String(bout.toByteArray());
	}

	public static void parseQuery(String query, Map<String, 
			Object> parameters) throws UnsupportedEncodingException {
		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0], 
							System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1], 
							System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);
					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}
}
