package com.doopp.gauss.api.service;

import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.exception.GaussException;
import io.grpc.gauss.user.LoginReply;

public interface AccountService {

    String userLogin(String account, String password);

    String userRegister(String account, String password);

    String userLogout(String token);

    User userByToken(String token);

    LoginReply testGrpc();
}
