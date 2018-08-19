package com.doopp.gauss.server.netty;

import com.doopp.gauss.server.application.ApplicationProperties;
import com.google.inject.Injector;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import com.google.inject.Inject;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyServer {

	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private Injector injector;

	@Inject
	private EventLoopGroup bossGroup;

	@Inject
	private EventLoopGroup workerGroup;

	public void run() throws Exception {

		String host = applicationProperties.s("server.host");
		int port = applicationProperties.i("server.port");

		try {
			ServerBootstrap b = new ServerBootstrap();

			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(channelInitializer())
				.option(ChannelOption.SO_BACKLOG, 128)
				.childOption(ChannelOption.SO_KEEPALIVE, true);

			System.out.print(">>> Running ServerBootstrap on " + host +":" + port + "\n");

			ChannelFuture f = b.bind(host, port).sync();
			f.channel().closeFuture().sync();
		}
		finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	// http://blog.csdn.net/kkkloveyou/article/details/44656325
	// http://blog.csdn.net/joeyon1985/article/details/53586004
	private ChannelInitializer channelInitializer() {
		return new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				// HttpServerCodec：将请求和应答消息解码为HTTP消息
				ch.pipeline().addLast(new HttpServerCodec());
				// HttpObjectAggregator：将HTTP消息的多个部分合成一条完整的HTTP消息
				ch.pipeline().addLast(new HttpObjectAggregator(65536));
				ch.pipeline().addLast(new ChunkedWriteHandler());
				// my application
				ch.pipeline().addLast(new ApplicationHandler(injector, "/game-socket"));
			}
		};
	}
}
