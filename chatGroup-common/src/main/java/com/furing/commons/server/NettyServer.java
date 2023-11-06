package com.furing.commons.server;

import com.furing.commons.handle.WebsocketHandle;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Objects;

/**
 * @author furing
 */
@Component
@RequiredArgsConstructor
public class NettyServer {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    /**
     * Websocket 协议名称
     */
    private static final String WEBSOCKET_PROTOCOL = "Websocket";

    /**
     * 端口号
     */
    @Value("${webSocket.netty.port:10022}")
    private int port;

    /**
     * webSocket路径
     */
    @Value("${webSocket.netty.path:/webSocket}")
    private String webSocketPath;

    @Resource
    private WebsocketHandle websocketHandle;

    /**
     * 定义两个事件循环组
     * 需要开启一个新的线程来执行NettyServer，防止主线程阻塞而无法调用项目的其他controller接口了
     */
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    @PostConstruct
    public void init() {
        new Thread(this::start).start();
    }

    private void start() {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ChannelFuture channelFuture = null;

        try {
            channelFuture = new ServerBootstrap()
                    // bossGroup辅助客户端的tcp连接请求,workGroup负责与客户端之前的读写操作
                    .group(bossGroup, workerGroup)
                    //设置NIO类型为Channel
                    .channel(NioServerSocketChannel.class)
                    // 设置监听端口
                    .localAddress(new InetSocketAddress(port))
                    // 连接成功后初始化channel
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // 使用http编解码器来编解码websocket
                            ch.pipeline().addLast(new HttpServerCodec());
                            // 将Java对象进行序列化的编解码器
                            ch.pipeline().addLast(new ObjectEncoder());
                            // 以块的方式来写处理器
                            ch.pipeline().addLast(new ChunkedWriteHandler());
                            // 将http数据在传输过程中分段的数据聚合
                            ch.pipeline().addLast(new HttpObjectAggregator(8192));
                            // 将http的协议升级为ws协议，保持长连接
                            ch.pipeline().addLast(new WebSocketServerProtocolHandler(webSocketPath, WEBSOCKET_PROTOCOL, Boolean.TRUE, 65536 * 10));
                            // 解码器 表示禁用缓存，让ObjectDecoder每次都动态解析Java对象的类型
                            ch.pipeline().addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)));
                            // 自定义的webSocketHandle，处理业务逻辑
                            ch.pipeline().addLast(websocketHandle);
                        }
                    }).bind().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            logger.info("Server started and listen on: {}", channelFuture.channel().localAddress());
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放资源
     */
    public void destroy() throws InterruptedException {
        if (Objects.nonNull(bossGroup)) {
            bossGroup.shutdownGracefully().sync();
        }
        if (Objects.nonNull(workerGroup)) {
            bossGroup.shutdownGracefully().sync();
        }
    }

}
