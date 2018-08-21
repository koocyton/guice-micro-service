package com.doopp.gauss.api.service;

import com.doopp.gauss.common.entity.User;

public interface LoginService {

    User tokenUser(String sessionToken);

    User login(String account, String password);

    User register(String account, String password);

    User logout(String account, String password);
}
