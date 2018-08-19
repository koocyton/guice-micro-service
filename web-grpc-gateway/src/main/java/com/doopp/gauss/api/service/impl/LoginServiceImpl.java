package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;
import org.springframework.stereotype.Service;


@Service("accountService")
public class LoginServiceImpl implements LoginService {

    @Override
    public User tokenUser(String sessionToken) {
        return null;
    }
}
