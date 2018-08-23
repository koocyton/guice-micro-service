package com.doopp.gauss.api.controller;

import com.doopp.gauss.api.service.LoginService;
import com.doopp.gauss.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * 用户的 Api Controller
 *
 * Created by henry on 2017/10/14.
 */
@Controller
@RequestMapping(value = "/api")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public User login() {//@RequestBody User user) {
        return loginService.login("abcdefg", "");
    }

    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public User register() {//(@RequestBody User user) {
        return new User();
    }

    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public User logout(@RequestBody User user) {
        return user;
    }
}
