package io.netty.example.chapter1.echo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author immore
 */
@Sharable
public class EchoServerHandler extends SimpleChannelInboundHandler<String> {

    /**
     * 通道就绪
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[Server] : " + ctx.channel().remoteAddress().toString().substring(1) + "连接");
    }

    /**
     * 通道未就绪
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[Server] : " + ctx.channel().remoteAddress().toString().substring(1) + "断开连接");
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
        System.out.println("[Server] received : " + msg);
        // 写到一个 buffer
        ctx.write("Nice to meet you Client~\n");
    }

    /**
     * 数据读取完毕事件
     *
     * @param ctx
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        // 把 buffer 里的数据发送出去
        ctx.flush();
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
