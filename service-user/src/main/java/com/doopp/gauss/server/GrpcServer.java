package com.doopp.gauss.server;

import com.doopp.gauss.api.grpc.UserGrpcImpl;
import com.doopp.gauss.server.application.ApplicationProperties;
import com.google.inject.Inject;
import com.google.inject.Injector;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Grpc Server
 */
public class GrpcServer {

    @Inject
    private Injector injector;

    @Inject
    private ApplicationProperties applicationProperties;

    private Server server;

    private void start() throws IOException {

        int port = this.applicationProperties.i("server.port");

        this.server = ServerBuilder.forPort(port)
            .addService(injector.getInstance(UserGrpcImpl.class))
            .build()
            .start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.out.print("*** shutting down gRPC server since JVM is shutting down");
                GrpcServer.this.stop();
                System.out.print("*** server shut down");
            }
        });
        System.out.print("*** start gRPC server in port 0.0.0.0:" + port + "\n");
    }

    private void stop() {
        if (this.server != null) {
            this.server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (this.server != null) {
            this.server.awaitTermination();
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
