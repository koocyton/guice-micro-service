package com.doopp.gauss.api.service;

import com.doopp.gauss.common.entity.User;

public interface AccountService {

    User getUserOnLogin(String account, String password);

    User getUserOnRegister(String account, String password);

    User getUserByName(String name);
}
