package com.doopp.gauss.api.grpc;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.google.inject.Inject;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import io.grpc.stub.StreamObserver;

public class AccountGrpcImpl extends GreeterGrpc.GreeterImplBase{

    @Inject
    private AccountService accountService;

    @Override
    public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
        User user = accountService.getUserByName(req.getName());
        HelloReply reply = (user==null)
            ? HelloReply.newBuilder().setMessage("Hello Null").build()
            : HelloReply.newBuilder().setMessage("Hello " + user.getAccount()).build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
