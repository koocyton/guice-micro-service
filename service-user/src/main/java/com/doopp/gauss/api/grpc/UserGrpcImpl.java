package com.doopp.gauss.api.grpc;

import io.grpc.gauss.ping.PingProto;
import io.grpc.gauss.user.*;
import io.grpc.stub.StreamObserver;

public class UserGrpcImpl extends UserGrpc.UserImplBase {

    @Override
    public void ping(PingProto.Empty request, StreamObserver<PingProto.Empty> responseObserver) {
        super.ping(request, responseObserver);
    }

    @Override
    public void login(LoginReq request, StreamObserver<LoginResp> responseObserver) {
        request.getClientIp();
        super.login(request, responseObserver);
    }

    @Override
    public void register(RegisterReq request, StreamObserver<RegisterResp> responseObserver) {
        super.register(request, responseObserver);
    }
}
