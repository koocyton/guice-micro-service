package com.doopp.gauss.common.grpc;

import com.doopp.gauss.server.filter.SessionFilter;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.gauss.user.LoginReply;
import io.grpc.gauss.user.LoginRequest;
import io.grpc.gauss.user.UserGrpc;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;

@Service("userGrpcClient")
public class UserGrpcClient {

    private final Logger logger = LoggerFactory.getLogger(UserGrpcClient.class);

    private final ManagedChannel channel;

    private final UserGrpc.UserBlockingStub blockingStub;

    public UserGrpcClient() {
        this("127.0.0.1", 8091);
    }

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    private UserGrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext(true)
            .build());
    }

    /** Construct client for accessing RouteGuide server using the existing channel. */
    private UserGrpcClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = UserGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public LoginReply login(String name) {
        // logger.info("Will try to greet " + name + " ...");
        LoginRequest request = LoginRequest.newBuilder().setClientIp(name).build();
        LoginReply reply;
        try {
            return blockingStub.login(request);
        }
        catch (StatusRuntimeException e) {
            // logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
        // return reply.getMessage();
        // logger.info("Greeting: " + response.getMessage());
    }
}
