package com.hello.netty.helloNetty.netty.book.step01.client.handler;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

	//utf-8�� �ƴ�
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		String sendMessage = "hello, netty";
		
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(sendMessage.getBytes());
		
		log.info("������ �޽���  [{}]", sendMessage);
		ctx.writeAndFlush(messageBuffer);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		
		String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
		log.info("������ �޽��� [{}]", readMessage);
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.close();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
