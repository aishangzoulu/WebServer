package com.webserver.demo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Head {
	private BufferedReader br;

	private String method;

	private String requestURL;

	private String content;

	private String url;

	private String protocol;

	private Map<String, String> headMap = new HashMap<String, String>();

	// get/post解析后的内容
	private Map<String, String> params = new HashMap<String, String>();

	public Head(BufferedReader br) {
		this.br = br;
	}

	public void parseHead() throws IOException {
		String line = br.readLine();
		System.out.println(line);
		if (line == null)
			return; // 返回无效的请求
		String[] firstLine = line.split(" ");

		if (firstLine.length == 3) {
			this.method = firstLine[0];
			this.requestURL = firstLine[1];
			this.protocol = firstLine[2];
		}

		while ((line = br.readLine()) != null) {
			System.out.println(line);
			if (line.equals("")) {
				break;
			}
			String[] s = line.split(": ");
			headMap.put(s[0], s[1]);
		}

		if ("POST".equals(method)) {
			//读取请求实体content
			int len = Integer.valueOf(headMap.get("Content-Length"));
			char[] ch = new char[len];
			br.read(ch);
			content = new String(ch);
			url = requestURL;
			System.out.println("post请求实体长度:" + len + "，内容:" + content);
			//解析请求实体content
			parsePostParams();
		}
		else if ("GET".equals(method)) {
			// 解析get提交的参数requestURL
			parseGetParams();
		}
		else { // 其他提交方法
			return;
		}
	}

	private void parseGetParams() {
		int k = requestURL.indexOf("?");
		if (k == -1) {
			url = requestURL;
			return; // 没有传参数
		}
		url = requestURL.substring(0, k);
		String msg = requestURL.substring(k + 1);
		String[] split = msg.split("&");
		for (String param : split) {
			String[] split2 = param.split("=");
			params.put(split2[0], split2[1]);
		}
	}

	private void parsePostParams() {
		String[] split = content.split("&");
		for (String param : split) {
			String[] split2 = param.split("=");
			params.put(split2[0], split2[1]);
		}
	}

	public String getByMap(String key) {
		return headMap.get(key);
	}

	public BufferedReader getBr() {
		return br;
	}

	public String getMethod() {
		return method;
	}

	public String getUrl() {
		return url;
	}

	public String getProtocol() {
		return protocol;
	}

	public String getContent() {
		return content;
	}

	public Map<String, String> getParams() {
		return params;
	}

}
