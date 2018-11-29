package cs601.project3.http;

import java.util.Arrays;
import java.util.List;

/**
 * Contain generic value of http
 * @author hiepbui
 *
 */
public class HttpConstant {
	public static final String GET = "GET";
	public static final String POST = "POST";
	public static final String CONNECTIONCLOSE = "Connection: Close\n";
	public static final String CONNECTIONALIVE = "Connection: Keep Alive\n";
	
	public static final List<String> HEADERKEY = Arrays.asList(
			"a-im","accept","accept-charset","accept-encoding","accept-language","accept-datetime",
			"access-control-request-method","access-control-request-headers","authorization",
			"cache-control","connection","content-length","content-md5","content-type","cookie",
			"date","except","forwarded","from","host","","if-match","if-modified-since",
			"if-none-match","if-range","if-unmodified-since","max-forwards","origin","pragma",
			"proxy-authorization","range","referer","te","user-agent","upgrade","via","warning",
			"upgrade-insecure-requests","x-requested-with","dnt","x-forwarded-for","x-forwarded-host",
			"x-forwarded-proto","front-end-https","x-http-method-override","x-att-deviceid",
			"x-wap-profile","proxy-connection","x-uidh","x-csrf-token","x-request-id","x-correlation-id",
			"save-data");
	
	
}
