package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;

import com.doopp.gauss.common.grpc.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private GrpcClient grpcClient;

    @Override
    public User tokenUser(String sessionToken) {
        // grpcClient.greet("koocyton@gmail.com");
        return new User();
    }

    @Override
    public User login(String account, String password) {
        // grpcClient.greet("koocyton@gmail.com");
        User user = new User();
        user.setUser_id(1234L);
        user.setNickname(grpcClient.greet("koocyton@gmail.com"));
        return user;
    }

    @Override
    public User register(String account, String password) {
        return null;
    }

    @Override
    public User logout(String account, String password) {
        return null;
    }
}
