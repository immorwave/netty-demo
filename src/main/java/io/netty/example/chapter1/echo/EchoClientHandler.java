package io.netty.example.chapter1.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author immore
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<String> {

    private final String messageToServer;

    /**
     * 客户端的业务逻辑
     */
    public EchoClientHandler() {
        messageToServer = "Hello, I'm Client~\n";
    }

    /**
     * 通道就绪事件
     *
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(messageToServer);
    }

    /**
     * 读取数据事件
     *
     * @param ctx
     * @param msg 自定义类型的消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        System.out.println("[Client] received : " + msg);
        ctx.write("Nice to meet you Server~\n");
        TimeUnit.SECONDS.sleep(3);
    }

    /**
     * 数据读取完毕事件
     *
     * @param ctx
     * @throws InterruptedException
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws InterruptedException {
        ctx.flush();
        ctx.close();
    }

    /**
     * 发生异常事件
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
        ctx.close();
    }
}
