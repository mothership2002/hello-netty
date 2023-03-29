package com.hello.netty.helloNetty.netty.book.step04.pipeline.server.handler.v4;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerV4FirstHandler extends ChannelInboundHandlerAdapter {
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf readMessage = (ByteBuf) msg;
        System.out.println("first handler channelRead : " 
        		+ readMessage.toString(Charset.defaultCharset()));
//        ctx.write(msg);
        ctx.fireChannelRead(msg);
        
    }
    
    
}