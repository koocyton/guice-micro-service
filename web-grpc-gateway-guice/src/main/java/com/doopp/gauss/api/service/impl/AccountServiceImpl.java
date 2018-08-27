package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.exception.GaussException;
import com.doopp.gauss.common.grpc.UserGrpcClient;
import com.doopp.gauss.common.util.IdWorker;
import com.google.inject.Inject;
import io.grpc.gauss.user.LoginReply;

public class AccountServiceImpl implements AccountService {

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
    public User userByToken(String token) throws GaussException {
        return new User();
    }
}
