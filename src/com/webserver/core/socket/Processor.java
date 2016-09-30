package com.webserver.core.socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Processor extends Thread {
	private Socket socket;
	private InputStream in;
	private PrintStream out;
	public final static String WEB_ROOT = "D:/Workspaces/MyEclipse Professional/WebServer/mydata/";

	public Processor(Socket socket) {
		System.out.println("Processor starts");
		this.socket = socket;
		try {
			in = socket.getInputStream(); // 得到socket接收的输入流
			out = new PrintStream(socket.getOutputStream()); // PrintStream
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String filename = parse(in); // 从输入流中解析文件名
		System.out.println("filename:" + filename);
		sendFile(filename);
	}

	public String parse(InputStream in) {
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String filename = null;
		try {
			while (true) {
				String httpMessage = br.readLine();
				if (httpMessage == null) {
					break;
				}
				String[] content = httpMessage.split(" ");

				for (int i = 0; i < content.length; i++)
					System.out.print(" " + content[i]);
				System.out.println();

				filename = content[1];
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}

	public void sendErrorMessage(int errorCode, String errorMessage) {
		out.println("HTTP/1.0 " + errorCode + " " + errorMessage);
		out.println("content-type: text/html");
		out.println();
		out.println("<html>");
		out.println("<title>Error Meassage");
		out.println("</title>");
		out.println("<body>");
		out.println("<h1>ErrorCode:" + errorCode + ",Message:" + errorMessage
				+ "</h1>");
		out.println("</body>");
		out.println("</html>");
	}

	public void sendFile(String filename) {
		File file = new File(Processor.WEB_ROOT + filename);
		if (!file.exists()) { 
			System.out.println("404 File Not Found");
			sendErrorMessage(404, "File Not Found");
			return;
		}
		try {
			InputStream in = new FileInputStream(file);
			byte content[] = new byte[(int) file.length()];
			in.read(content);
			out.println("HTTP/1.0 200 send queryfile");
			out.println("content-length:" + content.length);
			out.println();
			out.write(content);
			out.flush();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
