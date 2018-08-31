package com.doopp.gauss.api.grpc;

import io.grpc.gauss.model.ModelProto;
import io.grpc.gauss.ping.PingProto;
import io.grpc.gauss.user.*;
import io.grpc.stub.StreamObserver;

public class UserGrpcImpl extends UserGrpc.UserImplBase {

    @Override
    public void ping(PingProto.Empty request, StreamObserver<PingProto.Empty> responseObserver) {
        super.ping(request, responseObserver);
    }

    @Override
    public void login(LoginRequest request, StreamObserver<LoginReply> responseObserver) {
        LoginReply reply = LoginReply.newBuilder().setToken("linghou 119").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
