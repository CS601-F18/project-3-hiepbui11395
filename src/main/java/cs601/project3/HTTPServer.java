package cs601.project3;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

//Each client connection will work with one different httpServer
public class HTTPServer implements Runnable {
	static final File WEB_ROOT = new File(".");
	static final String DEFAULT_FILE = "index.html";
	static final String FILE_NOT_FOUND = "404.html";
	static final String METHOD_NOT_SUPPORTED = "not_supported.html";

	//port to listen connection
	static final int PORT = 8080;

	static final boolean verbose = true;

	//Client connection via socket class
	private Socket connect;

	public HTTPServer(Socket c) {
		connect = c;
	}

	public static void main(String[] args) {
		try(ServerSocket serverSocket = new ServerSocket(PORT)
				) {
			System.out.println("Server started: ");
			//listen until user request
			while(true) {
				HTTPServer httpServer = new HTTPServer(serverSocket.accept());
				if(verbose) {
					System.out.println("Connection opened. (" + new Date() + ")");
				}

				//Create thread to manage client connection
				Thread thread = new Thread(httpServer);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
		@Override
		public void run() {
			BufferedReader in = null;
			PrintWriter out = null;
			BufferedOutputStream bos = null;
			String fileRequested = null;
	
			try {
				//Read data from client
				in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
				//Get character output stream to client(for header)
				out = new PrintWriter(connect.getOutputStream());
				//get binary output stream to client(for data)
				bos = new BufferedOutputStream(connect.getOutputStream());
	
				//Get the first line of request from client
				String input = in.readLine();
				if(input==null) {
					return;
				}
				//Parse request with string tokenizer
				StringTokenizer parse = new StringTokenizer(input);
				//Get HTTP method from client
				String method = parse.nextToken().toUpperCase();
				//Get file requested
				fileRequested = parse.nextToken().toLowerCase();
	
				//Only support GET and POST
				if(method.equals("GET")) {
					this.getMethod(fileRequested, out, bos);
				} else if(method.equals("POST")) {
					this.postMethod(fileRequested, out, bos);
				} else {
					this.notFoundMethod(method, out, bos);
				}
			} catch(FileNotFoundException fnfe) {
				try {
					fileNotFound(out, bos, fileRequested);
				} catch (IOException ioe) {
					System.err.println("Error with file not found exception : " + ioe.getMessage());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	private void notFoundMethod(String method, PrintWriter out, BufferedOutputStream bos) {
		if(verbose) {
			System.out.println("501 Not Implemented :" + method);
		}

		//Return not supported to the client
		File file = new File(WEB_ROOT, METHOD_NOT_SUPPORTED);
		int fileLength = (int) file.length();
		String contentMimeType = "text/html";
		//Read content to return to client
		byte[] fileData = null;
		try {
			fileData = readFileData(file, fileLength);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Send header with data to client
		out.println("HTTP/1.1 501 Not Implemented");
		out.println("Server: Java HTTP Server 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + contentMimeType);
		out.println("Content-length: " + fileLength);
		out.println();
		out.flush();
		//data
		try {
			bos.write(fileData, 0, fileLength);
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void getMethod(String fileRequested, PrintWriter out, BufferedOutputStream bos) {
		//GET method
		if(fileRequested.endsWith("/")) {
			fileRequested += DEFAULT_FILE;
		}

		File file = new File(WEB_ROOT, fileRequested);
		int fileLength = (int)file.length();
		String content = getContentType(fileRequested);
		byte[] fileData = null;
		try {
			fileData = readFileData(file, fileLength);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//Send header with data to client
		out.println("HTTP/1.1 501 Not Implemented");
		out.println("Server: Java HTTP Server 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println();
		out.flush();

		try {
			bos.write(fileData, 0, fileLength);
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(verbose) {
			System.out.println("File " + fileRequested + " of type " + content + " returned");
		}
	}

	private void postMethod(String fileRequested, PrintWriter out, BufferedOutputStream bos) {
		//GET method

		if(verbose) {
			System.out.println("POST method called");
		}
	}

	private byte[] readFileData(File file, int fileLength) throws IOException {
		FileInputStream fileIn = null;
		byte[] fileData = new byte[fileLength];
		
		try {
			fileIn = new FileInputStream(file);
			fileIn.read(fileData);
		} finally {
			if (fileIn != null) 
				fileIn.close();
		}
		
		return fileData;
	}

	private String getContentType(String fileRequested) {
		if(fileRequested.endsWith(".htm") || fileRequested.endsWith(".html")) {
			return "text/html";
		} else {
			return "text/plain";
		}
	}

	private void fileNotFound(PrintWriter out, OutputStream dataOut, String fileRequested) throws IOException {
		File file = new File(WEB_ROOT, FILE_NOT_FOUND);
		int fileLength = (int) file.length();
		String content = "text/html";
		byte[] fileData = readFileData(file, fileLength);

		out.println("HTTP/1.1 404 File Not Found");
		out.println("Server: Java HTTP Server from SSaurel : 1.0");
		out.println("Date: " + new Date());
		out.println("Content-type: " + content);
		out.println("Content-length: " + fileLength);
		out.println(); // blank line between headers and content, very important !
		out.flush(); // flush character output stream buffer

		dataOut.write(fileData, 0, fileLength);
		dataOut.flush();

		if (verbose) {
			System.out.println("File " + fileRequested + " not found");
		}
	}
}
