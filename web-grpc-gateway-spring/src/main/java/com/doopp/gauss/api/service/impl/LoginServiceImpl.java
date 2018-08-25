package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;

import com.doopp.gauss.common.grpc.UserGrpcClient;
import com.google.gson.Gson;
import io.grpc.gauss.user.LoginReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {


    private final Logger logger = LoggerFactory.getLogger(UserGrpcClient.class);

    @Autowired
    private UserGrpcClient userGrpcClient;

    @Override
    public User tokenUser(String sessionToken) {
        // grpcClient.greet("koocyton@gmail.com");
        return new User();
    }

    @Override
    public User login(String account, String password) {
        LoginReply reply = userGrpcClient.login("koocyton@gmail.com");
        User user = new User();
        user.setUser_id(1234L);
        user.setNickname(reply.getAuthToken());
        logger.info("" + (new Gson()).toJson(reply));
        logger.info("" + reply);
        logger.info("" + user);
        return user;
        // return user;
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
