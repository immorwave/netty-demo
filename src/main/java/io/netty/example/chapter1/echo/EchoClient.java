
package io.netty.example.chapter1.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


/**
 * @author immore
 */
public final class EchoClient {

    public static void main(String[] args) throws Exception {

        // 1. 创建一个线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // 2. 创建客户端的启动助手, 完成相关配置
            Bootstrap bootstrap = new Bootstrap();
            // 3. 设置线程组
            bootstrap.group(group)
                    // 4. 设置客户端通道的实现类
                    .channel(NioSocketChannel.class)
                    // 5. 创建一个通道初始化对象
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) {
                            ChannelPipeline p = channel.pipeline();
                            // 6. 往pipeline链中添加一个解码器
                            p.addLast("decoder", new StringDecoder());
                            // 7. 往pipeline链中添加一个编码器
                            p.addLast("encoder", new StringEncoder());
                            // 8. 往pipline链中添加自定义handler
                            p.addLast(new EchoClientHandler());
                        }
                    });
            // 9. 启动客户端去连接服务器 异步非阻塞, connect是异步的, 它会立马返回一个future对象, sync是同步阻塞的用于等待主线程
            System.out.println("--------------------------Client is ready--------------------------\n");
            ChannelFuture sync = bootstrap.connect("127.0.0.1", 12888).sync();
            // 10. 等待关闭连接 异步非阻塞
            sync.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}
