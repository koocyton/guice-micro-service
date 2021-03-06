package com.doopp.gauss.server.dispatcher;

import java.util.HashMap;

public class DispatchRule {

    public static final HashMap<String, String> rules = new HashMap<>(256);

    static {
        // 测试
        rules.put("/test/testCookie", "test.testCookie");
        rules.put("/test/testHeader", "test.testHeader");
        rules.put("/test/testSession", "test.testSession");
        rules.put("/test/testGrpcClient", "test.testGrpcClient");

        // 登陆
        rules.put("POST /login", "account.userLogin");
        // 注册
        rules.put("POST /register", "account.userRegister");
        // 退出
        rules.put("/logout", "account.userLogout");
    }
}
