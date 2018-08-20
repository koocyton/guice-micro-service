package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("accountService")
public class LoginServiceImpl implements LoginService {

    @Autowired
    private GrpcClient grpcClient;

    @Override
    public User tokenUser(String sessionToken) {
        return new User();
    }
}
