package com.hello.netty.helloNetty.netty.book.step04.pipeline.server.handler;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerV1Handler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf readMessage = (ByteBuf) msg;
		System.out.println("channelRead : " + readMessage.toString(Charset.defaultCharset()));
//        ctx.writeAndFlush(msg);
		ctx.write(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}