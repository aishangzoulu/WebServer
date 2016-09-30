package com.webserver.core.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

	public void serverStart(int port) {

		try {
			ServerSocket serverSocket = new ServerSocket(port);
			while (true) {
				System.out.println("before accept ");
				Socket socket = serverSocket.accept();
				System.out.println("after accept ");
				new Processor(socket).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		int port = 8089;
		new WebServer().serverStart(port);

	}

}
