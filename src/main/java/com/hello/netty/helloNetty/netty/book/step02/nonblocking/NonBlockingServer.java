package com.hello.netty.helloNetty.netty.book.step02.nonblocking;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NonBlockingServer {
    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

    private void startEchoServer() {
       try ( //1
          Selector selector = Selector.open(); //2
          ServerSocketChannel serverSocketChannel = ServerSocketChannel.open() //3
        ) {

          if ((serverSocketChannel.isOpen()) && (selector.isOpen())) { //4
             serverSocketChannel.configureBlocking(false);	//5
             serverSocketChannel.bind(new InetSocketAddress(8888));	//6

             serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); //7
             log.info("wait connection...");

             while (true) {
                selector.select(); //8 blocking - > selectNow()
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();//9
                
                while (keys.hasNext()) {
                   SelectionKey key = (SelectionKey) keys.next();
                   keys.remove(); //10

                   if (!key.isValid()) {
                      continue;
                   }

                   if (key.isAcceptable()) { //11
                      this.acceptOP(key, selector);
                   }
                   else if (key.isReadable()) { //12
                      this.readOP(key);
                   }
                   else if (key.isWritable()) { //13
                      this.writeOP(key);
                   }
                }
             }
          }
          else {
             log.info("create socket fail");
          }
       }
       catch (IOException ex) {
          ex.printStackTrace();
       }
    }

    private void acceptOP(SelectionKey key, Selector selector) throws IOException {
       ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel(); //14
       SocketChannel socketChannel = serverChannel.accept(); //15
       socketChannel.configureBlocking(false); //16

       log.info("client connect : {}", socketChannel.getRemoteAddress()); 

       keepDataTrack.put(socketChannel, new ArrayList<byte[]>());
       socketChannel.register(selector, SelectionKey.OP_READ); //17
    }

    private void readOP(SelectionKey key) {
       try {
          SocketChannel socketChannel = (SocketChannel) key.channel();
          buffer.clear();
          int numRead = -1;
          try {
             numRead = socketChannel.read(buffer);
          }
          catch (IOException e) {
             log.error("error read data");
          }

          if (numRead == -1) {
             this.keepDataTrack.remove(socketChannel);
             log.info("client disconnect : {} ", socketChannel.getRemoteAddress());
             socketChannel.close();
             key.cancel();
             return;
          }

          byte[] data = new byte[numRead];
          System.arraycopy(buffer.array(), 0, data, 0, numRead);
          log.info("{} from {}", new String(data, "UTF-8"), socketChannel.getRemoteAddress());

          doEchoJob(key, data);
       }
       catch (IOException ex) {
          ex.printStackTrace();
       }
    }

    private void writeOP(SelectionKey key) throws IOException {
       SocketChannel socketChannel = (SocketChannel) key.channel();

       List<byte[]> channelData = keepDataTrack.get(socketChannel);
       Iterator<byte[]> its = channelData.iterator();

       while (its.hasNext()) {
          byte[] it = its.next();
          its.remove();
          socketChannel.write(ByteBuffer.wrap(it));
       }

       key.interestOps(SelectionKey.OP_READ);
    }

    private void doEchoJob(SelectionKey key, byte[] data) {
       SocketChannel socketChannel = (SocketChannel) key.channel();
       List<byte[]> channelData = keepDataTrack.get(socketChannel);
       channelData.add(data);

       key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
       NonBlockingServer main = new NonBlockingServer();
       main.startEchoServer();
    }
 }