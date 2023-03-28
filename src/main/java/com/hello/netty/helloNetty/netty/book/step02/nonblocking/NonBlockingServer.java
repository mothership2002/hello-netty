package com.hello.netty.helloNetty.netty.book.step02.nonblocking;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.netty.channel.socket.SocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NonBlockingServer {

	private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<SocketChannel, List<byte[]>>();
	private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

	private void startEchoServer() {
		try (Selector selector = Selector.open();
				ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {

			if (serverSocketChannel.isOpen() && selector.isOpen()) {
				serverSocketChannel.configureBlocking(false);
				serverSocketChannel.bind(new InetSocketAddress(8888));
				
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				
				log.info("wait connection...");
				
				while(true) {
					selector.select();
					Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
					
					while (keys.hasNext()) {

					}
				}
			}

		} catch (IOException e) {

		}
	}
}
