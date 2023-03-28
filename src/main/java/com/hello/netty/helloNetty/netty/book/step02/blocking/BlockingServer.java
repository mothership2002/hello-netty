package com.hello.netty.helloNetty.netty.book.step02.blocking;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BlockingServer {

	public static void main(String[] args) throws Exception {
		BlockingServer server = new BlockingServer();
		server.run();
	}
	
	private void run() throws Exception {
		ServerSocket server = new ServerSocket(8888);
		log.info("wait connection...");
		
		while( true ) { 
			Socket socket = server.accept();
			log.info("connected client");
			
			OutputStream output = socket.getOutputStream();
			InputStream input = socket.getInputStream();
			
			while ( true ) {
				try {
					int request = input.read();
					output.write(request);
				} catch (Exception e) {
					server.close();
					break;
				}
			}
		}
		
	}
}
