package com.doopp.gauss.api.service;

import com.doopp.gauss.common.entity.User;

public interface LoginService {

    User tokenUser(String sessionToken);
}
