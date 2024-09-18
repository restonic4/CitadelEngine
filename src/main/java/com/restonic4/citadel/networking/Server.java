package com.restonic4.citadel.networking;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import com.restonic4.citadel.util.debug.diagnosis.Logger;

public class Server {
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // Accept connections
        EventLoopGroup workerGroup = new NioEventLoopGroup(); // Handle connections
        ByteBuf delimiter = Unpooled.copiedBuffer("\n".getBytes());
        try {
            ServerBootstrap b = new ServerBootstrap(); // Server configuration
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) // TCP channel for server
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new StringDecoder()); // Decode messages from ByteBuf to String
                            pipeline.addLast(new StringEncoder()); // Encode messages from String to ByteBuf
                            pipeline.addLast(new ServerHandler(NetworkingManager.getMessageBus())); // Add your handler
                            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, delimiter));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync(); // Bind to port and start accepting connections
            Logger.log("Server started, listening on " + port);

            f.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
