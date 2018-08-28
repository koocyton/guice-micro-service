package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.grpc.UserGrpcClient;
import com.google.inject.Inject;
import io.grpc.gauss.user.LoginReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountServiceImpl implements AccountService {

    private static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Inject
    private UserGrpcClient userGrpcClient;

    @Override
    public String userLogin(String account, String password) {
        LoginReply reply = userGrpcClient.login(account, password);
        return "userLogin";
    }

    @Override
    public String userRegister(String account, String password) {
        return "userRegister";
    }

    @Override
    public String userLogout(String token) {
        return "userLogout";
    }

    @Override
    public User userByToken(String token) {
        return new User();
    }

    @Override
    public LoginReply testGrpc() {
        return userGrpcClient.login("koocyton", "password");
    }
}
