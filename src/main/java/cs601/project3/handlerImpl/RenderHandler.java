package cs601.project3.handlerImpl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import cs601.project3.handler.Handler;
import cs601.project3.http.HttpServer;
import cs601.project3.utils.FileHandler;

public class RenderHandler implements Handler {

	@Override
	public void handle(String target, PrintWriter out, BufferedOutputStream bos) {
		//Send header with data to client
		out.println("HTTP/1.1 200 OK");
		out.println();
		out.flush();


		//Send body
		target += ".html";
		File file = new File(HttpServer.WEB_ROOT, target);
		int fileLength = (int)file.length();
		byte[] fileData = null;
		try {
			fileData = FileHandler.readFileData(file, fileLength);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			bos.write(fileData, 0, fileLength);
			bos.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
