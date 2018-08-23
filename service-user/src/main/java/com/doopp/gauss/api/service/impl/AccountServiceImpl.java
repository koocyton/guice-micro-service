package com.doopp.gauss.api.service.impl;


import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.dao.UserDao;
import com.doopp.gauss.common.entity.User;
import com.google.inject.Inject;

public class AccountServiceImpl implements AccountService {

    @Inject
    private UserDao userDao;

    @Override
    public User getUserOnLogin(String account, String password) {
        User user = userDao.fetchByAccount(account);
        if (user==null) {
            return null;
        }
        String hasPassword = user.encryptPassword(password);
        if (!user.getPassword().equals(hasPassword)) {
            return null;
        }
        return user;
    }

    @Override
    public User getUserOnRegister(String account, String password) {
        return null;
    }

    @Override
    public User getUserByName(String name) {
        return userDao.fetchByAccount(name);
    }
}
