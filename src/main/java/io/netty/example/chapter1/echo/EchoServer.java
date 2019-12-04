package io.netty.example.chapter1.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @author immore
 */
public final class EchoServer {

    public static void main(String[] args) throws Exception {
        // 1. 创建一个线程组, 接收客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        // 2. 创建一个线程组, 处理网络IO操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 3. 创建服务器端启动助手, 接受客户端的连接以及为已接受的连接创建子通道
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            // 4. 设置两个线程组
            serverBootstrap.group(bossGroup, workerGroup)
                    // 5. 使用NioServerSocketChannel作为服务器端通道的实现
                    .channel(NioServerSocketChannel.class)
                    // 6. 设置线程队列中等待连接的个数
                    .option(ChannelOption.SO_BACKLOG, 128)
                    // 7. 保持活跃连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    // 8. 创建一个通道初始化对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 9. 往pipline链中添加自定义的handler类
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            ChannelPipeline p = channel.pipeline();
                            // 10. 往pipeline链中添加一个解码器
                            p.addLast("decoder", new StringDecoder());
                            // 11. 往pipeline链中添加一个编码器
                            p.addLast("encoder", new StringEncoder());
                            // 12. 往pipline链中添加自定义handler
                            p.addLast(new EchoServerHandler());
                        }
                    });
            // 13. 绑定端口, bind方法是异步的, sync同步阻塞
            ChannelFuture sync = serverBootstrap.bind(12888).sync();
            System.out.println("--------------------------Server start-------------------------\n");
            // 14. 关闭通道, 关闭线程组
            sync.channel().closeFuture().sync();
            bossGroup.shutdownGracefully();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            System.out.println("--------------------------Server stop-------------------------\n");
        }
    }
}