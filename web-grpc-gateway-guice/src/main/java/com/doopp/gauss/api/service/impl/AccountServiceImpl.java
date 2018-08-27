package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.exception.GaussException;
import com.doopp.gauss.common.util.IdWorker;
import com.google.inject.Inject;

public class AccountServiceImpl implements AccountService {

    @Override
    public String userLogin(String account, String password) {
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
