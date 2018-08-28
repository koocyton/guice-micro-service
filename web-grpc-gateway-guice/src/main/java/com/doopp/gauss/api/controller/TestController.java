package com.doopp.gauss.api.controller;

import com.doopp.gauss.api.service.AccountService;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.server.annotation.*;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TestController {

    @Inject
    private AccountService accountService;

    @ResponseBody
    public String testCookie(@RequestCookie("session-token") String sessionToken) {
        return sessionToken;
    }

    @ResponseBody
    public User testSession(@RequestSession("currentUser") User currentUser) {
        return currentUser;
    }

    @ResponseBody
    public String testHeader(@RequestHeader("Ueser-Agent") String userAgent) {
        return userAgent;
    }

    @ResponseBody
    public void testGrpcClient() {
        accountService.userByToken("abcdef");
    }
}
