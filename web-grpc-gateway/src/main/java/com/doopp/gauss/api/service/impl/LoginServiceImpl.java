package com.doopp.gauss.api.service.impl;

import com.doopp.gauss.common.dao.UserDao;
import com.doopp.gauss.common.dto.UserDTO;
import com.doopp.gauss.common.entity.User;
import com.doopp.gauss.common.mapper.UserMapper;
import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.utils.CommonUtils;
import com.doopp.gauss.server.configuration.ApplicationProperties;
import com.doopp.gauss.server.redis.CustomShadedJedis;
import com.google.gson.Gson;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import com.ning.http.client.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service("accountService")
public class LoginServiceImpl implements LoginService {

    private final static Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private CustomShadedJedis sessionRedis;

    @Override
    public User tokenUser(String token) {
        // get user by cache
        User user = this.userFromCache(token);
        if (user!=null) {
            return user;
        }
        // get user by passport
        UserDTO userDTO = this.userFromPassport(token);
        if (userDTO!=null) {
            user = userDao.fetchById(userDTO.getId());
            if (user==null) {
                user = UserMapper.INSTANCE.userDTOToUser(userDTO);
                userDao.create(user);
            }
            // set use cache
            this.cacheUserSession(user, token);
            return user;
        }
        // null
        return null;
    }

    private void cacheUserSession(User user, String token) {
        String userId = String.valueOf(user.getUser_id());
        sessionRedis.set(userId.getBytes(), user);
        sessionRedis.set(token, userId);
    }

    private User userFromCache(String token) {
        String userId = sessionRedis.get(token);
        if (userId==null) {
            return null;
        }
        return (User) sessionRedis.get(userId.getBytes());
    }

    private UserDTO userFromPassport(String token) {

        String passportHost = applicationProperties.s("passport.url");
        Response response = CommonUtils.simpleAsyncHttpGet(passportHost + "/api/user-info", new HashMap<String, String>(){{
            put("session-token", token);
        }});

        try {
            if (response.getStatusCode()==HttpResponseStatus.OK.getCode()) {
                return (new Gson()).fromJson(response.getResponseBody(), UserDTO.class);
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            return null;
        }
    }
}
