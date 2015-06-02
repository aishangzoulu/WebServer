package com.webserver.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * <p>Copyright: Copyright (c) 2015<p>
 * <p>succez<p>
 * @author Feizhh
 * @createdate 2015-4-27
 */
public class WebServer implements Runnable {
	ServerSocket serverSocket;

	public static int PORT = 80;// 标准HTTP端口为80

	public static Container container; 

	public WebServer() {
		try {
			serverSocket = new ServerSocket(8091);
		}
		catch (IOException e) {
			throw new RuntimeException();
		}
		new Thread(this).start();
		System.out.println("HTTP服务器正在运行,端口:" + PORT + "\n------------\n");
	}

	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			try {
				Socket client = serverSocket.accept();
				try {
					System.out.println("连接到服务器的用户:" + client);
					// 定义输入输出流
					BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
					PrintWriter out = new PrintWriter(client.getOutputStream(), true);
					// 定义request与response
					HttpServletRequest request = new Request(in);
					if (request.getMethod() == null)
						continue;
					HttpServletResponse response = new Response(out);
					// 处理

					String url = request.getRequestURI(); // 例如/index.html

					// new Process().service(request, response);

					HttpServlet servlet = container.getServlet(url.substring(1));
					if (servlet != null)
						try {
							servlet.service(request, response);
						}
						catch (ServletException e) {
							throw new RuntimeException(e);
						}
					else { //页面不存在
						new Process().service(request, response);
					}

				}
				finally {
					client.close();
					System.out.println(client + "离开了HTTP服务器\n----------------\n");
				}

			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				throw new RuntimeException();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		container = new Container();
		new WebServer();
	}

}
