package com.webserver.demo.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Container {

	public final static String pack = "com.webserver.core.";

	private Map<String, HttpServlet> servletMap = new HashMap<String, HttpServlet>();

	public Container() {
		init();
	}

	public void init() {
		File xml = new File("web.xml");
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder;

			builder = factory.newDocumentBuilder();

			Document doc = builder.parse(xml);
			NodeList list = doc.getElementsByTagName("servlet-mapping");
			int len = list.getLength();
			for (int i = 0; i < len; i++) {
				String servletName = doc.getElementsByTagName("servlet-name").item(i).getFirstChild().getNodeValue();
				String urlPattern = doc.getElementsByTagName("url-pattern").item(i).getFirstChild().getNodeValue();
				Class<?> servletClass = Class.forName(pack + servletName);
				HttpServlet newInstance = (HttpServlet) servletClass.getConstructor(new Class[] {}).newInstance(
						new Object[] {});
				servletMap.put(urlPattern, newInstance);
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public HttpServlet getServlet(String urlPattern) {
		return servletMap.get(urlPattern);

	}

	public static void main(String[] args) {
		new Container();
	}

}
