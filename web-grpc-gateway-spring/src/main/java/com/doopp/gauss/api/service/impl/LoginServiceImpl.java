package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;
import io.grpc.internal.GrpcUtil;
import org.springframework.stereotype.Service;

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Override
    public User tokenUser(String sessionToken) {
        return null;
    }

    @Override
    public User login(String account, String password) {
        return null;
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
