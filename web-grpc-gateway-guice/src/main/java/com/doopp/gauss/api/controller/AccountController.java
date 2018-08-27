package com.doopp.gauss.api.controller;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.exception.GaussException;
import com.doopp.gauss.server.annotation.RequestBody;
import com.doopp.gauss.server.annotation.RequestParam;
import com.doopp.gauss.server.annotation.ResponseBody;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class AccountController {

    private static Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Inject
    private AccountService accountService;

    @ResponseBody
    public User userLogin(@RequestBody User user) throws GaussException {
        return user;
    }

    @ResponseBody
    public User userRegister(@RequestParam("account") String account, @RequestParam("password") String password) throws GaussException {
        User user = new User();
        user.setAccount(account);
        user.setPassword(password);
        return user;
    }

    @ResponseBody
    public User userLogout(@RequestParam("token") String token) throws GaussException {
        User user = new User();
        user.setId(111L);
        user.setAccount("koocyton");
        user.setPassword(token);
        return user;
        // accountService.userLogout(token);
    }
}
