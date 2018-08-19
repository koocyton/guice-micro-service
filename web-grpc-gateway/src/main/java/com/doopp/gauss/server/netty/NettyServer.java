package com.doopp.gauss.server.netty;

import com.doopp.gauss.api.grpc.AccountGrpcImpl;
import com.doopp.gauss.api.service.impl.AccountServiceImpl;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class NettyServer {

	private final static Logger logger = LoggerFactory.getLogger(NettyServer.class);

	@Inject
	private ApplicationProperties applicationProperties;

	@Inject
	private Injector injector;

	private Server server;

	private void start() throws IOException {
    /* The port on which the server should run */
		int port = applicationProperties.i("server.port");
		server = ServerBuilder.forPort(port)
				.addService(injector.getInstance(AccountGrpcImpl.class))
				.build()
				.start();
		logger.info("Server started, listening on " + port);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				// Use stderr here since the logger may have been reset by its JVM shutdown hook.
				logger.warn("*** shutting down gRPC server since JVM is shutting down");
				NettyServer.this.stop();
				logger.warn("*** server shut down");
			}
		});
	}

	private void stop() {
		if (server != null) {
			server.shutdown();
		}
	}

	/**
	 * Await termination on the main thread since the grpc library uses daemon threads.
	 */
	private void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	/**
	 * Main launches the server from the command line.
	 */
	public void run() throws IOException, InterruptedException {
		this.start();
		this.blockUntilShutdown();
	}
}



