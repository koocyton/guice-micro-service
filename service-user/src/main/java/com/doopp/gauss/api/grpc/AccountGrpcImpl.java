package com.doopp.gauss.api.grpc;

import io.grpc.gauss.account.LoginGrpc;
import io.grpc.gauss.account.LoginReply;
import io.grpc.gauss.account.LoginRequest;
import io.grpc.stub.StreamObserver;

public class AccountGrpcImpl extends LoginGrpc.LoginImplBase{

    @Override
    public void login(LoginRequest request, StreamObserver<LoginReply> responseObserver) {
        super.login(request, responseObserver);
        LoginReply reply = LoginReply.newBuilder().setToken("abc").build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
