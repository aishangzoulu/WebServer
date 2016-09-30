package com.webserver.core.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class Process {
	private HttpServletRequest request;

	private HttpServletResponse response;

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		this.request = request;
		this.response = response;
		String url = request.getRequestURI();
		System.out.println("运行的页面:" + url);
		String path = "./htm" + url;
		File file = new File(path);

		if (file.exists()) {
			System.out.println(path + "文件存在");
			String msg = "";
			if (request.getParameter("LoginID") == null || request.getParameter("Passwd") == null) {
				msg = "请先输入用户名和密码！";
			}
			else if (request.getParameter("LoginID").equals("qwert") && request.getParameter("Passwd").equals("12345")) {
				msg = "登陆成功！";
			}
			else {
				msg = "帐号或密码错误！";
			}
			sendFile(file, msg);
		}
		else {
			System.out.println(path + "不存在");
			sendNoFoundError(new File("./htm/404.html"));
		}

	}

	private void sendFile(File file, String msg) throws IOException {
		PrintWriter out;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		try {
			out = response.getWriter();
			out.println("HTTP/1.0 200 OK");// 返回应答消息,并结束应答
			out.println("Content-Type:text/html;charset=UTF-8");
			out.println();// 根据 HTTP 协议, 空行将结束头信息
			String line;
			while ((line = br.readLine()) != null) {
				out.println(line);
			}
			out.println("<h1>" + msg + "</h1>");
		}
		finally {
			br.close();
		}
	}

	private void sendNoFoundError(File file) throws IOException {
		PrintWriter out;
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		try {
			out = response.getWriter();
			out.println("HTTP/1.0 404 Not found");// 返回应答消息,并结束应答
			out.println("content-type: text/html");
			out.println();// 根据 HTTP 协议, 空行将结束头信息
			String line;
			while ((line = br.readLine()) != null) {
				out.println(line);
			}
		}
		finally {
			br.close();
		}
	}

}
